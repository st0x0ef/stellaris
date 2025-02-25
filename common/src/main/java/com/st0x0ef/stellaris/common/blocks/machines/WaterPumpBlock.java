package com.st0x0ef.stellaris.common.blocks.machines;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class WaterPumpBlock extends BaseMachineBlock {

    public static final MapCodec<WaterPumpBlock> CODEC = simpleCodec(WaterPumpBlock::new);

    VoxelShape SHAPE = Block.box(5, 0, 5, 11, 16, 11);

    public WaterPumpBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntityRegistry.WATER_PUMP.get();
    }

    @Override
    public boolean hasTicker(Level level) {
        return !level.isClientSide;
    }
}
