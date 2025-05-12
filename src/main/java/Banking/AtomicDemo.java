package Banking;

import java.util.concurrent.atomic.AtomicInteger;
public class AtomicDemo {
    private static AtomicInteger atomicCounter = new AtomicInteger(0);
    private static int normalCounter = 0;
    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 1_000_000; i++) {
                atomicCounter.incrementAndGet();
                normalCounter++;
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Atomic Counter: " + atomicCounter);
        System.out.println("Normal Counter: " + normalCounter);
    }
}
