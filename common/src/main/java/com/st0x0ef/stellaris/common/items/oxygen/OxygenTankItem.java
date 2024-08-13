package com.st0x0ef.stellaris.common.items.oxygen;

import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTankHelper;
import dev.architectury.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OxygenTankItem extends Item {

    private final long capacity;

    public OxygenTankItem(Item.Properties properties, long capacity) {
        super(properties);
        this.capacity = Platform.isFabric() ? FluidTankHelper.convertFromMb(capacity) : capacity;
    }

    public static void setStoredOxygen(ItemStack stack, long amount) {
        stack.getOrCreateTagElement("oxygen").putLong("stored", Mth.clamp(amount, 0, ((OxygenTankItem) stack.getItem()).getCapacity()));
    }

    public static long getStoredOxygen(ItemStack stack) {
        return stack.getOrCreateTagElement("oxygen").getLong("stored");
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (stack.getOrCreateTagElement("oxygen").contains("stored")) {
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
