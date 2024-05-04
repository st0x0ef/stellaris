package com.st0x0ef.stellaris.common.blocks.machines.gauge;

import com.st0x0ef.stellaris.common.energy.util.Serializable;
import net.minecraft.network.chat.Component;

public interface IGaugeValue extends Serializable
{
    Component getDisplayName();

    String getUnit();

    long getAmount();

    long getCapacity();

    default int getColor()
    {
        return 0x00000000;
    }

    boolean isReverse();

    default double getDisplayRatio() {
        long capacity = this.getCapacity();

        if (capacity == 0) {
            return 0.0D;
        }

        long amount = this.getAmount();
        return (this.isReverse() ? (capacity - amount) : amount) / (double) capacity;
    }
}
