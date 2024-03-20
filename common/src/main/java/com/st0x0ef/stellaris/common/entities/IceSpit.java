package com.st0x0ef.stellaris.common.entities;

import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class IceSpit extends AbstractArrow implements ItemSupplier {


    public IceSpit(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(EntityRegistry.ICE_SPIT.get(), level, new ItemStack(ItemsRegistry.ICE_SHARD.get()));
    }


    protected IceSpit(EntityType<? extends AbstractArrow> entityType, LivingEntity livingEntity, Level level, ItemStack itemStack) {
        super(EntityRegistry.ICE_SPIT.get(), livingEntity, level, new ItemStack(ItemsRegistry.ICE_SHARD.get()));
    }

    public @NotNull ItemStack getItem() {
        return new ItemStack(ItemsRegistry.ICE_SHARD.get());
    }

    public static IceSpit shoot(LivingEntity entity, LivingEntity target, int damage) {
        IceSpit entityArrow = new IceSpit(EntityRegistry.ICE_SPIT.get(),entity, entity.level(), new ItemStack(ItemsRegistry.ICE_SHARD.get()));

        double d0 = target.getY() + (double) target.getEyeHeight() - 1.1;
        double d1 = target.getX() - entity.getX();
        double d3 = target.getZ() - entity.getZ();

        entityArrow.shoot(d1, d0 - entityArrow.getY() + Math.sqrt(d1 * d1 + d3 * d3) * 0.2F, d3, 0.7f * 2, 12.0F);
        entityArrow.setSilent(true);
        entityArrow.setBaseDamage(damage);
        entityArrow.setKnockback(1);
        entityArrow.setCritArrow(false);

        entity.level().addFreshEntity(entityArrow);

        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GLASS_BREAK, SoundSource.HOSTILE, 1, 1f / (new Random().nextFloat() * 0.5f + 1));
        return entityArrow;
    }


}
