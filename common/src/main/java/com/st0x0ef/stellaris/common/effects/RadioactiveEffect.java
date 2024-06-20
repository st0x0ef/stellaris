package com.st0x0ef.stellaris.common.effects;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.registry.DamageSourceRegistry;
import com.st0x0ef.stellaris.common.registry.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class RadioactiveEffect extends MobEffect {

    private final SoundEvent soundOnAdded = SoundRegistry.RADIOACTIVE.get();
    public RadioactiveEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int level) {
        if (livingEntity.getHealth() > 0.0F) {
            if (level == 0) {
                livingEntity.hurt(DamageSourceRegistry.of(livingEntity.level(), DamageSourceRegistry.RADIATIONS), 0.5f);
            } else if (level == 1) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
                livingEntity.hurt(DamageSourceRegistry.of(livingEntity.level(), DamageSourceRegistry.RADIATIONS), 0.5f);
            } else if (level == 2) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80));
                livingEntity.hurt(DamageSourceRegistry.of(livingEntity.level(), DamageSourceRegistry.RADIATIONS), 1f);
            }
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int i, int j) {
        return true;
    }

    @Override
    public @NotNull MobEffect withSoundOnAdded(SoundEvent event) {
        return super.withSoundOnAdded(soundOnAdded);
    }
}
