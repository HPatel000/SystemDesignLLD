package Practice.QuickRevision;

public class RateLimitSystem {
    public static void main(String[] args) throws InterruptedException {
        RateLimiter rateLimiter = new RateLimiter(new TokenBucket(10, 1000));
        for(int i=0; i<15; i++) {
            if(rateLimiter.isRequestAllowed()) System.out.println(i+" "+"allowed");
            else System.out.println(i+" "+"blocked");
            Thread.sleep(200);
        }
    }
}

class RateLimiter {
    RateLimiterStrategy rateLimiterStrategy;

    RateLimiter(RateLimiterStrategy _rateLimiterStrategy) {
        rateLimiterStrategy = _rateLimiterStrategy;
    }

    public Boolean isRequestAllowed() {
        return rateLimiterStrategy.isRequestAllowed();
    }
}

interface RateLimiterStrategy {
    boolean isRequestAllowed();
}

class TokenBucket implements RateLimiterStrategy {
    private final int capacity;
    private int tokens;
    private long lastTokenAddedAt;
    private final long refillIntervalInMillis;
    TokenBucket(int _capacity, long _refillIntervalInMillis ) {
        capacity = _capacity;
        tokens = _capacity;
        refillIntervalInMillis = _refillIntervalInMillis ;
        lastTokenAddedAt = System.currentTimeMillis();
    }

    public synchronized boolean isRequestAllowed() {
        refill();
        if(tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    private synchronized void refill() {
        long now = System.currentTimeMillis();
        long elapsedTime = now - lastTokenAddedAt;
        long tokensToAdd = elapsedTime / refillIntervalInMillis;

        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + (int) tokensToAdd);
            lastTokenAddedAt += tokensToAdd * refillIntervalInMillis;
        }
    }
}

class LeakyBucket implements RateLimiterStrategy {

    @Override
    public boolean isRequestAllowed() {
        throw new UnsupportedOperationException("LeakyBucket is not yet implemented.");
    }
}
