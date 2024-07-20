package com.st0x0ef.stellaris.common.systems.core.item.util;

import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.common.systems.resources.ResourceStack;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record ItemStorageData(List<ResourceStack<ItemResource>> stacks) {
    public static final Codec<ItemStorageData> CODEC = ResourceStack.ITEM_CODEC.listOf().xmap(ItemStorageData::new, ItemStorageData::stacks);

    public static final ItemStorageData EMPTY = new ItemStorageData(List.of());

    public static final Supplier<ItemStorageData> DEFAULT = () -> EMPTY;

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStorageData> NETWORK_CODEC = ByteBufCodecs.collection(size -> (List<ResourceStack<ItemResource>>) new ArrayList<ResourceStack<ItemResource>>(), ResourceStack.ITEM_STREAM_CODEC).map(ItemStorageData::new, ItemStorageData::stacks);

    public static ItemStorageData of(CommonStorage<ItemResource> container) {
        List<ResourceStack<ItemResource>> stacks = NonNullList.withSize(container.size(), ResourceStack.EMPTY_ITEM);
        for (int i = 0; i < container.size(); i++) {
            StorageSlot<ItemResource> slot = container.get(i);
            stacks.set(i, new ResourceStack<>(slot.getResource(), slot.getAmount()));
        }
        return new ItemStorageData(stacks);
    }
}
