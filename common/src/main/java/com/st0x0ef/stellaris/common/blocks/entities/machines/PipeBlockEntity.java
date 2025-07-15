package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.fej1fun.potentials.fluid.UniversalFluidStorage;
import com.fej1fun.potentials.providers.FluidProvider;
import com.st0x0ef.stellaris.common.blocks.machines.PipeBlock;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.FluidUtil;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.SingleFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PipeBlockEntity extends BlockEntity implements FluidProvider.BLOCK, TickingBlockEntity {

    private final SingleFluidStorage fluidTank;

    public PipeBlockEntity(BlockPos pos, BlockState blockState, long capacity, long maxIn, long maxOut) {
        super(BlockEntityRegistry.PIPE_ENTITY.get(), pos, blockState);
        this.fluidTank = new SingleFluidStorage(capacity, maxIn, maxOut) {
            @Override
            protected void onChange() {
                setChanged();
            }
        };
    }

    public static PipeBlockEntity create(BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof PipeBlock block) {
            return new PipeBlockEntity(pos, state, block.capacity, block.maxIn, block.maxOut);
        }
        return new PipeBlockEntity(pos, state, 0, 0, 0);
    }

    @Override
    public @Nullable UniversalFluidStorage getFluidTank(@Nullable Direction direction) {
        return fluidTank;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        fluidTank.load(tag, registries, "fluid");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        fluidTank.save(tag, registries, "fluid");
    }

    @Override
    public void tick() {
        FluidUtil.distributeFluidNearby(level, worldPosition, fluidTank.getFluidInTank(0));
    }
}
