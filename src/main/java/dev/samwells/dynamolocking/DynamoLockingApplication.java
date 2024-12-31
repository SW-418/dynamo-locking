package dev.samwells.dynamolocking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DynamoLockingApplication {
    private final LockTesting lockTesting;

    public DynamoLockingApplication(LockTesting lockTesting) {
        this.lockTesting = lockTesting;
    }

    public static void main(String[] args) {
        var context = SpringApplication.run(DynamoLockingApplication.class, args);
        DynamoLockingApplication application = context.getBean(DynamoLockingApplication.class);
        application.lockTesting.start();
    }
}
