package com.st0x0ef.stellaris.common.registry;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum ArmorMaterialsRegistry implements StringRepresentable, ArmorMaterial {
    JET_SUI_MATERIAL("jetsuit", 10, Util.make(new EnumMap<>(ArmorItem.Type.class), (enumMap) -> {
        enumMap.put(ArmorItem.Type.BOOTS, 1);
        enumMap.put(ArmorItem.Type.LEGGINGS, 2);
        enumMap.put(ArmorItem.Type.CHESTPLATE, 3);
        enumMap.put(ArmorItem.Type.HELMET, 1);
    }), 15, SoundEvents.ARMOR_EQUIP_ELYTRA, 2.0F, 0.0F, () -> Ingredient.of(ItemsRegistry.DESH_INGOT.get()));

    public static final Codec<ArmorMaterials> CODEC = StringRepresentable.fromEnum(ArmorMaterials::values);
    private static final EnumMap<ArmorItem.Type, Object> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (enumMap) -> {
        enumMap.put(ArmorItem.Type.BOOTS, 13);
        enumMap.put(ArmorItem.Type.LEGGINGS, 15);
        enumMap.put(ArmorItem.Type.CHESTPLATE, 16);
        enumMap.put(ArmorItem.Type.HELMET, 11);
    });
    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    ArmorMaterialsRegistry(String name, int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> protectionFunctionForType, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionFunctionForType = protectionFunctionForType;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue(repairIngredient);
    }

    public int getDurabilityForType(ArmorItem.Type type) {
        return (Integer)HEALTH_FUNCTION_FOR_TYPE.get(type) * this.durabilityMultiplier;
    }

    public int getDefenseForType(ArmorItem.Type type) {
        return this.protectionFunctionForType.get(type);
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    public String getSerializedName() {
        return this.name;
    }

}
