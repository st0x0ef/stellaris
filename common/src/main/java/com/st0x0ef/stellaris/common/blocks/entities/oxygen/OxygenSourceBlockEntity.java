package com.st0x0ef.stellaris.common.blocks.entities.oxygen;

import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.oxygen.OxygenContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class OxygenSourceBlockEntity extends BlockEntity {
    public OxygenContainer container;
    public int range;

    protected OxygenSourceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int range, int power) {
        super(type, pos, blockState);
        this.range = range;

        container = new OxygenContainer(power);
    }

    public void tick() {
        if (StellarisData.isPlanet(this.level.dimension())) {
            container.tick();
        }
    }
}
