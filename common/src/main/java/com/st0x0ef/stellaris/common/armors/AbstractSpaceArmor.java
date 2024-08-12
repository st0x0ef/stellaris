package com.st0x0ef.stellaris.common.armors;

import com.st0x0ef.stellaris.common.data_components.SpaceArmorComponent;
import com.st0x0ef.stellaris.common.items.CustomArmorItem;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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
            if (!PlanetUtil.hasOxygen(level.dimension().location())) {
                this.addOxygen(stack, -1);
            }
        }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
            super.appendHoverText(stack, level, tooltipComponents, isAdvanced);

            SpaceArmorComponent component = stack.get(DataComponentsRegistry.SPACE_ARMOR_ROCKET.get());

            if (component != null) {
                tooltipComponents.add(Component.translatable("jetsuit.stellaris.fuel", component.fuel()));
                tooltipComponents.add(Component.translatable("jetsuit.stellaris.oxygen", component.oxygen()));
            }
        }

        public void addOxygen(ItemStack stack, int amount) {
            SpaceArmorComponent component = stack.get(DataComponentsRegistry.SPACE_ARMOR_ROCKET.get());
            SpaceArmorComponent newComponent = new SpaceArmorComponent(component.fuel(), component.oxygen() + amount);
            stack.set(DataComponentsRegistry.SPACE_ARMOR_ROCKET.get(), newComponent);
        }

        public void addFuel(ItemStack stack, int amount) {
            SpaceArmorComponent component = stack.get(DataComponentsRegistry.SPACE_ARMOR_ROCKET.get());
            SpaceArmorComponent newComponent = new SpaceArmorComponent(component.fuel() + amount, component.oxygen());
            stack.set(DataComponentsRegistry.SPACE_ARMOR_ROCKET.get(), newComponent);
        }

        public long getFuel(ItemStack stack) {
            return stack.get(DataComponentsRegistry.SPACE_ARMOR_ROCKET.get()).fuel();
        }
        public int getOxygen(ItemStack stack) {
            return stack.get(DataComponentsRegistry.SPACE_ARMOR_ROCKET.get()).oxygen();
        }

        public int getMaxOxygen() {
            return 10000;
        }
    }
}
