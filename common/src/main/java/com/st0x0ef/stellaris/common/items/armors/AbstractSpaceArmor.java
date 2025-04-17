package com.st0x0ef.stellaris.common.items.armors;

import com.fej1fun.potentials.fluid.ItemFluidStorage;
import com.fej1fun.potentials.fluid.UniversalFluidItemStorage;
import com.fej1fun.potentials.providers.FluidProvider;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
import dev.architectury.fluid.FluidStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class AbstractSpaceArmor extends Item {
    public AbstractSpaceArmor(Item.Properties properties) {
        super(properties);
    }

    public static class AbstractSpaceChestplate extends AbstractSpaceArmor implements FluidProvider.ITEM {

        public AbstractSpaceChestplate(Properties properties) {
            super(properties);
        }

        @Override
        public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
            super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);

            consumer.accept(Component.translatable("jetsuit.stellaris.oxygen", getFluidTank(itemStack).getFluidInTank(0).getAmount()));
        }

        @Override
        public @NotNull UniversalFluidItemStorage getFluidTank(@NotNull ItemStack stack) {

            return new ItemFluidStorage(DataComponentsRegistry.FLUID_LIST.get(), stack, 1, 3000) {
                @Override
                public boolean isFluidValid(int tank, FluidStack stack) {
                    return stack.getFluid().isSame(FluidRegistry.OXYGEN_STILL.get());
                }
            };
        }
    }

    public static class Chestplate extends AbstractSpaceChestplate {
        public Chestplate(Properties properties) {
            super(properties);
        }

        @Override
        public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
            super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
            consumer.accept(Component.translatable("jetsuit.stellaris.fuel", getFluidTank(itemStack).getFluidInTank(1).getAmount()));
        }

        @Override
        public @NotNull UniversalFluidItemStorage getFluidTank(@NotNull ItemStack stack) {
            return new ItemFluidStorage(DataComponentsRegistry.FLUID_LIST.get(), stack, 2, 3000) {
                    @Override
                public boolean isFluidValid(int tank, FluidStack stack) {
                    return switch (tank) {
                        case 0 -> stack.getFluid().isSame(FluidRegistry.OXYGEN_STILL.get());
                        case 1 -> stack.getFluid().isSame(FluidRegistry.FUEL_STILL.get());
                        default -> false;
                    };
                }
            };
        }
    }
}
