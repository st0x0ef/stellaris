package com.st0x0ef.stellaris.common.armors;

import com.st0x0ef.stellaris.common.data_components.SpaceArmorComponent;
import com.st0x0ef.stellaris.common.items.CustomArmorItem;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractSpaceArmor extends CustomArmorItem {
    public AbstractSpaceArmor(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    public static class Chestplate extends AbstractSpaceArmor {

        public Chestplate(ArmorMaterial material, Type type, Properties properties) {
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

            SpaceArmorComponent component = SpaceArmorComponent.getData(stack.getOrCreateTag());

            tooltipComponents.add(Component.translatable("jetsuit.stellaris.fuel", component.fuel()));
            tooltipComponents.add(Component.translatable("jetsuit.stellaris.oxygen", component.oxygen()));
        }

        public void addOxygen(ItemStack stack, int amount) {
            SpaceArmorComponent component = SpaceArmorComponent.getData(stack.getOrCreateTag());
            SpaceArmorComponent newComponent = new SpaceArmorComponent(component.fuel(), component.oxygen() + amount);
            newComponent.savaData(stack.getOrCreateTag());
        }

        public void addFuel(ItemStack stack, int amount) {
            SpaceArmorComponent component = SpaceArmorComponent.getData(stack.getOrCreateTag());
            SpaceArmorComponent newComponent = new SpaceArmorComponent(component.fuel() + amount, component.oxygen());
            newComponent.savaData(stack.getOrCreateTag());
        }

        public long getFuel(ItemStack stack) {
            return SpaceArmorComponent.getData(stack.getOrCreateTag()).fuel();
        }
        public int getOxygen(ItemStack stack) {
            return SpaceArmorComponent.getData(stack.getOrCreateTag()).oxygen();
        }

        public int getMaxOxygen() {
            return 10000;
        }
    }
}
