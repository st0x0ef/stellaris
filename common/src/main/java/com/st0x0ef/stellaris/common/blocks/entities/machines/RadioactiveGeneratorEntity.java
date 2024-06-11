package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.items.RadiationItem;
import com.st0x0ef.stellaris.common.menus.RadioactiveGeneratorMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class RadioactiveGeneratorEntity extends CoalGeneratorEntity {

    public RadioactiveGeneratorEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.RADIOACTIVE_GENERATOR.get(), blockPos, blockState, 500, 1000000);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new RadioactiveGeneratorMenu(containerId, inventory, this, this);
    }

    @Override
    protected int getBurnDuration(ItemStack fuelStack) {
        if (fuelStack.isEmpty()) {
            return 0;
        }

        if (fuelStack.getItem() instanceof RadiationItem radiationItem) {
            return switch (radiationItem.getRadiationLevel()) {
                case 0 -> 200;
                case 1 -> 500;
                case 2 -> 1000;
                default -> 0;
            };
        }

        return 0;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.stellaris.radioactive_generator");
    }

    @Override
    protected int getMaxCapacity() {
        return 1000000;
    }
}
