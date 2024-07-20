package com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen;

import com.st0x0ef.stellaris.common.blocks.entities.machines.BaseEnergyBlockEntity;
import com.st0x0ef.stellaris.common.oxygen.OxygenContainer;
import com.st0x0ef.stellaris.common.oxygen.OxygenManager;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class OxygenPropagatorBlockEntity extends BaseEnergyBlockEntity implements OxygenContainerBlockEntity {

    private final OxygenContainer oxygenContainer = new OxygenContainer(0);

    public OxygenPropagatorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.OXYGEN_PROPAGATOR.get(), pos, state);
    }

    @Override
    public void tick() {
        OxygenManager.addOxygenBlocksPerLevel(this.level, this);
    }

    @Override
    public int getMaxEnergyCapacity() {
        return 18000;
    }

    @Override
    public OxygenContainer getOxygenContainer() {
        return oxygenContainer;
    }

    @Override
    public BlockPos getBlockPosition() {
        return this.getBlockPos();
    }
}
