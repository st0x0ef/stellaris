package com.st0x0ef.stellaris.common.blocks.machines;

import com.fej1fun.potentials.fluid.ItemFluidStorage;
import com.fej1fun.potentials.fluid.UniversalFluidStorage;
import com.fej1fun.potentials.providers.FluidProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTankBlockEntity;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidTankBlock extends BaseMachineBlock {

    public final long capacity;

    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 9);

    public FluidTankBlock(Properties properties, long capacity) {
        super(properties);
        this.capacity = capacity;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(STAGE, 0));
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
        return BlockEntityRegistry.FLUID_TANK.get();
    }

    @Override
    public boolean hasTicker(Level level) {
        return !level.isClientSide();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.getItem() instanceof FluidProvider.ITEM provider) {
            UniversalFluidStorage fluidStorage = provider.getFluidTank(stack);
            BlockEntity be = level.getBlockEntity(pos);
            if (fluidStorage != null) {
                if (be instanceof UniversalFluidStorage fluidBlock) {
                    fluidBlock.fill(fluidBlock.getFluidInTank(0), false);
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STAGE);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);

        if (level instanceof ServerLevel) {
            if (blockEntity instanceof UniversalFluidStorage fluidStorage) {
                ItemStack stack = new ItemStack(this);
                FluidProvider.ITEM provider = (FluidProvider.ITEM) stack.getItem();
                if (provider.getFluidTank(stack) instanceof ItemFluidStorage storage)
                    storage.setFluidInTank(0, fluidStorage.getFluidInTank(0));
                popResource(level, pos, stack);
            }

            state.spawnAfterBreak((ServerLevel)level, pos, tool, false);
        }
    }
}
