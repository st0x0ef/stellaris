package com.st0x0ef.stellaris.platform.systems.core.item.input.consumers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.platform.systems.core.CommonStorageLib;
import com.st0x0ef.stellaris.platform.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.platform.systems.core.energy.EnergyApi;
import com.st0x0ef.stellaris.platform.systems.core.item.input.ConsumerType;
import com.st0x0ef.stellaris.platform.systems.core.item.input.ItemConsumer;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.ValueStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

public record EnergyConsumer(long energy) implements ItemConsumer {
    public static final MapCodec<EnergyConsumer> CODEC = Codec.LONG.fieldOf("energy").xmap(EnergyConsumer::new, EnergyConsumer::energy);
    public static final ConsumerType<EnergyConsumer> TYPE = new ConsumerType<>(new ResourceLocation(CommonStorageLib.MOD_ID, "energy"), CODEC);

    @Override
    public boolean test(ItemStack stack, ItemContext context) {
        ValueStorage valueStorage = EnergyApi.ITEM.find(stack, context);
        return valueStorage != null && valueStorage.extract(energy, true) >= energy;
    }

    @Override
    public void consume(ItemStack stack, ItemContext context) {
        ValueStorage valueStorage = EnergyApi.ITEM.find(stack, context);
        if (valueStorage != null) {
            valueStorage.extract(energy, false);
        }
    }

    @Override
    public ItemStack modifyDisplay(ItemStack stack) {
        ItemLore lore = stack.getOrDefault(DataComponents.LORE, ItemLore.EMPTY);
        lore = lore.withLineAdded(Component.translatable("misc.common_storage_lib.consume", Component.translatable("misc.common_storage_lib.energy", Component.literal(String.valueOf(energy)).withStyle(ChatFormatting.DARK_RED)).withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.GRAY));
        stack.set(DataComponents.LORE, lore); //TODO add translations ^
        return stack;
    }

    @Override
    public ConsumerType<?> getType() {
        return TYPE;
    }
}
