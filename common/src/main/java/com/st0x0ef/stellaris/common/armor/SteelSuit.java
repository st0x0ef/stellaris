package com.st0x0ef.stellaris.common.armor;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.armor.SpaceSuitModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;

import static net.minecraft.world.entity.EquipmentSlot.LEGS;

public class SteelSuit
{
    private static final ResourceLocation SUIT_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/armor/space_suit.png");
    private static final ResourceLocation PANTS_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/armor/space_pants.png");

    // Create models for each armor piece
    private static final HumanoidModel<LivingEntity> SUIT_MODEL = new SpaceSuitModel.SpaceSuitP1<>(null);
    private static final HumanoidModel<LivingEntity> PANTS_MODEL = new SpaceSuitModel.SpaceSuitP2<>(null);

    // Method to get the appropriate model for the given equipment slot
    public static HumanoidModel<LivingEntity> getArmorModel(EquipmentSlot slot) {
        switch (slot) {
            case HEAD:
            case CHEST:
                return SUIT_MODEL;
            case LEGS:
                return PANTS_MODEL;
            default:
                return null;
        }
    }

    // Method to get the appropriate texture for the given equipment slot
    public static ResourceLocation getArmorTexture(EquipmentSlot slot) {
        switch (slot) {
            case HEAD:
            case CHEST:
                return SUIT_TEXTURE;
            case LEGS:
                return PANTS_TEXTURE;
            default:
                return null;
        }
    }
}

