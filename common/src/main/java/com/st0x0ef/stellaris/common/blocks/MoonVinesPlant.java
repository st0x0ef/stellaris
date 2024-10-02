package com.st0x0ef.stellaris.common.blocks;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class MoonVinesPlant extends GrowingPlantBodyBlock implements BonemealableBlock, CaveVines {
    public static final MapCodec<CaveVinesPlantBlock> CODEC = simpleCodec(CaveVinesPlantBlock::new);

    public MapCodec<CaveVinesPlantBlock> codec() {
        return CODEC;
    }

    public MoonVinesPlant(BlockBehaviour.Properties properties) {
        super(properties, Direction.DOWN, SHAPE, false);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(BERRIES, false));
    }

    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock)Blocks.CAVE_VINES;
    }

    protected BlockState updateHeadAfterConvertedFromBody(BlockState head, BlockState body) {
        return (BlockState)body.setValue(BERRIES, (Boolean)head.getValue(BERRIES));
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(Items.GLOW_BERRIES);
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return MoonVine.use(player, state, level, pos);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{BERRIES});
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !(Boolean)state.getValue(BERRIES);
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, (BlockState)state.setValue(BERRIES, true), 2);
    }


}
