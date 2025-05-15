package Practice.QuickRevision;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.UUID;

public class TaskSchedulerSystem {
    public static void main(String[] args) {
        TaskScheduler taskScheduler = new TaskScheduler();

        taskScheduler.addTask(() -> System.out.println("Task 1 At: "+System.currentTimeMillis()), 2000);
        taskScheduler.addTask(() -> System.out.println("Task 2 At: "+System.currentTimeMillis()));
    }
}


class Task {
    private UUID id;
    private Runnable task;
    private final long intervalMillis; // 0 if not recurring
    private long scheduledAtMillis;

    Task(Runnable _task) {
        this(_task, 0);
    }

    Task(Runnable _task, long _intervalMillis) {
        id = UUID.randomUUID();
        task = _task;
        intervalMillis = _intervalMillis;
        scheduledAtMillis =  getCurrTimeMillis();
    }

    public Runnable getTask() {
        return task;
    }

    public long getScheduledAtMillis() {
        return scheduledAtMillis;
    }

    public Task setNextScheduledAtMillis() {
        if(intervalMillis == 0) return null;
        scheduledAtMillis = getCurrTimeMillis() + intervalMillis;
        return this;
    }

    private long getCurrTimeMillis() {
        return System.currentTimeMillis();
    }
}

class TaskScheduler {
    PriorityQueue<Task> taskPriorityQueue;
    final Object lock;
    TaskScheduler() {
        taskPriorityQueue = new PriorityQueue<>(
                Comparator.comparingLong(Task::getScheduledAtMillis)
        );
        lock = new Object();
        startWorker();
    }

    public void addTask(Runnable task) {
        this.addTask(task, 0);
    }

    public void addTask(Runnable task, long intervalMillis) {
        synchronized(lock) {
            taskPriorityQueue.add(new Task(task, intervalMillis));
            lock.notify();
        }
    }

    private void startWorker(){
        Thread worker = new Thread(() -> {
            while(true) {
                synchronized(lock) {
                    if(taskPriorityQueue.isEmpty()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Task nextTask = taskPriorityQueue.peek(), taskToExecute = null;
                    long currTimeMillis = System.currentTimeMillis();
                    if(nextTask != null){
                        if (nextTask.getScheduledAtMillis() <= currTimeMillis) {
                            taskToExecute = taskPriorityQueue.poll();
                        } else {
                            long sleepTime = nextTask.getScheduledAtMillis() - currTimeMillis;
                            try {
                                lock.wait(sleepTime);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        if(taskToExecute != null) {
                            try {
                                taskToExecute.getTask().run();
                                Task recurrTask = taskToExecute.setNextScheduledAtMillis();
                                if(recurrTask != null) taskPriorityQueue.add(recurrTask);
                            } catch (Exception e) {
//                                log error
                                System.out.println("ERROR IN TASK EXECUTION");
                            }
                        }
                    }
                }

            }
        });
//        program will close on completion of main thread
//        worker.setDaemon(true);
        worker.start();
    }
}
