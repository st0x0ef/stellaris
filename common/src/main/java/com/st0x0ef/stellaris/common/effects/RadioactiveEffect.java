package com.st0x0ef.stellaris.common.effects;

import com.st0x0ef.stellaris.common.registry.DamageSourceRegistry;
import com.st0x0ef.stellaris.common.registry.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RadioactiveEffect extends MobEffect {
    public RadioactiveEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
        this.soundOnAdded = Optional.of(SoundRegistry.RADIOACTIVE.get());
    }
    private Optional<SoundEvent> soundOnAdded;



    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        this.applyEffectTick(livingEntity, amplifier);
        if (livingEntity.getHealth() > 0.0F) {
            if (amplifier == 0) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
                livingEntity.hurt(DamageSourceRegistry.of(livingEntity.level(), DamageSourceRegistry.RADIATIONS), 0.5f);
            } else if (amplifier == 1) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
                livingEntity.hurt(DamageSourceRegistry.of(livingEntity.level(), DamageSourceRegistry.RADIATIONS), 0.5f);
            } else if (amplifier == 2) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80));
                livingEntity.hurt(DamageSourceRegistry.of(livingEntity.level(), DamageSourceRegistry.RADIATIONS), 1f);

            }
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
        return tickCount % 2 == 0;
    }

    @Override
    public @NotNull MobEffect withSoundOnAdded(SoundEvent event) {
        this.soundOnAdded = Optional.of(event);
        return super.withSoundOnAdded(event);
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        soundOnAdded.ifPresent(soundEvent -> livingEntity.level().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundEvent, livingEntity.getSoundSource(), 1.0F, 1.0F));
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        if (amplifier == 1) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
        } else if (amplifier == 2) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80));
        }
    }
}
