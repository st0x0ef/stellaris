package com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen;

import com.st0x0ef.stellaris.common.blocks.entities.machines.BaseEnergyBlockEntity;
import com.st0x0ef.stellaris.common.oxygen.OxygenContainer;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class OxygenPropagatorBlockEntity extends BaseEnergyBlockEntity implements OxygenContainerBlockEntity {

    private final OxygenContainer oxygenContainer = new OxygenContainer(0);

    public OxygenPropagatorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.OXYGEN_PROPAGATOR.get(), pos, state);
    }

    @Override
    public void tick() {
        if (getWrappedEnergyContainer().getStoredEnergy() > 0) {
            if (PlanetUtil.isPlanet(level.dimension().location())) {
                for (int x = -16; x < 16; x++) {
                    for (int z = -16; z < 16; z++) {
                        for (int y = -16; y < 16; y++) {
                            if (level.getBlockEntity(new BlockPos(x + getBlockPos().getX(), y + getBlockPos().getY(), z + getBlockPos().getZ())) instanceof OxygenPropagatorBlockEntity propagatorBlockEntity) {
                                oxygenContainer.addOxygenAtFromSource(getBlockPos(), false, propagatorBlockEntity.getOxygenContainer());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getMaxCapacity() {
        return 6000;
    }

    @Override
    public OxygenContainer getOxygenContainer() {
        return oxygenContainer;
    }

    @Override
    public int getRange() {
        return 32;
    }
}
