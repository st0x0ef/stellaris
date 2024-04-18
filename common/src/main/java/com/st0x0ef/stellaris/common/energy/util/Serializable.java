package com.st0x0ef.stellaris.common.energy.util;

import net.minecraft.nbt.CompoundTag;

public interface Serializable {

    /**
     * Deserializes the information of an object from a {@link CompoundTag}.
     *
     * @param nbt The {@link CompoundTag} to deserialize from.
     */
    void deserialize(CompoundTag nbt);

    /**
     * Serializes the information of an object to a {@link CompoundTag}.
     *
     * @param nbt The {@link CompoundTag} to serialize to.
     * @return The {@link CompoundTag} that was passed in but with the information of the object serialized added to it.
     */
    CompoundTag serialize(CompoundTag nbt);
}
