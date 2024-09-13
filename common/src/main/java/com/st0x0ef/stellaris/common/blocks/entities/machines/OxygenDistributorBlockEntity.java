package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.menus.OxygenGeneratorMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.utils.OxygenUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class OxygenDistributorBlockEntity extends BaseEnergyContainerBlockEntity {
    public OxygenDistributorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.OXYGEN_DISTRIBUTOR.get(), pos, state);
    }

    @Override
    public void tick() {

    }

    public boolean takeOxygenFromTank() {
        long amountStored = OxygenUtils.getOxygen(getItem(0));
        if (amountStored > 0) {
            OxygenUtils.removeOxygen(getItem(0), 1);
            return true;
        }

        return false;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.stellaris.oxygen_distributor");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new OxygenGeneratorMenu(containerId, inventory, this, this);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }
}
