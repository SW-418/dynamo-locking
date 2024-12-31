package dev.samwells.dynamolocking;

import dev.samwells.dynamolocking.dataaccess.ILock;
import org.springframework.stereotype.Service;

@Service
public class LockTesting {
    private final ILock lock;

    public LockTesting(ILock lock) {
        this.lock = lock;
    }

    public void start() {
        System.out.println("Starting Lock Testing...");
        var lockAcquired = lock.acquire();
        if (lockAcquired) {
            sleepForAWhile();
            lock.release();
        }
    }

    private void sleepForAWhile() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
