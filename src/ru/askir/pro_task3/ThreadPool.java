package ru.askir.pro_task3;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import static java.lang.Thread.sleep;

public class ThreadPool {
    private ReentrantLock listLock = new ReentrantLock();
    private boolean isShutdown;
    private int threadCount;
    private Thread schedule;
    private Thread[] threadArray;
    private LinkedList<Runnable> taskList;

    public ThreadPool(int threadCount) {
        this.threadCount = threadCount;
        this.threadArray = new Thread[threadCount];
        this.taskList = new LinkedList<>();
        this.isShutdown = false;
        createSchedule();
    }

    private void createSchedule() {
        schedule = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    listLock.lock();
                    try {
                        if (isShutdown && taskList.size() == 0) {
                            return;
                        }

                        if (taskList.size() > 0) {
                            int freeThreadIndex = getFreeThreadIndex();
                            if (freeThreadIndex >= 0) {
                                Runnable first = taskList.removeFirst();

                                threadArray[freeThreadIndex] = new Thread(first);
                                threadArray[freeThreadIndex].start();
                            }
                        }
                    }
                    finally {
                        listLock.unlock();
                    }

                    sleep(100);
                }
            } catch(InterruptedException exception) {
                return;
            }
        });
        schedule.start();
    }

    private int getFreeThreadIndex() {
        for (int i = 0; i < threadCount; i++) {
            if(threadArray[i] == null || !threadArray[i].isAlive())
                return i;
        }
        return -1;
    }

    private boolean shutdownDone(){
        if(isShutdown && taskList.size() == 0) {
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
            if(isShutdown)
                throw new IllegalStateException("Пул в состоянии завершения");

            int freeThreadIndex = getFreeThreadIndex();
            if(freeThreadIndex < 0)
                taskList.add(runnable);
            else {
                threadArray[freeThreadIndex] = new Thread(runnable);
                threadArray[freeThreadIndex].start();
            }
        }
        finally {
            listLock.unlock();
        }
    }

    public void shutdown(){
        isShutdown = true;
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
