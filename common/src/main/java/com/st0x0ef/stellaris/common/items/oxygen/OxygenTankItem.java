package com.st0x0ef.stellaris.common.items.oxygen;

import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTankHelper;
import com.st0x0ef.stellaris.common.data_components.OxygenComponent;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.utils.OxygenUtils;
import dev.architectury.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class OxygenTankItem extends Item {
    public OxygenTankItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (stack.has(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get())) {
            tooltip.add(Component.translatable("tooltip.item.stellaris.oxygen_tank", OxygenUtils.getOxygen(stack), OxygenUtils.getOxygenCapacity(stack)).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        long storedOxygen = OxygenUtils.getOxygen(stack);
        return (int) Mth.clamp((13 + storedOxygen * 13) / OxygenUtils.getOxygenCapacity(stack), 0, 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xA7E6ED;
    }
}
