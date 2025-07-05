package com.st0x0ef.stellaris.common.blocks;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.machines.AntennaBlock;
import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RocketLaunchPad extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty STAGE = BlockStateProperties.LIT;

    public static final VoxelShape SHAPE_HIGH = Shapes.box(0, 0, 0, 1, 0.25, 1);
    public static final VoxelShape SHAPE_NORMAL = Shapes.box(0, 0, 0, 1, 0.187, 1);

    public RocketLaunchPad(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(STAGE, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER);
        return this.defaultBlockState().setValue(WATERLOGGED, flag);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, STAGE);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return !world.getBlockState(pos.below()).is(state.getBlock());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(STAGE)) {
            return SHAPE_HIGH;
        }
        else {
            return SHAPE_NORMAL;
        }
    }

    @Override
    public void onPlace(BlockState p_60566_, Level p_60567_, BlockPos p_60568_, BlockState p_60569_, boolean p_60570_) {
        super.onPlace(p_60566_, p_60567_, p_60568_, p_60569_, p_60570_);
        p_60567_.scheduleTick(p_60568_, this, 1);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        int y = pos.getY();

        /** POS FOR 3x3 */
        int x = pos.getX() - 1;
        int z = pos.getZ() - 1;

        /** POS FOR 5x5 */
        int x2 = pos.getX() - 2;
        int z2 = pos.getZ() - 2;

        /** LISTS */
        List<Boolean> flag1 = new ArrayList<>();
        List<Boolean> flag2 = new ArrayList<>();

        /** CHECK IF LAUNCH PAD 3x3 */
        for (int i1 = x; i1 < x + 3; i1++) {
            for (int f2 = z; f2 < z + 3; f2++) {
                BlockPos pos2 = new BlockPos(i1, y, f2);

                flag1.add(level.getBlockState(pos2).is(BlocksRegistry.ROCKET_LAUNCH_PAD.get()));
            }
        }

        /** CHECK IF LAUNCH PAD 5x5 (STAGE == FALSE) */
        for (int i1 = x2; i1 < x2 + 5; i1++) {
            for (int f2 = z2; f2 < z2 + 5; f2++) {
                BlockPos pos2 = new BlockPos(i1, y, f2);

                if (level.getBlockState(pos2).is(BlocksRegistry.ROCKET_LAUNCH_PAD.get()) && !pos2.equals(pos)) {
                    flag2.add(level.getBlockState(pos2).getValue(STAGE));
                }
            }
        }

        /** VARIABLE SETTER */
        if (!flag1.contains(false)) {
            if (!state.getValue(STAGE) && !flag2.contains(true)) {
                level.setBlock(pos, state.setValue(STAGE, true), 2);
            }
        }
        else {
            if (state.getValue(STAGE)) {
                level.setBlock(pos, state.setValue(STAGE, false), 2);
            }
        }

        level.scheduleTick(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), this, 1);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        if (state.getValue(STAGE)) {
            if (!player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemsRegistry.ROCKET.get()) && level.getBlockState(pos.below()).getBlock() instanceof AntennaBlock antennaBlock) {
                return antennaBlock.useWithoutItem(state, level, pos.below(), player, hitResult);
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;

        if(stack.is(ItemsRegistry.ANTENNA.get()) && state.getValue(STAGE)) {
            if (level.getBlockState(pos.below()).is(TagRegistry.ANTENNA_REPLACEABLES)) {
                level.setBlock(pos.below(), BlocksRegistry.ANTENNA.get().defaultBlockState(), 3);
                stack.shrink(1);
                return ItemInteractionResult.SUCCESS;

            } else if(stack.is(ItemsRegistry.ROCKET.get())) {
                return ItemInteractionResult.FAIL;
            } else {
                player.displayClientMessage(Component.literal("You can't place an antenna block here. The surface under the launchpad can't be repleaced."), false);
                return ItemInteractionResult.FAIL;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }
}
