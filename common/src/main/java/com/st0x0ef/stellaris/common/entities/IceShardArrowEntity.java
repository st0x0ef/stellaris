package com.st0x0ef.stellaris.common.entities;

import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class IceShardArrowEntity extends AbstractArrow {

    private static final ItemStack DEFAULT_ARROW_STACK;

    public IceShardArrowEntity(EntityType<? extends IceShardArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public IceShardArrowEntity(Level level, LivingEntity livingEntity, ItemStack itemStack) {
        super(EntityRegistry.ICE_SHARD_ARROW.get(), livingEntity, level, itemStack);
    }


    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);

        Entity entity = entityHitResult.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 150));
        }
    }

    static {
        DEFAULT_ARROW_STACK = new ItemStack(ItemsRegistry.ICE_SHARD_ARROW.get());
    }

    @Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkManager.createAddEntityPacket(this);
	}
}
