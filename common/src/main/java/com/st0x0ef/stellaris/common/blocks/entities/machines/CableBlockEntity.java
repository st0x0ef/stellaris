package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.energy.EnergyApi;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CableBlockEntity extends BaseEnergyBlockEntity {

    public CableBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.CABLE_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public void tick() {
        if (!this.getLevel().isClientSide) {
            BlockEntity blockEntity = this.getLevel().getBlockEntity(this.getBlockPos());
            EnergyApi.distributeEnergyNearby(blockEntity,100);
        }
    }
}
