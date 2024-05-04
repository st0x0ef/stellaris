package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.blocks.entities.ImplementedInventory;
import com.st0x0ef.stellaris.common.energy.EnergyApi;
import com.st0x0ef.stellaris.common.energy.base.EnergyBlock;
import com.st0x0ef.stellaris.common.energy.impl.ExtractOnlyEnergyContainer;
import com.st0x0ef.stellaris.common.energy.impl.WrappedBlockEnergyContainer;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
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

public class GeneratorBlockEntityTemplate extends BaseContainerBlockEntity implements EnergyBlock<WrappedBlockEnergyContainer>, ImplementedInventory {

    private WrappedBlockEnergyContainer energyContainer = this.getEnergyStorage(level,getBlockPos(),getBlockState(),this,null);

    public int getEnergyGeneratedPT() {
        return EnergyGeneratedPT;
    }

    protected int EnergyGeneratedPT;
    private final int MaxCapacity;
    protected NonNullList<ItemStack> items;
    private List<Integer> inputSlots = List.of(0);

    public GeneratorBlockEntityTemplate(BlockPos blockPos, BlockState blockState) {
        this(EntityRegistry.TEST_BLOCK.get(), blockPos, blockState,1,500);

        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    public GeneratorBlockEntityTemplate(BlockEntityType<?> entityType, BlockPos blockPos, BlockState blockState, int EnergyGeneratedPT,int MaxCapacity) {
        super(EntityRegistry.TEST_BLOCK.get(), blockPos, blockState);
        this.EnergyGeneratedPT=EnergyGeneratedPT;
        this.MaxCapacity=MaxCapacity;
    }
    @Override
    public final WrappedBlockEnergyContainer getEnergyStorage(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return energyContainer == null ? energyContainer = new WrappedBlockEnergyContainer(entity, new ExtractOnlyEnergyContainer(MaxCapacity, Integer.MAX_VALUE)) : energyContainer;
    }
    public final WrappedBlockEnergyContainer getEnergyContainer() {
        return this.getEnergyStorage(this.level,this.worldPosition,this.getBlockState(),this,null);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
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

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }


    public boolean canGenerate(){
        return true;
    }


    public void tick() {
        if(canGenerate()) {
            if (energyContainer.getStoredEnergy() < energyContainer.getMaxCapacity()) {
                energyContainer.setEnergy(energyContainer.getStoredEnergy() + EnergyGeneratedPT);
            } else if (energyContainer.getStoredEnergy() > energyContainer.getMaxCapacity()) {
                energyContainer.setEnergy(energyContainer.getMaxCapacity());
            }
            System.out.println(energyContainer.getStoredEnergy());
        }
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this.getBlockPos());
        EnergyApi.distributeEnergyNearby(blockEntity,100);
    }

}
