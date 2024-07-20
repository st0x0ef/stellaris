package com.st0x0ef.stellaris.common.systems.core.item.input.consumers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.systems.core.CommonStorageLib;
import com.st0x0ef.stellaris.common.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.common.systems.core.fluid.FluidApi;
import com.st0x0ef.stellaris.common.systems.core.item.input.ConsumerType;
import com.st0x0ef.stellaris.common.systems.core.item.input.ItemConsumer;
import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.core.storage.util.TransferUtil;
import com.st0x0ef.stellaris.common.systems.resources.fluid.FluidResource;
import com.st0x0ef.stellaris.common.systems.resources.fluid.ingredient.SizedFluidIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

public record FluidConsumer(SizedFluidIngredient ingredient, Component name) implements ItemConsumer {
    public static final MapCodec<FluidConsumer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedFluidIngredient.FLAT_MB_CODEC.forGetter(FluidConsumer::ingredient),
            ComponentSerialization.CODEC.fieldOf("name").forGetter(FluidConsumer::name)
    ).apply(instance, FluidConsumer::new));
    public static final ConsumerType<FluidConsumer> TYPE = new ConsumerType<>(new ResourceLocation(CommonStorageLib.MOD_ID, "fluid"), CODEC);

    @Override
    public boolean test(ItemStack stack, ItemContext context) {
        CommonStorage<FluidResource> storage = FluidApi.ITEM.find(stack, context);
        return storage != null && TransferUtil.extractFluid(storage, ingredient, true).amount() > ingredient.getAmount();
    }

    @Override
    public void consume(ItemStack stack, ItemContext context) {
        CommonStorage<FluidResource> storage = FluidApi.ITEM.find(stack, context);
        TransferUtil.extractFluid(storage, ingredient, false);
    }

    @Override
    public ItemStack modifyDisplay(ItemStack stack) {
        ItemLore lore = stack.getOrDefault(DataComponents.LORE, ItemLore.EMPTY);
        lore = lore.withLineAdded(Component.translatable("misc.common_storage_lib.consume", Component.translatable("misc.common_storage_lib.fluid", Component.literal(String.valueOf(ingredient.getAmountAsMb())), name).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
        stack.set(DataComponents.LORE, lore); //TODO add translations ^
        return stack;
    }

    @Override
    public ConsumerType<?> getType() {
        return null;
    }
}
