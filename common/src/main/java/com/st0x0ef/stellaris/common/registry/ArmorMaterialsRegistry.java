package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;

public class ArmorMaterialsRegistry {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIAL = DeferredRegister.create(Stellaris.MODID, Registries.ARMOR_MATERIAL);

    public static final ArmorMaterial JET_SUIT_MATERIAL = new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.BODY, 11);
            }),
            10,
            SoundEvents.ARMOR_EQUIP_ELYTRA,
            () -> Ingredient.of(ItemsRegistry.DESH_INGOT.get()),
            List.of(new ArmorMaterial.Layer(ResourceLocation.parse("jetsuit"))),
            2.0F, 0.0F);

    public static final ArmorMaterial SPACE_SUIT_MATERIAL = new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 2);
                map.put(ArmorItem.Type.LEGGINGS, 5);
                map.put(ArmorItem.Type.CHESTPLATE, 6);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 9);
            }),
            10,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            () -> Ingredient.of(Items.IRON_INGOT),
            List.of(new ArmorMaterial.Layer(ResourceLocation.parse("spacesuit")),
                    new ArmorMaterial.Layer(ResourceLocation.parse("spacesuit"), "dyeable", true)
                    ),
            2.0F, 0.0F);


    public static final RegistrySupplier<ArmorMaterial> JET_SUIT = ARMOR_MATERIAL.register("radioactive", () -> JET_SUIT_MATERIAL);
    public static final RegistrySupplier<ArmorMaterial> SPACE_SUIT = ARMOR_MATERIAL.register("space_suit", () -> SPACE_SUIT_MATERIAL);


}
