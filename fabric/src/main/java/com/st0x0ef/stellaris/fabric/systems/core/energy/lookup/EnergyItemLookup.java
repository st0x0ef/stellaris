package com.st0x0ef.stellaris.fabric.systems.core.energy.lookup;

import com.st0x0ef.stellaris.common.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.common.systems.core.storage.base.ValueStorage;
import com.st0x0ef.stellaris.common.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.fabric.systems.core.storage.common.CommonValueStorage;
import com.st0x0ef.stellaris.fabric.systems.core.storage.context.CommonItemContext;
import com.st0x0ef.stellaris.fabric.systems.core.storage.context.FabricItemContext;
import com.st0x0ef.stellaris.fabric.systems.core.storage.fabric.FabricLongStorage;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

import java.util.function.Consumer;

public class EnergyItemLookup implements ItemLookup<ValueStorage, ItemContext> {

    @Override
    public @Nullable ValueStorage find(ItemStack stack, ItemContext context) {
        EnergyStorage storage = EnergyStorage.ITEM.find(stack, new FabricItemContext(context));
        if (storage == null) {
            return null;
        }
        if (storage instanceof FabricLongStorage(ValueStorage rootContainer, var ignored)) {
            return rootContainer;
        }
        return new CommonValueStorage(storage);
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<ValueStorage, ItemContext>> registrar) {
        registrar.accept(this::registerSelf);
    }

    @Override
    public void registerSelf(ItemGetter<ValueStorage, ItemContext> getter, Item... items) {
        EnergyStorage.ITEM.registerForItems((stack, context) -> {
            ValueStorage container = getter.getContainer(stack, new CommonItemContext(context));
            return new FabricLongStorage(container);
        }, items);
    }
}
