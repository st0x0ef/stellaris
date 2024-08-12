package com.st0x0ef.stellaris.common.effects;

import com.st0x0ef.stellaris.common.registry.DamageSourceRegistry;
import com.st0x0ef.stellaris.common.registry.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RadioactiveEffect extends MobEffect {


    public RadioactiveEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity livingEntity, int amplifier, double health) {
        this.applyEffectTick(livingEntity, amplifier);
        if (livingEntity.getHealth() > 0.0F) {
            if (amplifier == 0) {
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

    }
}
