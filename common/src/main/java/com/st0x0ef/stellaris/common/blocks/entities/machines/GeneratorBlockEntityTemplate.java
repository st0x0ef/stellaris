package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.blocks.entities.ImplementedInventory;
import com.st0x0ef.stellaris.common.systems.energy.EnergyApi;
import com.st0x0ef.stellaris.common.systems.energy.base.EnergyBlock;
import com.st0x0ef.stellaris.common.systems.energy.impl.ExtractOnlyEnergyContainer;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class GeneratorBlockEntityTemplate extends BaseEnergyBlockEntity implements ImplementedInventory {

    private WrappedBlockEnergyContainer energyContainer;

    public int getEnergyGeneratedPT() {
        return EnergyGeneratedPT;
    }

    protected int EnergyGeneratedPT;
    private final int MaxCapacity;
    //private List<Integer> inputSlots = List.of(0);

    public GeneratorBlockEntityTemplate(BlockEntityType<?> entityType, BlockPos blockPos, BlockState blockState,
                                        int EnergyGeneratedPT, int MaxCapacity, String tagName, int containerSize) {
        super(entityType, blockPos, blockState, tagName, containerSize);
        this.EnergyGeneratedPT=EnergyGeneratedPT;
        this.MaxCapacity=MaxCapacity;
    }

    @Override
    public final WrappedBlockEnergyContainer getEnergyStorage(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return energyContainer == null ? energyContainer = new WrappedBlockEnergyContainer(entity, new ExtractOnlyEnergyContainer(MaxCapacity, Integer.MAX_VALUE)) : energyContainer;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public void setChanged() {
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 1);
        super.setChanged();
    }


    @Override
    protected Component getDefaultName() {
        return null;
    }

    public abstract boolean canGenerate();

    public void tick() {
        if(canGenerate()) {
            WrappedBlockEnergyContainer energyContainer1 = this.getWrappedEnergyContainer();
            if (energyContainer1.getStoredEnergy() < energyContainer1.getMaxCapacity()) {
                energyContainer1.setEnergy(energyContainer1.getStoredEnergy() + EnergyGeneratedPT);
            } else if (energyContainer1.getStoredEnergy() > energyContainer1.getMaxCapacity()) {
                energyContainer1.setEnergy(energyContainer1.getMaxCapacity());
            }
            System.out.println(energyContainer1.getStoredEnergy());
        }
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this.getBlockPos());
        //EnergyApi.distributeEnergyNearby(blockEntity,100);
    }

}
