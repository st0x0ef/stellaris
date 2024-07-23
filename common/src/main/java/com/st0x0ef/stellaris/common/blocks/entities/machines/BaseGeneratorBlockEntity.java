package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.platform.systems.core.energy.EnergyUtils;
import com.st0x0ef.stellaris.platform.systems.core.energy.impl.SimpleValueStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseGeneratorBlockEntity extends BaseEnergyContainerBlockEntity {

    protected int energyGeneratedPT;

    public BaseGeneratorBlockEntity(BlockEntityType<?> entityType, BlockPos blockPos, BlockState blockState, int energyGeneratedPT) {
        super(entityType, blockPos, blockState);
        this.energyGeneratedPT = energyGeneratedPT;
    }

    public int getEnergyGeneratedPT() {
        return energyGeneratedPT;
    }

    @Override
    public void setChanged() {
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 1);
        super.setChanged();
    }

    public abstract boolean canGenerate();

    @Override
    public void tick() {
        if (canGenerate()) {
            SimpleValueStorage energy = getEnergy(null);
            if (energy.getStoredAmount() < energy.getCapacity()) {
                energy.insert(energyGeneratedPT,false);
            }
            else if (energy.getStoredAmount() > energy.getCapacity()) {
                energy.set(energy.getCapacity());
            }
        }
        EnergyUtils.distributeEnergyNearby(this,500);
    }
}
