package MonteCarloPI;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class MonteCarloPi {

    static final long NUM_POINTS = 50_000_000L;
    static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    static AtomicLong result = new AtomicLong(0);

    public static void main(String[] args) throws InterruptedException, ExecutionException
    {
        // Without Threads
        System.out.println("Single threaded calculation started: ");
        long startTime = System.nanoTime();
        double piWithoutThreads = estimatePiWithoutThreads(NUM_POINTS);
        long endTime = System.nanoTime();
        System.out.println("Monte Carlo Pi Approximation (single thread): " + piWithoutThreads);
        System.out.println("Time taken (single threads): " + (endTime - startTime) / 1_000_000 + " ms");

        // With Threads
        System.out.printf("Multi threaded calculation started: (your device has %d logical threads)\n",NUM_THREADS);
        startTime = System.nanoTime();
        double piWithThreads = estimatePiWithThreads(NUM_POINTS, NUM_THREADS);
        endTime = System.nanoTime();
        System.out.println("Monte Carlo Pi Approximation (Multi-threaded): " + piWithThreads);
        System.out.println("Time taken (Multi-threaded): " + (endTime - startTime) / 1_000_000 + " ms");

        // TODO: After completing the implementation, reflect on the questions in the description of this task in the README file
        //       and include your answers in your report file.
    }

    // Monte Carlo Pi Approximation without threads
    public static double estimatePiWithoutThreads(long numPoints)
    {
        // TODO: Implement this method to calculate Pi using a single thread
        Random rand = new Random();
        long insideCircle = 0;

        for (int i = 0; i < numPoints; i++) {
            double x = 2 * rand.nextDouble() - 1;
            double y = 2 * rand.nextDouble() - 1;
            if ((x * x) + (y * y) <= 1) {
                insideCircle++;
            }
        }
        return 4.0 * insideCircle / numPoints;
    }

    // Monte Carlo Pi Approximation with threads
    public static double estimatePiWithThreads(long numPoints, int numThreads) throws InterruptedException, ExecutionException
    {
        // TODO: Implement this method to calculate Pi using multiple threads

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        long pointsPerThread = NUM_POINTS / numThreads;

        // HINT: You may need to create a variable to *safely* keep track of points that fall inside the circle
        // HINT: Each thread should generate and process a subset of the total points

        // TODO: After submitting all tasks, shut down the executor to prevent new tasks
        // TODO: wait for the executor to be fully terminated
        // TODO: Calculate and return the final estimation of Pi

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                Random random = new Random();
                long inside = 0;

                for (long j = 0; j < pointsPerThread; j++) {
                    double x = 2 * random.nextDouble() - 1;
                    double y = 2 * random.nextDouble() - 1;
                    if ((x * x) + (y * y) <= 1) {
                        inside++;
                    }
                }
                result.addAndGet(inside);
            });
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        long ans = result.get();
        return 4.0 * ans / numPoints;
    }
}