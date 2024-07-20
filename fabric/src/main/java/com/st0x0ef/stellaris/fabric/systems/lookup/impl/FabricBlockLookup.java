package com.st0x0ef.stellaris.fabric.systems.lookup.impl;

import com.st0x0ef.stellaris.common.systems.lookup.BlockLookup;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FabricBlockLookup<T, C> implements BlockLookup<T, C> {
    private final BlockApiLookup<T, C> lookup;

    public FabricBlockLookup(BlockApiLookup<T, C> lookup) {
        this.lookup = lookup;
    }

    public FabricBlockLookup(ResourceLocation id, Class<T> type, Class<C> contextType) {
        this(BlockApiLookup.get(id, type, contextType));
    }

    @Override
    public @Nullable T find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable C direction) {
        return lookup.find(level, pos, state, entity, direction);
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<T, C>> registrar) {
        registrar.accept(new LookupRegistrar());
    }

    @Override
    public void registerSelf(BlockGetter<T, C> getter, Block... blocks) {
        lookup.registerForBlocks(getter::getContainer, blocks);
    }

    public class LookupRegistrar implements BlockRegistrar<T, C> {
        @Override
        public void registerBlocks(BlockGetter<T, C> getter, Block... blocks) {
            lookup.registerForBlocks(getter::getContainer, blocks);
        }

        @Override
        public void registerBlockEntities(BlockEntityGetter<T, C> getter, BlockEntityType<?>... containers) {
            lookup.registerForBlockEntities(getter::getContainer, containers);
        }
    }
}
