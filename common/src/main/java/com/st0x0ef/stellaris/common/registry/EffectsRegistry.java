package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.effects.FoggingScreenEffect;
import com.st0x0ef.stellaris.common.effects.RadioactiveEffect;
import com.st0x0ef.stellaris.common.effects.SandStormEffect;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EffectsRegistry {

    private static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Stellaris.MODID, Registries.MOB_EFFECT);

    // DO NOT CONVERT THESE TO RegistrySuppliers
    public static final ResourceLocation RADIOACTIVE = ResourceLocationUtils.id("radioactive");
    public static final ResourceLocation SANDSTORM = ResourceLocationUtils.id("sandstorm");
    public static final ResourceLocation FOGGING_OVERLAY = ResourceLocationUtils.id("fogging");

    public static void register() {
        EFFECTS.register();

        // DO NOT CONVERT THESE TO CONSTANTS AND DO NOT REFERENCE THESE DIRECTLY
        EFFECTS.register(RADIOACTIVE, () -> new RadioactiveEffect(MobEffectCategory.HARMFUL, 8889187).withSoundOnAdded(SoundRegistry.RADIOACTIVE.get()));
        EFFECTS.register(SANDSTORM, () -> new SandStormEffect(MobEffectCategory.HARMFUL, 8889187));
        EFFECTS.register(FOGGING_OVERLAY, () -> new FoggingScreenEffect(MobEffectCategory.HARMFUL, 8889187));
    }

    public static Holder<MobEffect> getHolder(ResourceLocation id) {
        Holder<MobEffect> holder = EFFECTS.getRegistrar().getHolder(id);
        if (holder == null) {
            throw new IllegalArgumentException("MobEffect with id " + id + " does not exist");
        }
        return holder;
    }

    private EffectsRegistry() {}
}
