package com.st0x0ef.stellaris.fabric.systems.generic;

import com.st0x0ef.stellaris.common.systems.generic.base.BlockContainerLookup;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.impl.lookup.block.BlockApiLookupImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class FabricBlockContainerLookup<T, C> implements BlockContainerLookup<T, C> {
    private final BlockApiLookup<T, C> lookupMap;

    public FabricBlockContainerLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        lookupMap = BlockApiLookupImpl.get(name, typeClass, contextClass);
    }

    @Override
    public @Nullable T find(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable C direction) {
        return lookupMap.find(level, pos, state, entity, direction);
    }

    @Override
    @SafeVarargs
    public final void registerBlocks(BlockGetter<T, C> getter, Supplier<Block>... containers) {
        for (Supplier<Block> container : containers) {
            lookupMap.registerForBlocks(getter::getContainer, container.get());
        }
    }

    @Override
    @SafeVarargs
    public final void registerBlockEntities(BlockGetter<T, C> getter, Supplier<BlockEntityType<?>>... containers) {
        for (Supplier<BlockEntityType<?>> container : containers) {
            lookupMap.registerForBlockEntities((blockEntity, context) -> getter.getContainer(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, context), container.get());
        }
    }

    public BlockApiLookup<T, C> getLookupMap() {
        return lookupMap;
    }
}
