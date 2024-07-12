package com.st0x0ef.stellaris.common.items.oxygen;

import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTankHelper;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import dev.architectury.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class OxygenTankItem extends Item {

    private final long capacity;

    public OxygenTankItem(Item.Properties properties, long capacity) {
        super(properties);
        this.capacity = Platform.isFabric() ? FluidTankHelper.convertFromMb(capacity) : capacity;
    }

    public static void setStoredOxygen(ItemStack stack, long amount) {
        stack.set(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get(), Mth.clamp(amount, 0, ((OxygenTankItem) stack.getItem()).getCapacity()));
    }

    public static long getStoredOxygen(ItemStack stack) {
        return stack.has(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get()) ? stack.get(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get()) : 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (stack.has(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get())) {
            tooltip.add(Component.translatable("tooltip.item.stellaris.oxygen_tank", getStoredOxygen(stack), capacity).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        long storedOxygen = getStoredOxygen(stack);
        return (int) Mth.clamp((13 + storedOxygen * 13) / capacity, 0, 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xA7E6ED;
    }

    public long getCapacity() {
        return capacity;
    }
}
