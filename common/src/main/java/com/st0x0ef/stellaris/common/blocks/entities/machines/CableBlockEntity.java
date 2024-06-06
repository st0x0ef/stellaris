package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.systems.energy.EnergyApi;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CableBlockEntity extends BaseEnergyBlockEntity {

    public CableBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.CABLE_ENTITY.get(), blockPos, blockState,"stellaris.energy.cable",0);
    }

    @Override
    public void tick() {
        if (!this.getLevel().isClientSide) {
            BlockEntity blockEntity = this.getLevel().getBlockEntity(this.getBlockPos());
            //EnergyApi.distributeEnergyNearby(blockEntity,100);
        }
    }


    @Override
    public WrappedBlockEnergyContainer getWrappedEnergyContainer() {
        return this.getEnergyStorage(this.getLevel(),this.getBlockPos(),this.getBlockState(),this,null);
    }

    @Override
    protected Component getDefaultName() {
        return null;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return null;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {

    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }
}
