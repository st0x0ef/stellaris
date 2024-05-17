package com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen;

import com.st0x0ef.stellaris.common.menus.OxygenPropagatorMenu;
import com.st0x0ef.stellaris.common.oxygen.OxygenContainer;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class OxygenPropagatorBlockEntity extends OxygenBlockEntity {

    public OxygenPropagatorBlockEntity(BlockPos pos, BlockState blockState) {
        super(EntityRegistry.OXYGEN_PROPAGATOR.get(), pos, blockState, new OxygenContainer(0), 0); // TODO change range and power values
    }

    @Override
    public void tick() {
        if (PlanetUtil.isPlanet(this.level.dimension())) {
            if (container.removeOxygenAt(this.getBlockPos().getX() + 1, this.getBlockPos().getY(), this.getBlockPos().getZ(), false)) {
                container.tick(this.level);
            }
            else if (container.removeOxygenAt(this.getBlockPos().getX() - 1, this.getBlockPos().getY(), this.getBlockPos().getZ(), false)) {
                container.tick(this.level);
            }
            else if (container.removeOxygenAt(this.getBlockPos().getX(), this.getBlockPos().getY() + 1, this.getBlockPos().getZ(), false)) {
                container.tick(this.level);
            }
            else if (container.removeOxygenAt(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ(), false)) {
                container.tick(this.level);
            }
            else if (container.removeOxygenAt(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() + 1, false)) {
                container.tick(this.level);
            }
            else if (container.removeOxygenAt(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() - 1, false)) {
                container.tick(this.level);
            }
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Oxygen Propagator");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new OxygenPropagatorMenu(containerId, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return 0;
    }
}
