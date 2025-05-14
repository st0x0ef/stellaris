package com.st0x0ef.stellaris.common.effects;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.registry.EffectsRegistry;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class OxygenEffectsOverlay extends MobEffect {


    public static ResourceLocation OXYGEN_OVERLAY = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/overlay/oxygen_overlay.png");

    public OxygenEffectsOverlay(MobEffectCategory category, int color) {
        super(category, color);
    }

    public Holder<MobEffect> getMobEffect() {
        return EffectsRegistry.OXYGEN;
    }
}



