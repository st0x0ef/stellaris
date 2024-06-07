package com.st0x0ef.stellaris.platform.systems.item.base.neoforge;

import com.st0x0ef.stellaris.neoforge.systems.item.PlatformItemContainer;
import com.st0x0ef.stellaris.platform.systems.item.base.ItemContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;


public interface ItemContainerImpl {

    static ItemContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        IItemHandler capability = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, entity, direction);
        return capability != null ? new PlatformItemContainer(capability) : null;
    }
}
