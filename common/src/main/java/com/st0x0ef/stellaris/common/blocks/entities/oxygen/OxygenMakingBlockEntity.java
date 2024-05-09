package com.st0x0ef.stellaris.common.blocks.entities.oxygen;

import com.st0x0ef.stellaris.common.oxygen.OxygenContainer;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class OxygenMakingBlockEntity extends BlockEntity {
    public OxygenContainer container;

    protected OxygenMakingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void tick() {
        if (!PlanetUtil.isPlanet(this.level.dimension())) {
            container.addOxygenAt(this.getBlockPos(), false);
        }
    }
}
