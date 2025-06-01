package com.st0x0ef.stellaris.common.blocks.machines;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTankBlockEntity;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidTankBlock extends BaseMachineBlock {

    public final long capacity;

    public FluidTankBlock(Properties properties, long capacity) {
        super(properties);
        this.capacity = capacity;
    }

    @Override
    protected @NotNull MapCodec<? extends FluidTankBlock> codec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                propertiesCodec(),
                Codec.LONG.fieldOf("capacity").forGetter(tank -> tank.capacity)
        ).apply(instance, FluidTankBlock::new));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FluidTankBlockEntity(pos, state, capacity);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntityRegistry.TANK.get();
    }

    @Override
    public boolean hasTicker(Level level) {
        return true;
    }
}
