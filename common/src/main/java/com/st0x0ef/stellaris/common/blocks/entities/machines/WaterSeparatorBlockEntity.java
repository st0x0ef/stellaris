package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.blocks.entities.ImplementedInventory;
import com.st0x0ef.stellaris.common.menus.WaterSeparatorMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WaterSeparatorBlockEntity extends BaseEnergyContainerBlockEntity implements ImplementedInventory {

    public WaterSeparatorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.WATER_SEPARATOR_ENTITY.get(), pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.stellaris.water_separator");
    }

    @Override
    public int getContainerSize() {
        return 6;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new WaterSeparatorMenu(containerId, inventory, this);
    }

    @Override
    public void tick() {
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, WaterSeparatorBlockEntity blockEntity) {
    }
}
