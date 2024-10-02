package ru.askir.pro_task3;

public class TestThreadPool {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(5);

        for (int i = 0; i < 10; i++) {
            final int w = i;
            threadPool.execute(()->{
                try {
                    System.out.println(String.format("Поток %s стартовал ", w));
                    Thread.sleep(100 + (int) (5000 * Math.random()));
                    System.out.println(String.format("Поток %s окончен ", w));
                }
                catch (InterruptedException exception) {
                    System.out.println(String.format("Поток %s прерван ", w));
                }
            });
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 10; i++) {
            final int w = i;
            threadPool.execute(()->{
                try {
                    System.out.println(String.format("Поток %s стартовал ", w));
                    Thread.sleep(100 + (int) (5000 * Math.random()));
                    System.out.println(String.format("Поток %s окончен ", w));
                }
                catch (InterruptedException exception) {
                    System.out.println(String.format("Поток %s прерван ", w));
                }
            });
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Завершение работы");
        threadPool.shutdown();

        final int w = 500;

        try {
            threadPool.execute(() -> {
                try {
                    System.out.println(String.format("Поток %s стартовал ", w));
                    Thread.sleep(100 + (int) (5000 * Math.random()));
                    System.out.println(String.format("Поток %s окончен ", w));
                } catch (InterruptedException exception) {
                    System.out.println(String.format("Поток %s прерван ", w));
                }
            });
        } catch (IllegalStateException exception) {
            System.out.println(String.format("Поток %s Ошибка: %s", w, exception.getMessage()));
        }

        System.out.println("Ожидаем остановки");
        threadPool.awaitTermination();
        System.out.println("Пул остановлен");
    }
}
