package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.blocks.machines.CableBlock;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.utils.capabilities.energy.EnergyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CableBlockEntity extends BaseEnergyBlockEntity {

    public CableBlockEntity(BlockPos blockPos, BlockState blockState, int capacity, int maxIn, int maxOut) {
        super(BlockEntityRegistry.CABLE_ENTITY.get(), blockPos, blockState, capacity, maxIn, maxOut);
    }

    public static CableBlockEntity create(BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof CableBlock block) {
            return new CableBlockEntity(pos, state, block.capacity, block.maxIn, block.maxOut);
        }
        return new CableBlockEntity(pos, state, 0, 0, 0);
    }

    @Override
    public void tick() {
        EnergyUtil.distributeEnergyNearby(level, worldPosition, energyContainer.getEnergy() / 8);
    }
}
