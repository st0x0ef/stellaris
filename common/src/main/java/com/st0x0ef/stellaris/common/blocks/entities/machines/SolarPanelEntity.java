package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SolarPanelEntity extends GeneratorBlockEntityTemplate {
    public SolarPanelEntity(BlockPos blockPos, BlockState blockState) {
        this(EntityRegistry.SOLAR_PANEL.get(), blockPos, blockState);
    }

    public SolarPanelEntity(BlockEntityType<?> entityType, BlockPos blockPos, BlockState blockState) {
        super(entityType, blockPos, blockState);
    }

    @Override
    protected void setEnergyGeneratedPT(int energyGeneratedPT) {
        super.setEnergyGeneratedPT(10);
    }
}