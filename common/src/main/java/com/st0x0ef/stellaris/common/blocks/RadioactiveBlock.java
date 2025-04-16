package com.st0x0ef.stellaris.common.blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.blocks.machines.BaseTickingEntityBlock;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RadioactiveBlock extends BaseTickingEntityBlock {

    public static final MapCodec<RadioactiveBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            propertiesCodec(),
            Codec.INT.fieldOf("level").forGetter(RadioactiveBlock::getRadioactivityLevel)
    ).apply(instance, RadioactiveBlock::new));

    private final int radioactivityLevel;

    public RadioactiveBlock(Properties properties, int level) {
        super(properties);
        radioactivityLevel = level;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntityRegistry.RADIOACTIVE_BLOCK.get();
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean hasTicker(Level level) {
        return !level.isClientSide;
    }

    public int getRadioactivityLevel() {
        return radioactivityLevel;
    }
}