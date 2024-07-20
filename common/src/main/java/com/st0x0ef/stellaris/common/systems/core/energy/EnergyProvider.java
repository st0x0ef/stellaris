package com.st0x0ef.stellaris.common.systems.core.energy;

import com.st0x0ef.stellaris.common.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.common.systems.core.storage.base.ValueStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class EnergyProvider {
    private EnergyProvider() {}

    public interface Block {
        ValueStorage getEnergy(Level level, BlockPos pos, @Nullable BlockState state, @Nullable net.minecraft.world.level.block.entity.BlockEntity entity, @Nullable Direction direction);
    }

    public interface BlockEntity {
        ValueStorage getEnergy(@Nullable Direction direction);
    }

    public interface Entity {
        ValueStorage getEnergy(@Nullable Direction direction);
    }

    public interface Item {
        ValueStorage getEnergy(ItemStack stack, ItemContext context);
    }
}
