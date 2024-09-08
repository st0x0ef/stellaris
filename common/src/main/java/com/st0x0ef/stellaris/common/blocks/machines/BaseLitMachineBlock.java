package com.st0x0ef.stellaris.common.blocks.machines;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class BaseLitMachineBlock extends BaseMachineBlock {

    public static final BooleanProperty LIT = AbstractFurnaceBlock.LIT;

    public BaseLitMachineBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }

    public boolean isLit(BlockState state) {
        return state.getValue(LIT);
    }
}
