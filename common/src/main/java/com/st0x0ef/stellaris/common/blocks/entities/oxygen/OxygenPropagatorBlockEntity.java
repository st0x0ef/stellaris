package com.st0x0ef.stellaris.common.blocks.entities.oxygen;

import com.st0x0ef.stellaris.common.oxygen.OxygenContainer;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class OxygenPropagatorBlockEntity extends BlockEntity {
    public OxygenContainer container;
    public int range;

    protected OxygenPropagatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int range, int power) {
        super(type, pos, blockState);
        this.range = range;

        container = new OxygenContainer(power);
    }

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
}
