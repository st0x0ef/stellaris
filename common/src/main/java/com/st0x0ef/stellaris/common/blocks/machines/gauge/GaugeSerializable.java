package com.st0x0ef.stellaris.common.blocks.machines.gauge;

import net.minecraft.nbt.CompoundTag;

public interface GaugeSerializable {
    CompoundTag serialize(CompoundTag nbt);
    void deserialize(CompoundTag nbt);
}
