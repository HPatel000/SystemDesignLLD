package Practice.QuickRevision;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationSystem {
    public static void main(String[] args) throws InterruptedException {
        NotificationDispatcher notificationDispatcher = new NotificationDispatcher(5);
        StrategyRegistry strategyRegistry = new StrategyRegistry();
        strategyRegistry.register(NOTIFICATION_TYPE.EMAIL, new EmailNotificationStrategy());
        strategyRegistry.register(NOTIFICATION_TYPE.PUSH, new PushNotificationStrategy());
        strategyRegistry.register(NOTIFICATION_TYPE.SMS, new SMSNotificationStrategy());
        RetryPolicy retryPolicy = new RetryPolicy(3, 1000);
        NotificationManager notificationManager = new NotificationManager(retryPolicy, strategyRegistry, notificationDispatcher);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    notificationManager.sendNotification(NOTIFICATION_TYPE.values()[finalI % 3], "A" + finalI, "B" + finalI, "Message " + finalI);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

        // Wait and shutdown
        try { Thread.sleep(5000); } catch (InterruptedException e) { }
        notificationManager.shutdown();
    }
}

class NotificationManager {
    private final RetryPolicy retryPolicy;
    private final StrategyRegistry strategyRegistry;
    private final NotificationDispatcher notificationDispatcher;
    NotificationManager(RetryPolicy _retryPolicy, StrategyRegistry _strategyRegistry, NotificationDispatcher _notificationDispatcher) {
        retryPolicy = _retryPolicy;
        strategyRegistry = _strategyRegistry;
        this.notificationDispatcher = _notificationDispatcher;
    }
    public void sendNotification(NOTIFICATION_TYPE notificationType, String from, String to, String content) throws InterruptedException {
        NotificationStrategy notificationStrategy = strategyRegistry.get(notificationType);
        Notification notification = new Notification(from, to, content);
        notificationDispatcher.dispatch(notification, notificationStrategy, retryPolicy);
    }

    public void shutdown() {
        notificationDispatcher.shutdown();
    }
}

class NotificationDispatcher {
    private final ExecutorService executor;

    public NotificationDispatcher(int threadPoolSize) {
        this.executor = Executors.newFixedThreadPool(threadPoolSize);
    }

    public void dispatch(Notification notification, NotificationStrategy strategy, RetryPolicy retryPolicy) {
        executor.submit(() -> {
            boolean delivered = retryPolicy.executeWithRetry(notification, strategy);
            if (!delivered) {
                System.out.println("Delivery failed: ");
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
    }
}

record Notification (String from, String to, String content) { }

class StrategyRegistry {
    private final Map<NOTIFICATION_TYPE, NotificationStrategy> strategyMap = new HashMap<>();

    public void register(NOTIFICATION_TYPE type, NotificationStrategy strategy) {
        strategyMap.put(type, strategy);
    }

    public NotificationStrategy get(NOTIFICATION_TYPE type) {
        if (!strategyMap.containsKey(type)) {
            throw new IllegalArgumentException("No strategy registered for type: " + type);
        }
        return strategyMap.get(type);
    }
}

interface NotificationStrategy {
    boolean sendNotification(Notification notification);
}

class EmailNotificationStrategy implements NotificationStrategy {
    @Override
    public boolean sendNotification(Notification notification) {
        System.out.println("Email Notification sent from :"+notification.from()+ ", to: "+notification.to()+", content: "+notification.content());
        return true;
    }
}

class SMSNotificationStrategy implements NotificationStrategy {
    @Override
    public boolean sendNotification(Notification notification) {
        System.out.println("SMS Notification sent from :"+notification.from()+ ", to: "+notification.to()+", content: "+notification.content());
        return true;
    }
}

class PushNotificationStrategy implements NotificationStrategy {
    @Override
    public boolean sendNotification(Notification notification) {
        System.out.println("Push Notification sent from :"+notification.from()+ ", to: "+notification.to()+", content: "+notification.content());
        return false;
    }
}

class RetryPolicy {
    private final int maxRetries;
    private final long retryIntervalMillis;

    RetryPolicy(int _maxRetries, long _retryIntervalMillis) {
        maxRetries = _maxRetries;
        retryIntervalMillis = _retryIntervalMillis;
    }

    boolean executeWithRetry(Notification notification, NotificationStrategy notificationStrategy) {
        int attempts = 0;
        boolean isSent = false;
        while(!isSent && attempts < maxRetries) {
            if(attempts > 0) System.out.println("Retrying...");
            isSent = notificationStrategy.sendNotification(notification);
            attempts++;
            if(isSent) {
                System.out.println("Notification Sent");
                return true;
            } else if(attempts < maxRetries) {
                try {
                    Thread.sleep((int) Math.pow(2, attempts) * retryIntervalMillis);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            } else return false;
        }
        return false;
    }
}

enum NOTIFICATION_TYPE {
    EMAIL, SMS, PUSH
}
