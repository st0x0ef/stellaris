package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FuelRefineryBlockEntity extends BaseEnergyContainerBlockEntity {

    public FluidTank inputFluidTank = new FluidTank("inputTank",5000);
    public FluidTank outputFluidTank = new FluidTank("outputTank",5000);


    public FuelRefineryBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FUEL_REFINERY.get(), pos, state);
    }

    @Override
    public void tick() {
        this.getItems();
        inputFluidTank.getAmount();

    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.stellaris.fuel_refinery");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    protected int getMaxCapacity() {
        return 128000;
    }
}
