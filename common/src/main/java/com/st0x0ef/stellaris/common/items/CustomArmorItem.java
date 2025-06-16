package com.st0x0ef.stellaris.common.items;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public class CustomArmorItem extends ArmorItem {

    private final boolean enchantable;

    public CustomArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties, boolean enchantable) {
        super(material, type, properties.stacksTo(1));
        this.enchantable = enchantable;
    }


    @Override
    public boolean isEnchantable(ItemStack stack) {
        return this.enchantable;
    }
}
