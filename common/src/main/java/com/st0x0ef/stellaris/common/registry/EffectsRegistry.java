package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.effects.RadioactiveEffect;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EffectsRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT = DeferredRegister.create(Stellaris.MODID, Registries.MOB_EFFECT);

    public static final Holder<MobEffect> RADIOACTIVE = MOB_EFFECT.register("radioactive", () -> new RadioactiveEffect(MobEffectCategory.HARMFUL, 8889187));
}


