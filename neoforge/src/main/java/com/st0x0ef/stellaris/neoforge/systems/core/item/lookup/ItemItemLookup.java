package com.st0x0ef.stellaris.neoforge.systems.core.item.lookup;

import com.st0x0ef.stellaris.common.systems.core.CommonStorageLib;
import com.st0x0ef.stellaris.common.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.common.systems.core.context.impl.ModifyOnlyContext;
import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;
import com.st0x0ef.stellaris.neoforge.systems.core.item.wrappers.CommonItemContainerItem;
import com.st0x0ef.stellaris.neoforge.systems.core.item.wrappers.NeoItemHandler;
import com.st0x0ef.stellaris.neoforge.systems.lookup.RegistryEventListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class ItemItemLookup implements ItemLookup<CommonStorage<ItemResource>, ItemContext>, RegistryEventListener {
    public static final ItemItemLookup INSTANCE = new ItemItemLookup();
    private static final ItemCapability<CommonStorage<ItemResource>, ItemContext> CAPABILITY = ItemCapability.create(new ResourceLocation(CommonStorageLib.MOD_ID, "item_item"), CommonStorage.asClass(), ItemContext.class);

    private final List<Consumer<ItemRegistrar<CommonStorage<ItemResource>, ItemContext>>> registrars = new ArrayList<>();

    private ItemItemLookup() {
        registerSelf();
    }

    @Override
    public @Nullable CommonStorage<ItemResource> find(ItemStack stack, ItemContext context) {
        CommonStorage<ItemResource> capability = stack.getCapability(CAPABILITY, context);
        if (capability != null) {
            return capability;
        }
        IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
        return handler != null ? new CommonItemContainerItem(handler, stack, context) : null;
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<CommonStorage<ItemResource>, ItemContext>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, containers) -> {
            event.registerItem(CAPABILITY, getter::getContainer, containers);

            event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ignored) -> {
                ModifyOnlyContext context = new ModifyOnlyContext(stack);
                CommonStorage<ItemResource> container = getter.getContainer(stack, context);
                return container == null ? null : new NeoItemHandler(container);
            }, containers);
        }));
    }
}
