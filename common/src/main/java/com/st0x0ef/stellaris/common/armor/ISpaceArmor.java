package com.st0x0ef.stellaris.common.armor;

import com.google.common.collect.Maps;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.GaugeTextHelper;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.GaugeValueHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public abstract class ISpaceArmor extends ArmorItem {
    private static final HashMap<String, ResourceLocation> TEXTURES = Maps.newHashMap();
    public static final String FUEL_TAG = Stellaris.MODID + ":fuel";
    public ISpaceArmor(ArmorMaterial armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }
    public static abstract class Helmet extends ISpaceArmor {
        public Helmet(ArmorMaterial armorMaterial, Type equipmentSlot, Properties properties) {
            super(armorMaterial, equipmentSlot, properties);
        }
    }

    public static abstract class Chestplate extends ISpaceArmor {
        public Chestplate(ArmorMaterial armorMaterial, Type equipmentSlot, Properties properties) {
            super(armorMaterial, equipmentSlot, properties);
        }

        @Override
        public void appendHoverText(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag) {
            super.appendHoverText(itemStack, level, list, tooltipFlag);
            //IOxygenStorage oxygen = OxygenUtil.getItemStackOxygenStorage(itemStack);
            //list.add(GaugeTextHelper.buildSpacesuitOxygenTooltip(GaugeValueHelper.getOxygen(oxygen.getOxygen(), oxygen.getMaxCapacity())));
        }

        @Override
        public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
            super.inventoryTick(itemStack, level, entity, i, bl);
        }

        public abstract int getOxygenCapacity();
    }
    public static abstract class Leggings extends ISpaceArmor {
        public Leggings(ArmorMaterial armorMaterial, Type equipmentSlot, Properties properties) {
            super(armorMaterial, equipmentSlot, properties);
        }

        public void appendHoverText(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag) {
            super.appendHoverText(itemStack, level, list, tooltipFlag);
            int fuel = itemStack.getOrCreateTag().getInt(FUEL_TAG);
            int capacity = this.getFuelCapacity();
            list.add(GaugeTextHelper.buildFuelStorageTooltip(GaugeValueHelper.getFuel(fuel, capacity), ChatFormatting.GRAY));
        }

        @Override
        public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
            super.inventoryTick(itemStack, level, entity, i, bl);
        }

        public abstract int getFuelCapacity();
    }

    public static abstract class Boots extends ISpaceArmor {
        public Boots(ArmorMaterial armorMaterial, Type equipmentSlot, Properties properties) {
            super(armorMaterial, equipmentSlot, properties);
        }
    }

    public ResourceLocation getTexture(ItemStack itemStack, LivingEntity entity) {
        String loc = Stellaris.MODID + "textures/armor/"+ entity.getArmorSlots();
        ResourceLocation texture = TEXTURES.get(loc);
        if (texture == null) {
            texture = new ResourceLocation(loc);
            TEXTURES.put(loc, texture);
        }

        return texture;
    }

}
