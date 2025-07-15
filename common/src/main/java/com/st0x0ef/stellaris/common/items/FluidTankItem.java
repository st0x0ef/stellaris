package com.st0x0ef.stellaris.common.items;

import com.fej1fun.potentials.fluid.ItemFluidStorage;
import com.fej1fun.potentials.fluid.UniversalFluidItemStorage;
import com.fej1fun.potentials.providers.FluidProvider;
import com.st0x0ef.stellaris.common.blocks.machines.FluidTankBlock;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluidTankItem extends BlockItem implements FluidProvider.ITEM {

    final long capacity;

    public FluidTankItem(FluidTankBlock block, Properties properties) {
        super(block, properties);
        this.capacity = block.capacity;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        UniversalFluidItemStorage storage = getFluidTank(stack);
        tooltipComponents.add(Component.literal(storage.getFluidInTank(0).getAmount() + " / " + storage.getTankCapacity(0) + "mb").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public @Nullable UniversalFluidItemStorage getFluidTank(@NotNull ItemStack stack) {
        return new ItemFluidStorage(DataComponentsRegistry.FLUID_LIST.get(), stack, 1, capacity) {};
    }
}