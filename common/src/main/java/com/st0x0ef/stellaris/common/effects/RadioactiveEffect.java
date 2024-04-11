package com.st0x0ef.stellaris.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
public class RadioactiveEffect extends MobEffect {
    public int level;

    public RadioactiveEffect(MobEffectCategory mobEffectCategory, int color, int level) {
        super(mobEffectCategory, color);
        this.level = level;
    }

    public void applyEffectTick(LivingEntity livingEntity, int color) {
        super.applyEffectTick(livingEntity, color);
        if (livingEntity.getHealth() > 0.0F) {
            if (this.level == 1 || this.level == 2) {
                livingEntity.hurt(livingEntity.damageSources().magic(), 0.5f);
            }
            if (this.level == 2 || this.level == 3) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20));

                if (this.level == 3) {
                    livingEntity.hurt(livingEntity.damageSources().magic(), 1.0f);
                }
            }
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