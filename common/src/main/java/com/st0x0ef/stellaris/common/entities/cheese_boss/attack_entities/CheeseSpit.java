package com.st0x0ef.stellaris.common.entities.cheese_boss.attack_entities;

import com.st0x0ef.stellaris.common.entities.cheese_boss.CheeseBoss;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class CheeseSpit extends LlamaSpit {

    public CheeseSpit(EntityType<? extends CheeseSpit> entityType, Level level) {
        super(entityType, level);
    }

    private CheeseSpit(Level level, CheeseBoss spitter) {
        this(EntityRegistry.CHEESE_SPIT.get(), level);
        this.setOwner(spitter);
        this.setPos(spitter.getX() - (double)(spitter.getBbWidth() + 1.0F) * 0.5 * (double) Mth.sin(spitter.yBodyRot * 0.017453292F), spitter.getEyeY() - 0.10000000149011612, spitter.getZ() + (double)(spitter.getBbWidth() + 1.0F) * 0.5 * (double)Mth.cos(spitter.yBodyRot * 0.017453292F));
    }

    public static CheeseSpit fromLevelAndEntity(Level level, CheeseBoss cheeseBoss) {
        return new CheeseSpit(level, cheeseBoss);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity livingEntity) {
            entity = result.getEntity();
            DamageSource damageSource = this.damageSources().spit(this, livingEntity);
            if (entity.hurt(damageSource, 5.5F)) {
                Level level = this.level();
                if (level instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel) level;
                    EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
                }
            }
        }
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
//        int i1 = packet.getId();
//        double d1 = packet.getX();
//        double e1 = packet.getY();
//        double f1 = packet.getZ();
//        this.syncPacketPositionCodec(d1, e1, f1);
//        this.moveTo(d1, e1, f1);
//        this.setXRot(packet.getXRot());
//        this.setYRot(packet.getYRot());
//        this.setId(i1);
//        this.setUUID(packet.getUUID());
//        Entity entity = this.level().getEntity(packet.getData());
//        if (entity != null) {
//            this.setOwner(entity);
//        }
//        double d = packet.getXa();
//        double e = packet.getYa();
//        double f = packet.getZa();
//
//        for(int i = 0; i < 7; ++i) {
//            double g = 0.4 + 0.1 * (double)i;
//            this.level().addParticle(ParticleTypes.SPIT, this.getX(), this.getY(), this.getZ(), d * g, e, f * g); //TODO change particle
//        }
//
//        this.setDeltaMovement(d, e, f);
        super.recreateFromPacket(packet);
    }
}
