package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.menus.SolarPanelMenu;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class SolarPanelEntity extends GeneratorBlockEntityTemplate {
    public SolarPanelEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.SOLAR_PANEL.get(), blockPos, blockState,1,500);
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new SolarPanelMenu(i, inventory,this,this);
    }

}