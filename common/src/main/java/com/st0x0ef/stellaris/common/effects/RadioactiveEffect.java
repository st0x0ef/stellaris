package com.st0x0ef.stellaris.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
public class RadioactiveEffect extends MobEffect {

    public RadioactiveEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    public void applyEffectTick(LivingEntity livingEntity, int i) {
        super.applyEffectTick(livingEntity, i);
        if (livingEntity.getHealth() > 0.0F) {
            livingEntity.hurt(livingEntity.damageSources().magic(), 2.0F);
        }

    }

    public boolean shouldApplyEffectTickThisTick(int i, int j) {
        int k = 25 >> j;
        if (k > 0) {
            return i % k == 0;
        } else {
            return true;
        }
    }
}