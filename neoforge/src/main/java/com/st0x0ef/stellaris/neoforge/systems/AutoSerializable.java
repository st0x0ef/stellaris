package com.st0x0ef.stellaris.neoforge.systems;

import com.st0x0ef.stellaris.common.systems.util.Serializable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public interface AutoSerializable extends INBTSerializable<CompoundTag> {
    Serializable getSerializable();

    @Override
    default @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider arg) {
        return getSerializable().serialize(new CompoundTag());
    }

    @Override
    default void deserializeNBT(HolderLookup.Provider arg, CompoundTag arg2) {
        getSerializable().deserialize(arg2);
    }
}
