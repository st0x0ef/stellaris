package com.st0x0ef.stellaris.platform.systems.lookup;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public interface BlockLookup<T, C> {

    @Expect
    static <T, C> BlockLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass);

    static <T> BlockLookup<T, @Nullable Direction> create(ResourceLocation name, Class<T> typeClass) {
        return create(name, typeClass, Direction.class);
    }

    /**
     * @return The {@link T} for the block.
     */
    @Nullable
    T find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable C direction);

    @Nullable
    default T find(BlockEntity block, @Nullable C direction) {
        return find(block.getLevel(), block.getBlockPos(), block.getBlockState(), block, direction);
    }

    @Nullable
    default T find(Level level, BlockPos pos, @Nullable C direction) {
        return find(level, pos, null, null, direction);
    }

    default boolean isPresent(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable C direction) {
        return find(level, pos, state, entity, direction) != null;
    }

    default boolean isPresent(BlockEntity block, @Nullable C direction) {
        return find(block, direction) != null;
    }

    default void registerSelf(BlockGetter<T, C> getter, Block ... blocks) {
        onRegister(registrar -> registrar.registerBlocks(getter, blocks));
    }

    default void registerFallback(BlockGetter<T, C> getter, Predicate<Block> blockPredicate) {
        onRegister(registrar -> {
            for (Block block : BuiltInRegistries.BLOCK) {
                if (blockPredicate.test(block)) {
                    registrar.registerBlocks(getter, block);
                }
            }
        });
    }

    default void registerFallback(BlockGetter<T, C> getter) {
        onRegister(registrar -> {
            for (Block block : BuiltInRegistries.BLOCK) {
                registrar.registerBlocks(getter, block);
            }
        });
    }

    default void registerFallback(BlockEntityGetter<T, C> getter, Predicate<BlockEntityType<?>> entityTypePredicate) {
        onRegister(registrar -> {
            for (BlockEntityType<?> entityType : BuiltInRegistries.BLOCK_ENTITY_TYPE) {
                if (entityTypePredicate.test(entityType)) {
                    registrar.registerBlockEntities(getter, entityType);
                }
            }
        });
    }

    default void registerFallback(BlockEntityGetter<T, C> getter) {
        onRegister(registrar -> {
            for (BlockEntityType<?> entityType : BuiltInRegistries.BLOCK_ENTITY_TYPE) {
                registrar.registerBlockEntities(getter, entityType);
            }
        });
    }

    void onRegister(Consumer<BlockRegistrar<T, C>> registrar);

    interface BlockGetter<T, C> {
        @Nullable
        T getContainer(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable C direction);
    }

    interface BlockEntityGetter<T, C> {
        @Nullable
        T getContainer(BlockEntity entity, @Nullable C direction);
    }

    interface BlockRegistrar<T, C> {
        void registerBlocks(BlockGetter<T, C> getter, Block... blocks);

        void registerBlockEntities(BlockEntityGetter<T, C> getter, BlockEntityType<?>... blockEntityTypes);
    }
}
