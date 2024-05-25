package com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen;

import com.st0x0ef.stellaris.common.oxygen.OxygenContainer;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class OxygenDistributorBlockEntity extends OxygenBlockEntity {
    protected OxygenDistributorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int range, int power) {
        super(type, pos, blockState, new OxygenContainer(power), range);
    }

    @Override
    public void tick() {
        if (PlanetUtil.isPlanet(this.level.dimension())) {
            container.addOxygenAt(this.getBlockPos(), false);
            container.tick(this.level);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Oxygen Source");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null; // TODO : menu
    }

    @Override
    public int getContainerSize() {
        return 0;
    }
}
