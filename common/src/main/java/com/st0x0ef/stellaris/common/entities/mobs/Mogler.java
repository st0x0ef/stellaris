package com.st0x0ef.stellaris.common.entities.mobs;

import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class Mogler extends Hoglin {

    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(Mogler.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState attackAnimationState = new AnimationState();
    private int attackAnimationTick = -1;

    public Mogler(EntityType<? extends Mogler> type, Level world) { super(type, world); }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6)
                .add(Attributes.ATTACK_KNOCKBACK, 0.6)
                .add(Attributes.ATTACK_DAMAGE, 6);
    }

    public static boolean checkMoglerSpawnRules(EntityType<Mogler> entity, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource random) {
        return !levelAccessor.getBlockState(blockPos.below()).is(Blocks.NETHER_WART_BLOCK);
    }


    @Override
    public boolean removeWhenFarAway(double d) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        Mogler moglerentity = EntityRegistry.MOGLER.get().create(serverLevel);
        if (moglerentity != null) {
            moglerentity.setPersistenceRequired();
        }
        return moglerentity;
    }
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return NetworkManager.createAddEntityPacket(this, entity);
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide()) {
            checkAnim();
        }
    }

    private void checkAnim(){
        if (this.isAttacking() && !this.attackAnimationState.isStarted() && this.attackAnimationTick>=0) {
            this.attackAnimationState.start(this.tickCount);
        }
    }

    @Override
    public boolean doHurtTarget(Entity target){
        if (!(target instanceof LivingEntity)) {
            return false;
        } else {

            this.setAttacking(true);
            this.attackAnimationTick = 10;

            this.level().broadcastEntityEvent(this, (byte)4);
            this.makeSound(SoundEvents.HOGLIN_ATTACK);
            return HoglinBase.hurtAndThrowTarget(this, (LivingEntity)target);

        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.attackAnimationTick = 10;
            this.makeSound(SoundEvents.HOGLIN_ATTACK);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void aiStep() {
        if (this.attackAnimationTick > 0) {
            --this.attackAnimationTick;
        }
        else if (this.attackAnimationTick==0) {
            this.setAttacking(false);
            this.attackAnimationState.stop();
        }

        super.aiStep();
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, false);
    }
}
