package com.st0x0ef.stellaris.common.systems.core.storage.base;

public interface UpdateManager<T> {
    T createSnapshot();

    void readSnapshot(T snapshot);

    void update();

    static void batch(Object... managers) {
        for (Object potential : managers) {
            if (potential instanceof UpdateManager) {
                ((UpdateManager<?>) potential).update();
            }
        }
    }

    static <T> void forceRead(UpdateManager<T> manager, Object o) {
        manager.readSnapshot((T) o);
    }
}
