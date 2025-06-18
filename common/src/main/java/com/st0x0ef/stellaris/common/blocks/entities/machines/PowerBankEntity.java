package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.blocks.machines.PowerBankBlock;
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
    private int renderStage = -1; // -1 to force update on first tick

    public PowerBankEntity(BlockPos pos, BlockState state) {
        this(pos, state, ((PowerBankBlock)state.getBlock()).tier);
    }

    public PowerBankEntity(BlockPos pos, BlockState state, int tier) {
        super(BlockEntityRegistry.POWER_BANK.get(), pos, state, (int) Math.pow(2,4*tier)*1000, (int) Math.pow(2,4*tier)*1000, (int) Math.pow(2,4*tier)*1000);
    }

    @Override
    public void tick() {
        int initialRenderStage = renderStage;

        //First - Insert slot
        if (!items.getFirst().isEmpty())
            EnergyUtil.moveEnergyFromItem(energyContainer, items.getFirst(), energyContainer.getMaxEnergy() / 40);
        //Last - Extract slot
        if (!items.getLast().isEmpty())
            EnergyUtil.moveEnergyToItem(energyContainer, items.getLast(), energyContainer.getMaxEnergy() / 40);

        EnergyUtil.distributeEnergyNearby(level, worldPosition, energyContainer.getMaxEnergy() / 20);

        //Update render stage
        renderStage = (energyContainer.getEnergy() * 9) / energyContainer.getMaxEnergy();

        if (initialRenderStage != renderStage) {
            BlockState state = getBlockState().setValue(PowerBankBlock.STAGE, renderStage);
            level.setBlock(getBlockPos(), state, 3);
            setChanged();
        }
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("screen.stellaris.power_bank");
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