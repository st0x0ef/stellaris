package com.st0x0ef.stellaris.common.systems.item.base;

import com.st0x0ef.stellaris.common.systems.util.Snapshotable;
import com.st0x0ef.stellaris.common.systems.util.Updatable;
import com.st0x0ef.stellaris.platform.systems.item.base.ItemContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface BotariumItemBlock<T extends ItemContainer & Updatable & Snapshotable<ItemSnapshot>> {

    @Nullable
    T getItemContainer(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction);
}
