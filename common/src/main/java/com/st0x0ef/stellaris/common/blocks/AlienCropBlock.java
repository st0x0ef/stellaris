package com.st0x0ef.stellaris.common.blocks;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

public class AlienCropBlock extends CropBlock {

    private final RegistrySupplier<Item> seed;
    private final RegistrySupplier<Block> soil;

    public AlienCropBlock(Properties properties, RegistrySupplier<Item> seed, RegistrySupplier<Block> soil) {
        super(properties);
        this.seed = seed;
        this.soil = soil;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return seed.get();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(soil);
    }
}
