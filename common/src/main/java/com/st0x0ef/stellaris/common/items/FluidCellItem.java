package com.st0x0ef.stellaris.common.items;

import com.fej1fun.potentials.fluid.ItemFluidStorage;
import com.fej1fun.potentials.fluid.UniversalFluidItemStorage;
import com.fej1fun.potentials.providers.FluidProvider;
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

public class FluidCellItem extends Item implements FluidProvider.ITEM {
    public FluidCellItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        UniversalFluidItemStorage storage = getFluidTank(stack);
        if (!storage.getFluidInTank(0).isEmpty()) tooltipComponents.add(storage.getFluidInTank(0).getName().copy().withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal(storage.getFluidInTank(0).getAmount() + "/" + storage.getTankCapacity(0) + "mb").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getFluidTank(stack).getFluidInTank(0).getAmount() > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        UniversalFluidItemStorage storage = getFluidTank(stack);
        return (int) Mth.clamp(((storage.getFluidInTank(0).getAmount() + 1) * 13) / storage.getTankCapacity(0), 0, 13);

    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x7DF9FF;
    }

    @Override
    public @Nullable UniversalFluidItemStorage getFluidTank(@NotNull ItemStack stack) {
        return new ItemFluidStorage(DataComponentsRegistry.FLUID_LIST.get(), stack, 1, 1000) {};
    }
}
