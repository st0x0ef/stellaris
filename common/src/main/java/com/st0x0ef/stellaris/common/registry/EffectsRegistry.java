package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.effects.RadioactiveEffect;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EffectsRegistry {
    public static final DeferredRegister<MobEffect> RADIOACTIVEEFFECT = DeferredRegister.create(Stellaris.MODID, Registries.MOB_EFFECT);

    public static final RegistrySupplier<MobEffect> RADIOACTIVE = RADIOACTIVEEFFECT.register("radioactive", () -> new RadioactiveEffect(MobEffectCategory.HARMFUL, 8889187));
}