package com.st0x0ef.stellaris.common.systems.data;

import com.mojang.serialization.Codec;

public interface DataManagerBuilder<T> {
    DataManagerBuilder<T> serialize(Codec<T> codec);
    DataManagerBuilder<T> copyOnDeath();
    DataManager<T> buildAndRegister(String name);
}
