package com.st0x0ef.stellaris.common.armors;

import com.st0x0ef.stellaris.common.items.CustomArmorItem;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.utils.OxygenUtils;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
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

    public static class Chestplate extends AbstractSpaceArmor {
        public Chestplate(Holder<ArmorMaterial> material, Type type, Properties properties) {
            super(material, type, properties);
        }

        public void onArmorTick(ItemStack stack, Level level, Player player) {
            if (!PlanetUtil.hasOxygen(level.dimension().location()) && !player.isCreative()) {
                OxygenUtils.removeOxygen(stack, 1);
            }
        }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

            tooltipComponents.add(Component.translatable("jetsuit.stellaris.fuel", getFuel(stack)));
            tooltipComponents.add(Component.translatable("jetsuit.stellaris.oxygen", OxygenUtils.getOxygen(stack)));
        }

        public static void addFuel(ItemStack stack, int amount) {
            stack.set(DataComponentsRegistry.STORED_FUEL_COMPONENT.get(), stack.getOrDefault(DataComponentsRegistry.STORED_FUEL_COMPONENT.get(), 0).longValue() + amount);
        }

        public static long getFuel(ItemStack stack) {
            return stack.getOrDefault(DataComponentsRegistry.STORED_FUEL_COMPONENT.get(), 0).longValue();
        }
    }
}
