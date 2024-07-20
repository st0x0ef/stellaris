package com.st0x0ef.stellaris.fabric.systems.data;

import com.st0x0ef.stellaris.common.systems.data.DataManagerBuilder;
import com.st0x0ef.stellaris.fabric.systems.data.impl.DataManagerBuilderImpl;
import net.msrandom.multiplatform.annotations.Actual;

import java.util.function.Supplier;

@Actual
public final class DataManagerRegistryActual {
    private final String modid;

    @Actual
    public DataManagerRegistryActual(String modid) {
        this.modid = modid;
    }

    @Actual
    public <T> DataManagerBuilder<T> builder(Supplier<T> factory) {
        return new DataManagerBuilderImpl<>(modid, factory);
    }

    @Actual
    public void init() {
    }
}
