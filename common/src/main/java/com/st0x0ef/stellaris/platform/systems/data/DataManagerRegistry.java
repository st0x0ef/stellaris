package com.st0x0ef.stellaris.platform.systems.data;

import net.msrandom.multiplatform.annotations.Expect;

import java.util.function.Supplier;

@SuppressWarnings("ALL")
@Expect
public final class DataManagerRegistry {
    public DataManagerRegistry(String modid);

    public <T> DataManagerBuilder<T> builder(Supplier<T> factory);

    public void init();
}
