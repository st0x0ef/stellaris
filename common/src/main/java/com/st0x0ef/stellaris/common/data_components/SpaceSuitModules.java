package com.st0x0ef.stellaris.common.data_components;

import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.common.module.*;
import com.st0x0ef.stellaris.common.module.Module;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.registry.ModuleRegistry;
import com.st0x0ef.stellaris.common.registry.RegistryRegistry;
import com.st0x0ef.stellaris.platform.RegistrarUtilPlatform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

public record SpaceSuitModules(List<SpaceSuitModule> modules) implements Serializable {

    public static SpaceSuitModules empty() {
        return new SpaceSuitModules(List.of());
    }

    public static final Codec<SpaceSuitModules> CODEC = RegistrarUtilPlatform.getByNameCodec(RegistryRegistry.SUIT_MODULE)
            .listOf().xmap(SpaceSuitModules::new, SpaceSuitModules::modules);
    public static final StreamCodec<RegistryFriendlyByteBuf, SpaceSuitModules> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public List<? extends Item> items() {
        return this.modules.stream().map(Module::getAsItem).map(Supplier::get).toList();
    }

    public List<ItemStack> itemStacks() {
        return this.modules.stream().map(Module::getAsItem).map(Supplier::get).map(ItemStack::new).toList();
    }

    public static boolean contains(ItemStack stack, Item module) {
        SpaceSuitModules spaceSuitModules = stack.getOrDefault(DataComponentsRegistry.SPACE_SUIT_MODULES.get(), empty());
        if (spaceSuitModules.modules == null)
            return false;

        for (Item item : spaceSuitModules.items())
            if (item.equals(module))
                return true;

        return false;
    }

    public static boolean contains(ItemStack stack, Module module) {
        SpaceSuitModules spaceSuitModules = stack.getOrDefault(DataComponentsRegistry.SPACE_SUIT_MODULES.get(), empty());
        if (spaceSuitModules.modules == null)
            return false;

        for (Module module1 : spaceSuitModules.modules)
            if (module1.equals(module))
                return true;

        return false;
    }

    public static boolean containsAllInModules(ItemStack stack, Set<? extends Module> modules) {
        boolean containsAll = true;
        for (Module module : modules) {
            if (!containsInModules(stack, module)) {
                containsAll = false;
            }
            break;
        }
        return containsAll;
    }

    public static boolean containsInModules(ItemStack stack, ItemStack module) {
        return containsInModules(stack, getModule(module));
    }

    public static boolean containsInModules(ItemStack stack, Module module) {
        if (stack.isEmpty()) {
            return false;
        }
        SpaceSuitModules spaceSuitModules = stack.get(DataComponentsRegistry.SPACE_SUIT_MODULES.get());
        if (spaceSuitModules == null) {
            return false;
        }
        boolean boolToReturn = false;
        for (SpaceSuitModule module1 : spaceSuitModules.modules) {
            if (module1 == module) {
                boolToReturn = true;
                break;
            }
        }

        return boolToReturn;
    }

    private static SpaceSuitModule getModule(ItemStack itemStack) {
        if (itemStack.getItem() instanceof SpaceSuitModule spaceSuitModule) {
            return spaceSuitModule;
        }
        return null; //failsafe, shouldn't happen unless tampered with or incorrect checks for upgrade station
    }

    public Mutable toMutable() {
        return new Mutable(this);
    }

    public static class Mutable {

        private final List<SpaceSuitModule> modules;

        public Mutable(SpaceSuitModules contents) {
            this.modules = new ArrayList<>(contents.modules);
        }

        public Mutable insert(SpaceSuitModule module) {
            this.modules.add(module);
            return this;
        }

        public <M extends Item & SpaceSuitModule> Mutable insert(M module) {
            this.modules.add(module);
            return this;
        }

        public SpaceSuitModules toImmutable() {
            return new SpaceSuitModules(List.copyOf(this.modules));
        }
    }
}
