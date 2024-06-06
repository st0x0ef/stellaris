package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.menus.SolarPanelMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SolarPanelEntity extends GeneratorBlockEntityTemplate {

    public SolarPanelEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.SOLAR_PANEL.get(), blockPos, blockState,1,500,"stellaris.energy.solar_panel",0);
    }

    @Override
    public WrappedBlockEnergyContainer getWrappedEnergyContainer() {
        return this.getEnergyStorage(this.getLevel(),this.getBlockPos(),this.getBlockState(),this,null);
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new SolarPanelMenu(i, inventory,this, this);
    }

    @Override
    public boolean canGenerate() {
        Level level = this.getLevel();
        BlockPos blockPos = this.getBlockPos().offset(0, 1, 0);
        return level.isDay() && level.canSeeSky(blockPos);
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        //this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items, provider);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        ContainerHelper.saveAllItems(compoundTag, this.items, provider);
    }

    @Override
    public int getContainerSize() {
        return 0;
    }
}