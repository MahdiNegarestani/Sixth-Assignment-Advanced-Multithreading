### 1. `Atomic Variables`

```java
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
  
```

**Questions:**

- What output do you get from the program? Why?

  The output will be:
  Atomic Counter: 2000000
  Normal Counter: some unpredictable value (1000000 < Normal Counter < 2000000)

  The `Atomic Counter` will always be `2000000` because `atomicCounter.incrementAndGet()` is thread-safe and guarantees that all increments are counted correctly across both threads. However, the `Normal Counter` will likely not be `2000000` due to race conditions. Since `normalCounter++` is not synchronized, multiple threads may read and write to it simultaneously, leading to lost updates.
- What is the purpose of AtomicInteger in this code?

  1 AtomicInteger doesn't need Locks
  2 AtomicInteger is Thread-safe
  3 AtomicInteger is faster than normal counter
  4 AtomicInteger is used for atomic operations
  5 To compare handling multi-threads between int and AtomicInteger

  - What thread-safety guarantees does atomicCounter.incrementAndGet() provide?

    The `incrementAndGet()` method of `AtomicInteger`  provides atomicity, meaning that the operation is completed in a single  step without being interrupted by other threads. It guarantees that if  multiple threads call this method concurrently, each call will be  executed in a way that the final value reflects all increments made by  all threads, without any lost updates.

    - In which situations would using a lock be a better choice than an atomic variable?

      When we need to work with multiple variables or complex operations.

      - Besides AtomicInteger, what other data types are available in the java.util.concurrent.atomic package?

        * AtomicIBoolean
        * AtomicIntegerArray
        * AtomicLong
        * Atomic
        * Others...

---


**Monte Carlo Questions**


No. The multi-thread version is slower than the single-thread version

because the actions are simple, and creating threads would result in more wasted time

Multi-thread would be better if we had more complicated tasks to assign to them
