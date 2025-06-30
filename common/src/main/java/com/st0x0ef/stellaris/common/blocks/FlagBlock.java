package com.st0x0ef.stellaris.common.blocks;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.common.blocks.entities.FlagBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.AntennaBlockEntity;
import com.st0x0ef.stellaris.common.blocks.machines.BaseMachineBlock;
import com.st0x0ef.stellaris.common.menus.AntennaMenu;
import com.st0x0ef.stellaris.common.menus.FlagUploadMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import io.netty.buffer.Unpooled;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class FlagBlock extends BaseMachineBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public FlagBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return BaseEntityBlock.simpleCodec(FlagBlock::new);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.box((double) 7 / 16, 0, (double) 7 / 16, (double) 9 / 16, 3, (double) 9 / 16);
    }

    @Nullable
    @Override
    protected ExtendedMenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FlagBlockEntity flagBlockEntity) {
            return new ExtendedMenuProvider() {
                @Override
                public void saveExtraData(FriendlyByteBuf buf) {
                    buf.writeBlockPos(blockEntity.getBlockPos());
                }

                @Override
                public Component getDisplayName() {
                    return flagBlockEntity.getDisplayName();
                }

                @Override
                public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeBlockPos(blockEntity.getBlockPos());

                    return FlagUploadMenu.create(containerId, inventory, buf);
                }
            };
        }
        return null;
    }

    @Override
    public BlockState playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (!worldIn.isClientSide && player.isCreative()) {
        }
        super.playerWillDestroy(worldIn, pos, state, player);
        player.addItem(ItemsRegistry.FLAG_ITEM.get().getDefaultInstance());
        return state;
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();

        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && context.getLevel().getBlockState(blockpos.above()).canBeReplaced(context)) {
            boolean flag = context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER);
            return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(WATERLOGGED, flag);
        }
        else {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {

        if(worldIn.isClientSide()) return;

        boolean flag = worldIn.getFluidState(pos.above()).is(Fluids.WATER);

        BlockEntity tileentity = worldIn.getBlockEntity(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));

        if(tileentity instanceof FlagBlockEntity flagBlockEntity && placer instanceof Player player) {
            flagBlockEntity.setProfile(new ResolvableProfile(player.getGameProfile()));
        }
        super.setPlacedBy(worldIn, pos, state.setValue(WATERLOGGED, false), placer, stack);

    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(stack.getItem() instanceof DyeItem dyeItem) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FlagBlockEntity flagBlockEntity) {
                flagBlockEntity.setDyeColor(dyeItem.getDyeColor());
                level.sendBlockUpdated(pos, state, state, 3);
                return ItemInteractionResult.SUCCESS;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FlagBlockEntity(pos, state);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntityRegistry.FLAG.get();
    }

    @Override
    public boolean hasTicker(Level level) {
        return false;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        state = state.setValue(WATERLOGGED, false);
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING, WATERLOGGED);
    }



}
