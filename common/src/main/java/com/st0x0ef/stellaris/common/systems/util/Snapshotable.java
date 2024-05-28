package com.st0x0ef.stellaris.common.systems.util;

public interface Snapshotable<T> extends Updatable {
    T createSnapshot();
}
