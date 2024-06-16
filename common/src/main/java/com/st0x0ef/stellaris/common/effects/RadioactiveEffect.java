package com.st0x0ef.stellaris.common.effects;

import com.st0x0ef.stellaris.common.registry.DamageSourceRegistry;
import com.st0x0ef.stellaris.common.registry.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class RadioactiveEffect extends MobEffect {

    private final SoundEvent soundOnAdded = SoundRegistry.RADIOACTIVE.get();
    public RadioactiveEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

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

    public boolean shouldApplyEffectTickThisTick(int i, int j) {
        int k = 25 >> j;
        return k == 0 || i % k == 0;
    }

    public void onEffectAdded(LivingEntity livingEntity, int level) {
        super.onEffectAdded(livingEntity, level);
        livingEntity.level().playSound((Player) livingEntity, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundOnAdded, livingEntity.getSoundSource(), 3.0F, 1.0F);
    }
}
