package com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen;

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
            for (int x = -16; x < 16; x++) {
                for (int z = -16; z < 16; z++) {
                    for (int y = -16; y < 16; y++) {
                        if (this.level.getBlockEntity(new BlockPos(x + this.getBlockPos().getX(), y + this.getBlockPos().getY(), z + this.getBlockPos().getZ())) instanceof OxygenDistributorBlockEntity source) {
                            container.addOxygenAtFromSource(this.getBlockPos(), false, source.getOxygenContainer());
                        }
                    }
                }
            }
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Oxygen Propagator");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }
}
