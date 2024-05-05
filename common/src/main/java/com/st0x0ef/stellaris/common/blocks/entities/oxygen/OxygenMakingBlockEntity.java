package com.st0x0ef.stellaris.common.blocks.entities.oxygen;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.oxygen.OxygenContainer;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class OxygenMakingBlockEntity extends BlockEntity {
    public OxygenContainer container;

    protected OxygenMakingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void tick() {
        if (!StellarisData.isPlanet(this.level.dimension())) {
            container.addOxygenAt(this.getBlockPos(), false);
        }
    }
}
