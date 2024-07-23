package com.st0x0ef.stellaris.platform.systems.core.wrapped;

import com.st0x0ef.stellaris.platform.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.platform.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.platform.systems.resources.Resource;
import com.st0x0ef.stellaris.platform.systems.resources.fluid.FluidResource;
import com.st0x0ef.stellaris.platform.systems.core.storage.ConversionUtils;
import com.st0x0ef.stellaris.platform.systems.core.storage.common.CommonWrappedContainer;
import com.st0x0ef.stellaris.platform.systems.core.storage.context.CommonItemContext;
import com.st0x0ef.stellaris.platform.systems.core.storage.context.FabricItemContext;
import com.st0x0ef.stellaris.platform.systems.core.storage.fabric.FabricWrappedContainer;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class WrappedItemLookup<U extends Resource, V extends TransferVariant<?>> implements ItemLookup<CommonStorage<U>, ItemContext> {
    private final ItemApiLookup<Storage<V>, ContainerItemContext> fabricLookup;

    public WrappedItemLookup(ItemApiLookup<Storage<V>, ContainerItemContext> fabricLookup) {
        this.fabricLookup = fabricLookup;
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<CommonStorage<U>, ItemContext>> registrar) {
        registrar.accept(this::registerItems);
    }

    public void registerItems(ItemGetter<CommonStorage<U>, ItemContext> getter, Item... items) {
        fabricLookup.registerForItems((stack, context) -> {
            CommonStorage<U> container = getter.getContainer(stack, new CommonItemContext(context));
            if (container != null) {
                return wrap(container);
            }
            return null;
        }, items);
    }

    public abstract FabricWrappedContainer<U, V> wrap(CommonStorage<U> container);

    public ItemApiLookup<Storage<V>, ContainerItemContext> getFabricLookup() {
        return fabricLookup;
    }

    public static class OfFluid extends WrappedItemLookup<FluidResource, FluidVariant> {
        public OfFluid() {
            super(FluidStorage.ITEM);
        }

        @Override
        public FabricWrappedContainer<FluidResource, FluidVariant> wrap(CommonStorage<FluidResource> container) {
            return new FabricWrappedContainer.OfFluid(container);
        }

        @Override
        public @Nullable CommonStorage<FluidResource> find(ItemStack stack, ItemContext context) {
            Storage<FluidVariant> storage = getFabricLookup().find(stack, new FabricItemContext(context));
            if (storage != null) {
                if (storage instanceof FabricWrappedContainer.OfFluid container) {
                    return container.container();
                }
                return new CommonWrappedContainer<>(storage, ConversionUtils::toVariant, ConversionUtils::toResource);
            }
            return null;
        }
    }
}
