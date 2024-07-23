package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.platform.systems.core.energy.EnergyProvider;
import com.st0x0ef.stellaris.platform.systems.core.energy.impl.SimpleValueStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.st0x0ef.stellaris.common.blocks.entities.machines.BaseEnergyContainerBlockEntity.ENERGY_TAG;

public abstract class BaseEnergyBlockEntity extends BlockEntity implements EnergyProvider.BlockEntity, TickingBlockEntity {

    private final SimpleValueStorage energy = new SimpleValueStorage(this, Stellaris.VALUE_CONTENT, getMaxEnergyCapacity());

    public BaseEnergyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract int getMaxEnergyCapacity();

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        energy.set(tag.getLong(ENERGY_TAG));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putLong(ENERGY_TAG, energy.getStoredAmount());
        energy.createSnapshot();
    }

    @Override
    public SimpleValueStorage getEnergy(@Nullable Direction direction) {
        return energy;
    }

}