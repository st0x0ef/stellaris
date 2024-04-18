package com.st0x0ef.stellaris.common.blocks.entities;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.blocks.RadioactiveBlock;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class Vacumator extends BaseEntityBlock {

    //public static final MapCodec<Vacumator> CODEC = RecordCodecBuilder.mapCodec();

    protected Vacumator(Properties properties) {

        super(properties);
    }



    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return Vacumatorentity(pos, state, UseOnClick);
    }

    int UseOnClick = 0;
}