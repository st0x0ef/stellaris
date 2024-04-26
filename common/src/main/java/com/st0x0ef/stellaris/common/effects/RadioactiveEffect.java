package com.st0x0ef.stellaris.common.effects;

import com.st0x0ef.stellaris.Stellaris;
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
    public boolean applyEffectTick(LivingEntity livingEntity, int level) {
        if (livingEntity.getHealth() > 0.0F) {
            if (level == 0) {
                livingEntity.hurt(DamageSourceRegistry.of(livingEntity.level(), DamageSourceRegistry.RADIATIONS), 0.5f);
            }
            else if (level == 1) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
                livingEntity.hurt(DamageSourceRegistry.of(livingEntity.level(), DamageSourceRegistry.RADIATIONS), 0.25f);
            }
            else if (level == 2) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
                livingEntity.hurt(DamageSourceRegistry.of(livingEntity.level(), DamageSourceRegistry.RADIATIONS), 0.5f);
            }

            return true;
        }

        return false;
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
            livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
        }
    }
}