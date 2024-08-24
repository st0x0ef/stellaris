package com.st0x0ef.stellaris.common.entities.mobs.cheese_boss.attack_entities;

import com.st0x0ef.stellaris.common.entities.mobs.cheese_boss.CheeseBoss;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LlamaSpit;
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
        Entity var3 = this.getOwner();
        if (var3 instanceof LivingEntity livingEntity) {
            result.getEntity().hurt(this.damageSources().spit(this, livingEntity), 9.5F);
        }
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {

        int i1 = packet.getId();
        double d1 = packet.getX();
        double e1 = packet.getY();
        double f1 = packet.getZ();
        this.syncPacketPositionCodec(d1, e1, f1);
        this.moveTo(d1, e1, f1);
        this.setXRot(packet.getXRot());
        this.setYRot(packet.getYRot());
        this.setId(i1);
        this.setUUID(packet.getUUID());

        Entity entity = this.level().getEntity(packet.getData());
        if (entity != null) {
            this.setOwner(entity);
        }

        double d = packet.getXa();
        double e = packet.getYa();
        double f = packet.getZa();

        for(int i = 0; i < 7; ++i) {
            double g = 0.4 + 0.1 * (double)i;
            this.level().addParticle(ParticleTypes.SPIT, this.getX(), this.getY(), this.getZ(), d * g, e, f * g);
        }

        this.setDeltaMovement(d, e, f);

    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return NetworkManager.createAddEntityPacket(this, entity);
    }
}
