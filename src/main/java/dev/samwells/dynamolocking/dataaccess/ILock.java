package dev.samwells.dynamolocking.dataaccess;

public interface ILock {
    boolean acquire();
    boolean release();
}
