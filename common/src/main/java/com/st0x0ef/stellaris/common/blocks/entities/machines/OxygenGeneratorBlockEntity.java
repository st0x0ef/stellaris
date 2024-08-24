package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.menus.OxygenDistributorMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class OxygenGeneratorBlockEntity extends BaseEnergyContainerBlockEntity {
    public OxygenGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.OXYGEN_DISTRIBUTOR.get(), pos, state);
    }
    private static final int TANK_CAPACITY = 3;
    private final FluidTank oxygenTank = new FluidTank("ingredientTank", TANK_CAPACITY);

    @Override
    public void tick() {
        if (!FluidTankHelper.addFluidFromBucket(this, oxygenTank, 1, 0)) {
            FluidTankHelper.extractFluidToItem(this, oxygenTank, 1, 0);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.stellaris.oxygen_distributor");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new OxygenDistributorMenu(containerId, inventory, this, this);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    public FluidTank getOxygenTank() {
        return oxygenTank;
    }
}
