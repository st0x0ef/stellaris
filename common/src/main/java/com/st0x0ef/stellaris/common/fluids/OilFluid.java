package com.st0x0ef.stellaris.common.fluids;

import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;


public class OilFluid extends FlowingFluid {
    @Override
    public Fluid getFlowing() {
        return FluidRegistry.FLOWING_OIL.get();
    }

    @Override
    public Fluid getSource() {
        return FluidRegistry.OIL_STILL.get();
    }

    @Override
    public Item getBucket() {
        return ItemsRegistry.OIL_BUCKET.get();
    }

    @Override
    protected void animateTick(Level level, BlockPos blockPos, FluidState state, RandomSource randomSource) {
        super.animateTick(level, blockPos, state, randomSource);
        if (!state.isSource() && !state.getValue(FALLING)) {
            if (randomSource.nextInt(64) == 0) {
                level.playLocalSound((double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 0.5D, (double)blockPos.getZ() + 0.5D, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, randomSource.nextFloat() * 0.25F + 0.75F, randomSource.nextFloat() + 0.5F, false);
            }
        } else if (randomSource.nextInt(10) == 0) {
            level.addParticle(ParticleTypes.UNDERWATER, (double)blockPos.getX() + randomSource.nextDouble(), (double)blockPos.getY() + randomSource.nextDouble(), (double)blockPos.getZ() + randomSource.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Nullable
    @Override
    protected ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_WATER;
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor worldIn, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? worldIn.getBlockEntity(pos) : null;
        Block.dropResources(state, worldIn, pos, blockEntity);
    }

    @Override
    protected int getSlopeFindDistance(LevelReader p_76074_) {
        return 2;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState p_76136_) {
        return BlocksRegistry.OIL_BLOCK.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(p_76136_));
    }

    @Override
    public boolean isSource(FluidState state) {
        return false;
    }

    @Override
    public int getAmount(FluidState p_164509_) {
        return 4;
    }

    @Override
    public boolean isSame(Fluid fluidIn) {
        return fluidIn == FluidRegistry.OIL_STILL.get() || fluidIn == FluidRegistry.FLOWING_OIL.get();
    }

    @Override
    protected int getDropOff(LevelReader p_76087_) {
        return 2;
    }
    @Override
    public int getTickDelay(LevelReader p_76120_) {
        return 8;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState p_76127_, BlockGetter p_76128_, BlockPos p_76129_, Fluid p_76130_, Direction p_76131_) {
        return false;
    }

    @Override
    protected boolean canSpreadTo(BlockGetter p_75978_, BlockPos p_75979_, BlockState p_75980_, Direction p_75981_, BlockPos p_75982_, BlockState p_75983_, FluidState p_75984_, Fluid p_75985_) {
        return super.canSpreadTo(p_75978_, p_75979_, p_75980_, p_75981_, p_75982_, p_75983_, p_75984_, p_75985_) && !p_75978_.getFluidState(p_75979_.below()).is(FluidTags.WATER);
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    public static class Flowing extends OilFluid {
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> p_76046_) {
            super.createFluidStateDefinition(p_76046_);
            p_76046_.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState p_164509_) {
            return p_164509_.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends OilFluid {
        @Override
        public int getAmount(FluidState p_164509_) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }
}