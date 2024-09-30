package com.st0x0ef.stellaris.common.armors;

import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTankHelper;
import com.st0x0ef.stellaris.common.items.CustomArmorItem;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.utils.OxygenUtils;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import dev.architectury.platform.Platform;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class AbstractSpaceArmor extends CustomArmorItem {
    public AbstractSpaceArmor(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    public static class AbstractSpaceChestplate extends AbstractSpaceArmor {
        public AbstractSpaceChestplate(Holder<ArmorMaterial> material, Type type, Properties properties) {
            super(material, type, properties);
        }

        int tickBeforeNextOxygenCheck = 10;

        public void onArmorTick(ItemStack stack, Level level, Player player) {
            if (!PlanetUtil.hasOxygen(level) && !player.isCreative() && tickBeforeNextOxygenCheck == 0) {
                OxygenUtils.removeOxygen(stack, FluidTankHelper.convertFromNeoMb(1L));

            }

            tickBeforeNextOxygenCheck = tickBeforeNextOxygenCheck == 0 ? 10 : tickBeforeNextOxygenCheck - 1;
        }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

            tooltipComponents.add(Component.translatable("jetsuit.stellaris.oxygen", OxygenUtils.getOxygen(stack)));
        }
    }

    public static class Chestplate extends AbstractSpaceChestplate {
        public Chestplate(Holder<ArmorMaterial> material, Type type, Properties properties) {
            super(material, type, properties);
        }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

            tooltipComponents.add(Component.translatable("jetsuit.stellaris.fuel", getFuel(stack)));
        }

        public static void addFuel(ItemStack stack, int amount) {
            stack.set(DataComponentsRegistry.STORED_FUEL_COMPONENT.get(), stack.getOrDefault(DataComponentsRegistry.STORED_FUEL_COMPONENT.get(), 0).longValue() + amount);
        }

        public static long getFuel(ItemStack stack) {
            return stack.getOrDefault(DataComponentsRegistry.STORED_FUEL_COMPONENT.get(), 0).longValue();
        }
    }
}
