package dev.samwells.dynamolocking;

import dev.samwells.dynamolocking.dataaccess.ILock;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import org.springframework.stereotype.Service;

@Service
public class LockTesting {
    private final ILock lock;

    public LockTesting(ILock lock) {
        this.lock = lock;
    }

    public void start() {
        System.out.println("Starting Lock Testing...");
        var threadCount = 20;
        try (var executor = Executors.newFixedThreadPool(threadCount)) {
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch doneLatch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executor.execute(() -> {
                    try {
                        var uuid = UUID.randomUUID().toString();

                        startLatch.await();

                        tryAcquireAndRelease(uuid);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        doneLatch.countDown();
                    }
                });
            }

            // All threads start work at the same time
            startLatch.countDown();

            // Wait for all tasks to be completed
            doneLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void tryAcquireAndRelease(final String uuid) {
        var lockAcquired = lock.acquire();
        if (!lockAcquired) {
            System.out.printf("%s failed to acquire lock\n", uuid);
            return;
        }

        System.out.printf("%s acquired lock successfully\n", uuid);

        sleepForAWhile();
        lock.release();
    }

    private void sleepForAWhile() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
