package com.st0x0ef.stellaris.common.data_components;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.common.items.module.SpaceSuitModule;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

public record SpaceSuitModules(List<ItemStack> modules) implements Serializable {

    public static final SpaceSuitModules EMPTY = new SpaceSuitModules(List.of());

    public static final Codec<SpaceSuitModules> CODEC = ItemStack.CODEC.listOf().xmap(SpaceSuitModules::new, modules -> modules.modules);
    public static final StreamCodec<RegistryFriendlyByteBuf, SpaceSuitModules> STREAM_CODEC = ItemStack.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(SpaceSuitModules::new, modules -> modules.modules);


    public ItemStack getItemUnsafe(int index) {
        return (ItemStack)this.modules.get(index);
    }

    public Stream<ItemStack> itemCopyStream() {
        return this.modules.stream().map(ItemStack::copy);
    }

    public Iterable<ItemStack> items() {
        return this.modules;
    }

    public Iterable<ItemStack> itemsCopy() {
        return Lists.<ItemStack, ItemStack>transform(this.modules, ItemStack::copy);
    }

    public List<SpaceSuitModule> getModules() {
        return Lists.<ItemStack, SpaceSuitModule>transform(this.modules, this::getModule);
    }

    private SpaceSuitModule getModule(ItemStack itemStack) {
        if (itemStack.getItem() instanceof SpaceSuitModule spaceSuitModule) return spaceSuitModule;
        return null; //failsafe, shouldn't happen unless tampered with or incorrect checks for upgrade station
    }
}
