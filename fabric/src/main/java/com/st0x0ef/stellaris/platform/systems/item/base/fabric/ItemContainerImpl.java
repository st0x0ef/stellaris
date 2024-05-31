package com.st0x0ef.stellaris.platform.systems.item.base.fabric;

import com.st0x0ef.stellaris.fabric.systems.item.PlatformItemContainer;
import com.st0x0ef.stellaris.platform.systems.item.base.ItemContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface ItemContainerImpl {

    static ItemContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return PlatformItemContainer.of(level, pos, state, entity, direction);
    }
}
