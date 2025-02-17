package com.st0x0ef.stellaris.common.items.armors;

import com.fej1fun.potentials.fluid.ItemFluidStorage;
import com.fej1fun.potentials.fluid.UniversalFluidItemStorage;
import com.fej1fun.potentials.providers.FluidProvider;
import com.st0x0ef.stellaris.common.items.CustomArmorItem;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
import dev.architectury.fluid.FluidStack;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractSpaceArmor extends CustomArmorItem {
    public AbstractSpaceArmor(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    public static class AbstractSpaceChestplate extends AbstractSpaceArmor implements FluidProvider.ITEM {
        protected ItemFluidStorage storage;

        public AbstractSpaceChestplate(Holder<ArmorMaterial> material, Type type, Properties properties) {
            super(material, type, properties);
        }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
            tooltipComponents.add(Component.translatable("jetsuit.stellaris.oxygen", getFluidTank(stack).getFluidInTank(0).getAmount()));

        }

        @Override
        public @NotNull UniversalFluidItemStorage getFluidTank(@NotNull ItemStack stack) {
            if (storage == null) {
                storage = new ItemFluidStorage(DataComponentsRegistry.FLUID_LIST.get(), stack, 1, 3000) {
                    @Override
                    public boolean isFluidValid(int tank, FluidStack stack) {
                        if (tank == 0) {
                            return stack.getFluid().isSame(FluidRegistry.OXYGEN_STILL.get());
                        }
                        return false;
                    }
                };
            }
            return storage;
        }
    }

    public static class Chestplate extends AbstractSpaceChestplate implements FluidProvider.ITEM {
        public Chestplate(Holder<ArmorMaterial> material, Type type, Properties properties) {
            super(material, type, properties);
        }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
            tooltipComponents.add(Component.translatable("jetsuit.stellaris.fuel", getFluidTank(stack).getFluidInTank(1).getAmount()));
        }

        @Override
        public @NotNull UniversalFluidItemStorage getFluidTank(@NotNull ItemStack stack) {
            if (storage == null || storage.getTanks() != 2) {
                storage = new ItemFluidStorage(DataComponentsRegistry.FLUID_LIST.get(), stack, 2, 3000) {
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

            return storage;
        }
    }
}
