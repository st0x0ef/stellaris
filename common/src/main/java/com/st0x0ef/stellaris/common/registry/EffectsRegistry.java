package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.common.effects.RadioactiveEffect;
import com.st0x0ef.stellaris.platform.EffectRegister;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
public class EffectsRegistry {

    public static final Holder<MobEffect> RADIOACTIVE = EffectRegister.registerEffect("radioactive", () -> new RadioactiveEffect(MobEffectCategory.HARMFUL, 8889187));

}