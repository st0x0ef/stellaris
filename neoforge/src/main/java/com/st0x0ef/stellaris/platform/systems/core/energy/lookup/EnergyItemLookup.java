package com.st0x0ef.stellaris.platform.systems.core.energy.lookup;

import com.st0x0ef.stellaris.platform.systems.core.CommonStorageLib;
import com.st0x0ef.stellaris.platform.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.platform.systems.core.context.impl.ModifyOnlyContext;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.ValueStorage;
import com.st0x0ef.stellaris.platform.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.platform.systems.core.energy.wrappers.CommonItemEnergyStorage;
import com.st0x0ef.stellaris.platform.systems.core.energy.wrappers.NeoEnergyContainer;
import com.st0x0ef.stellaris.platform.systems.lookup.RegistryEventListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class EnergyItemLookup implements ItemLookup<ValueStorage, ItemContext>, RegistryEventListener {
    public static final EnergyItemLookup INSTANCE = new EnergyItemLookup();
    private static final ItemCapability<ValueStorage, ItemContext> CAPABILITY = ItemCapability.create(new ResourceLocation(CommonStorageLib.MOD_ID, "energy_item"), ValueStorage.class, ItemContext.class);

    private final List<Consumer<ItemRegistrar<ValueStorage, ItemContext>>> registrars = new ArrayList<>();

    private EnergyItemLookup() {
        registerSelf();
    }

    @Override
    public @Nullable ValueStorage find(ItemStack stack, ItemContext context) {
        ValueStorage container = stack.getCapability(CAPABILITY, context);

        if (container != null) {
            return container;
        }

        IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return storage == null ? null : new CommonItemEnergyStorage(storage, stack, context);
    }

    public void onRegister(Consumer<ItemRegistrar<ValueStorage, ItemContext>> registrar) {
        registrars.add(registrar);
    }

    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, items) -> {
           event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, ignored) -> {
               ValueStorage storage = getter.getContainer(stack, new ModifyOnlyContext(stack));
               return storage == null ? null : new NeoEnergyContainer(storage);
           }, items);

           event.registerItem(CAPABILITY, getter::getContainer, items);
        }));
    }
}
