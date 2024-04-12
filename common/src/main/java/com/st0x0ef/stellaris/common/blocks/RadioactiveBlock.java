package com.st0x0ef.stellaris.common.blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.blocks.entities.RadioactiveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RadioactiveBlock extends BaseEntityBlock {
    public static final MapCodec<RadioactiveBlock> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    propertiesCodec(),
                    Codec.INT.fieldOf("level").forGetter(RadioactiveBlock::getRadioactivityLevel)
            ).apply(i, RadioactiveBlock::new)
    );

    private final int radioactivityLevel;

    public RadioactiveBlock(Properties properties, int level) {
        super(properties);
        this.radioactivityLevel = level;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RadioactiveBlockEntity(pos, state, this.radioactivityLevel);
    }

    @Override
    public <T2 extends BlockEntity> BlockEntityTicker<T2> getTicker(Level level, BlockState state, BlockEntityType<T2> type) {
        return (l, p, s, e) -> {
            if (e instanceof RadioactiveBlockEntity entity) {
                entity.tick();
            }
        };
    }

    public int getRadioactivityLevel() {
        return radioactivityLevel;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

}