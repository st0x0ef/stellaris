package com.st0x0ef.stellaris.common.energy.util;

public interface Snapshotable<T> extends Updatable {
    T createSnapshot();
}
