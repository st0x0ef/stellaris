package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.menus.PowerBankMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.utils.capabilities.energy.EnergyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PowerBankEntity extends BaseEnergyContainerBlockEntity {

    public PowerBankEntity(BlockPos pos, BlockState state) {
        this(pos, state, 1);
    }
    public PowerBankEntity(BlockPos pos, BlockState state, int tier) {
        super(BlockEntityRegistry.POWER_BANK.get(), pos, state, (int) Math.pow(2,4*tier)*1000, (int) Math.pow(2,4*tier)*1000, (int) Math.pow(2,4*tier)*1000);
    }

    @Override
    public void tick() {
        //First - Insert slot
        if (!items.getFirst().isEmpty())
            EnergyUtil.moveEnergyFromItem(energyContainer, items.getFirst(), energyContainer.getMaxEnergy() / 8);
        //Last - Extract slot
        if (!items.getLast().isEmpty())
            EnergyUtil.moveEnergyToItem(energyContainer, items.getLast(), energyContainer.getMaxEnergy() / 8);

        EnergyUtil.distributeEnergyNearby(level, worldPosition, energyContainer.getMaxEnergy() / 4);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("block.stellaris.power_bank");
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new PowerBankMenu(containerId, inventory, this, this);
    }
}