package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.menus.OxygenGeneratorMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class OxygenGeneratorBlockEntity extends BaseEnergyContainerBlockEntity {
    public OxygenGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.OXYGEN_DISTRIBUTOR.get(), pos, state);
    }

    @Override
    public void tick() {

    }

    public boolean takeOxygenFromTank(long amount) {
        long amountStored = getItem(0).get(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get()).longValue();
        if (amountStored >= amount) {
            getItem(0).set(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get(), amountStored - amount);
            return true;
        } else if (amountStored > 0) {
            getItem(0).set(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get(), 0L);
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
