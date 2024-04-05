package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.energy.base.EnergyBlock;
import com.st0x0ef.stellaris.common.energy.impl.SimpleEnergyContainer;
import com.st0x0ef.stellaris.common.energy.impl.WrappedBlockEnergyContainer;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeneratorBlockEntityTemplate extends BlockEntity implements EnergyBlock<WrappedBlockEnergyContainer> {
    private WrappedBlockEnergyContainer energyContainer;
    private WrappedBlockEnergyContainer energyStorage = this.getEnergyStorage(level,getBlockPos(),getBlockState(),this,null);
    private int EnergyGeneratedPT = 1;

    public GeneratorBlockEntityTemplate(BlockPos blockPos, BlockState blockState) {
        this(EntityRegistry.TEST_BLOCK.get(), blockPos, blockState);
    }

    public GeneratorBlockEntityTemplate(BlockEntityType<?> entityType, BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.TEST_BLOCK.get(), blockPos, blockState);
    }

    @Override
    public final WrappedBlockEnergyContainer getEnergyStorage(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return energyContainer == null ? energyContainer = new WrappedBlockEnergyContainer(entity, new SimpleEnergyContainer(15000, Integer.MAX_VALUE)) : energyContainer;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
    }

    public void tick() {
        if (energyStorage.getStoredEnergy() < energyStorage.getMaxCapacity()){
            energyStorage.setEnergy(energyStorage.getStoredEnergy()+EnergyGeneratedPT);
        } else if (energyStorage.getStoredEnergy() > energyStorage.getMaxCapacity()) {
            energyStorage.setEnergy(energyStorage.getMaxCapacity());
        }
    }
}
