package com.st0x0ef.stellaris.fabric.systems.core.wrapped;

import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.common.systems.resources.Resource;
import com.st0x0ef.stellaris.common.systems.resources.fluid.FluidResource;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;
import com.st0x0ef.stellaris.fabric.systems.core.storage.ConversionUtils;
import com.st0x0ef.stellaris.fabric.systems.core.storage.common.CommonWrappedContainer;
import com.st0x0ef.stellaris.fabric.systems.core.storage.fabric.FabricWrappedContainer;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class WrappedBlockLookup<U extends Resource, V extends TransferVariant<?>> implements BlockLookup<CommonStorage<U>, @Nullable Direction> {
    private final BlockApiLookup<Storage<V>, Direction> fabricLookup;

    public WrappedBlockLookup(BlockApiLookup<Storage<V>, Direction> fabricLookup) {
        this.fabricLookup = fabricLookup;
    }

    @Override
    public void onRegister(Consumer<BlockLookup.BlockRegistrar<CommonStorage<U>, @Nullable Direction>> registrar) {
        registrar.accept(new LookupRegistrar());
    }

    public class LookupRegistrar implements BlockRegistrar<CommonStorage<U>, @Nullable Direction> {
        @Override
        public void registerBlocks(BlockGetter<CommonStorage<U>, @Nullable Direction> getter, Block... blocks) {
            fabricLookup.registerForBlocks((world, pos, state, blockEntity, context) -> {
                CommonStorage<U> container = getter.getContainer(world, pos, state, blockEntity, context);
                if (container != null) {
                    return wrap(container);
                }
                return null;
            }, blocks);
        }

        @Override
        public void registerBlockEntities(BlockEntityGetter<CommonStorage<U>, @Nullable Direction> getter, BlockEntityType<?>... blockEntities) {
            fabricLookup.registerForBlockEntities((entity, context) -> {
                CommonStorage<U> container = getter.getContainer(entity, context);
                if (container != null) {
                    return wrap(container);
                }
                return null;
            }, blockEntities);
        }
    }

    public abstract FabricWrappedContainer<U, V> wrap(CommonStorage<U> container);

    public static class ofFluid extends WrappedBlockLookup<FluidResource, FluidVariant> {
        public ofFluid() {
            super(FluidStorage.SIDED);
        }

        @Override
        public @Nullable CommonStorage<FluidResource> find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
            Storage<FluidVariant> storage = FluidStorage.SIDED.find(level, pos, state, entity, direction);
            if (storage != null) {
                if (storage instanceof FabricWrappedContainer.OfFluid wrappedContainer) {
                    return wrappedContainer.container();
                }
                return new CommonWrappedContainer<>(storage, ConversionUtils::toVariant, ConversionUtils::toResource);
            }
            return null;
        }

        @Override
        public FabricWrappedContainer<FluidResource, FluidVariant> wrap(CommonStorage<FluidResource> container) {
            return new FabricWrappedContainer.OfFluid(container);
        }
    }

    public static class ofItem extends WrappedBlockLookup<ItemResource, ItemVariant> {
        public ofItem() {
            super(ItemStorage.SIDED);
        }

        @Override
        public @Nullable CommonStorage<ItemResource> find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
            Storage<ItemVariant> storage = ItemStorage.SIDED.find(level, pos, state, entity, direction);
            if (storage != null) {
                if (storage instanceof FabricWrappedContainer.OfItem wrappedContainer) {
                    return wrappedContainer.container();
                }
                return new CommonWrappedContainer<>(storage, ConversionUtils::toVariant, ConversionUtils::toResource);
            }
            return null;
        }

        @Override
        public FabricWrappedContainer<ItemResource, ItemVariant> wrap(CommonStorage<ItemResource> container) {
            return new FabricWrappedContainer.OfItem(container);
        }
    }
}
