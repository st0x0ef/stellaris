package com.st0x0ef.stellaris.common.effects;

import com.st0x0ef.stellaris.common.registry.DamageSourceRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
public class RadioactiveEffect extends MobEffect {

    public RadioactiveEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int level) {
        if (livingEntity.getHealth() > 0.0F) {
            if (level == 0 || level == 1) {
                livingEntity.hurt(DamageSourceRegistry.of(livingEntity.level(), DamageSourceRegistry.RADIATIONS), 0.5f);
            }
            if (level == 1 || level == 2) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20));

                if (level >= 2) {
                    livingEntity.hurt(DamageSourceRegistry.of(livingEntity.level(), DamageSourceRegistry.RADIATIONS), 1.0f);
                }
            }
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int i, int j) {
        int k = 25 >> j;
        if (k > 0) {
            return i % k == 0;
        } else {
            return true;
        }
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int i) {
        if (i == 1 || i == 2) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20));
        }
    }
}