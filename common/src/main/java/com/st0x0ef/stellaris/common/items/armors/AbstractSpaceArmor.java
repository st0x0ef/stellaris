package com.st0x0ef.stellaris.common.items.armors;

import com.fej1fun.potentials.fluid.ItemFluidStorage;
import com.fej1fun.potentials.fluid.UniversalFluidStorage;
import com.fej1fun.potentials.providers.FluidProvider;
import com.st0x0ef.stellaris.common.items.CustomArmorItem;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
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
        public @NotNull UniversalFluidStorage getFluidTank(@NotNull ItemStack stack) {
            if (storage == null) {
                storage = new ItemFluidStorage(DataComponentsRegistry.FLUID_LIST.get(), stack, 1, 3000);
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
        public @NotNull UniversalFluidStorage getFluidTank(@NotNull ItemStack stack) {
            if (storage == null || storage.getTanks() != 2) {
                storage = new ItemFluidStorage(DataComponentsRegistry.FLUID_LIST.get(), stack, 2, 3000);
            }
            return storage;
        }
    }
}
