package com.st0x0ef.stellaris.common.items;

import com.fej1fun.potentials.energy.ItemEnergyStorage;
import com.fej1fun.potentials.energy.UniversalEnergyStorage;
import com.fej1fun.potentials.providers.EnergyProvider;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BatteryItem extends Item implements EnergyProvider.ITEM {
    public BatteryItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        UniversalEnergyStorage energy = getEnergy(stack);
        tooltipComponents.add(Component.translatable("tooltip.item.stellaris.energy", energy.getEnergy(), energy.getMaxEnergy()).withStyle(ChatFormatting.GRAY));

    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getEnergy(stack).getEnergy() > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        UniversalEnergyStorage storage = getEnergy(stack);
        return Mth.clamp(((storage.getEnergy() + 1) * 13) / storage.getMaxEnergy(), 0, 13);

    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x50C878;
    }

    @Override
    public @Nullable UniversalEnergyStorage getEnergy(@NotNull ItemStack stack) {
        return new ItemEnergyStorage(stack, DataComponentsRegistry.ENERGY.get(), 3000);
    }
}
