package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeTabsRegistry {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Stellaris.MODID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> STELLARIS_TAB = TABS.register(
            "stellaris",
            () -> CreativeTabRegistry.create(
                    Component.translatable("categorie.stellaris.main"),
                    () -> new ItemStack(ItemsRegistry.STEEL_INGOT)
            )
    );
    public static final RegistrySupplier<CreativeModeTab> STELLARIS_BLOCKS_TAB = TABS.register(
            "stellaris_blocks",
            () -> CreativeTabRegistry.create(
                    Component.translatable("categorie.stellaris_blocks.main"),
                    () -> new ItemStack(ItemsRegistry.STEEL_BLOCK_ITEM)
            )
    );
}
