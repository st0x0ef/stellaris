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
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class IceSpit extends ThrowableItemProjectile {
    public IceSpit(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(EntityRegistry.ICE_SPIT.get(), level);
    }
//    public IceSpit(Level world, LivingEntity owner) {
//        super(EntityRegistry.ICE_SPIT.get(), owner, world); // null will be changed later
//    }
//
//    public IceSpit(Level world, double x, double y, double z) {
//        super(EntityRegistry.ICE_SPIT.get(), x, y, z, world);
//    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ItemsRegistry.ICE_SHARD.get();
    }
    @Environment(EnvType.CLIENT)
    private ParticleOptions getParticleParameters() {
        ItemStack itemStack = this.getItem();
        return (ParticleOptions) (itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, itemStack));
    }



}
