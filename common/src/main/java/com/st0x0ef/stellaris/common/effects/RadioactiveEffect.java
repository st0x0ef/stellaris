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
import net.minecraft.world.level.Level;
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
            Level level = livingEntity.level();

            if (amplifier == 0) {
                this.playSound(level, livingEntity);
                livingEntity.hurt(DamageSourceRegistry.of(level, DamageSourceRegistry.RADIATIONS), 0.5f);
            }
            else if (amplifier == 1) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
                this.playSound(level, livingEntity);
                livingEntity.hurt(DamageSourceRegistry.of(level, DamageSourceRegistry.RADIATIONS), 0.5f);
            }
            else if (amplifier == 2) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80));
                this.playSound(level, livingEntity);
                livingEntity.hurt(DamageSourceRegistry.of(level, DamageSourceRegistry.RADIATIONS), 1f);
            }
        }
    }

    private void playSound(Level level, LivingEntity entity) {
        if (!level.isClientSide) {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundRegistry.RADIOACTIVE.get(),
                    entity.getSoundSource(), 3.0F, 1.0F);
        }
    }

    @Override
    public @NotNull MobEffect withSoundOnAdded(SoundEvent event) {
        return super.withSoundOnAdded(SoundRegistry.RADIOACTIVE.get());
    }
}