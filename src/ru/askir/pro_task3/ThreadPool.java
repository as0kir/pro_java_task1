package ru.askir.pro_task3;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import static java.lang.Thread.sleep;

public class ThreadPool {
    private ReentrantLock listLock = new ReentrantLock();
    private AtomicBoolean isShutdown;
    private int threadCount;
    private Thread[] threadArray;
    private LinkedList<Runnable> taskList;

    public ThreadPool(int threadCount) {
        this.threadCount = threadCount;
        this.threadArray = new Thread[threadCount];
        this.taskList = new LinkedList<>();
        this.isShutdown = new AtomicBoolean(false);

        for (int i = 0; i < threadCount; i++) {
            this.threadArray[i] = new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        Runnable firstTask = null;

                        listLock.lock();
                        try {
                            if (isShutdown.get() && taskList.size() == 0) {
                                return;
                            }
                            if (taskList.size() > 0) {
                                firstTask = taskList.removeFirst();
                            }
                        }
                        finally {
                            listLock.unlock();
                        }

                        if(firstTask != null)
                            firstTask.run();

                        sleep(100);
                    }
                } catch(InterruptedException exception) {
                    return;
                }
            });
            this.threadArray[i].start();
        }
    }

    private boolean shutdownDone(){
        if(isShutdown.get() && taskList.size() == 0) {
            boolean allDone = true;
            for (int i = 0; i < threadCount; i++) {
                if(threadArray[i] != null && threadArray[i].isAlive())
                    allDone = false;
            }
            return allDone;
        }
        return false;
    }

    public void execute(Runnable runnable){
        listLock.lock();

        try {
            if(isShutdown.get())
                throw new IllegalStateException("Пул в состоянии завершения");

            taskList.add(runnable);
        }
        finally {
            listLock.unlock();
        }
    }

    public void shutdown(){
        isShutdown.set(true);
    }

    public void awaitTermination() {
        while (!shutdownDone()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
