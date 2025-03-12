package com.st0x0ef.stellaris.platform.fabric;

import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.function.Supplier;

public class EffectRegisterImpl {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Stellaris.MODID, Registries.MOB_EFFECT);

    public static Holder<MobEffect> registerEffect(String name, Supplier<MobEffect> effect) {
        return MOB_EFFECTS.register(name, effect);
    }
}
