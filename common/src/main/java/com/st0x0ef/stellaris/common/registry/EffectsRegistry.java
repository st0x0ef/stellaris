package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.common.effects.RadioactiveEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EffectsRegistry {
    public static final MobEffect RADIOACTIVE;

    static {
        RADIOACTIVE = register("radioactive", new RadioactiveEffect(MobEffectCategory.HARMFUL, 8889187));
    }

    private static MobEffect register(String string, MobEffect mobEffect) {
        return Registry.register(BuiltInRegistries.MOB_EFFECT, string, mobEffect);
    }
}


