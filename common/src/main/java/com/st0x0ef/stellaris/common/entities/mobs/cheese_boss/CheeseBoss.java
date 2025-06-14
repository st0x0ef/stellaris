package com.st0x0ef.stellaris.common.entities.mobs.cheese_boss;

import com.mojang.serialization.Dynamic;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.registry.MemoryModuleTypeRegistry;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CheeseBoss extends Monster implements Enemy {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public final AnimationState punchAnimationState = new AnimationState();
    private int punchingAnimationTimeout = 0;
    private boolean punching = false;
    public final AnimationState spitAnimationState = new AnimationState();
    private int spittingAnimationTimeout = 0;
    private boolean spitting = false;

    private static final Component CHEESE_BOSS_NAME_COMPONENT = Component.translatable("event.stellaris.cheeseboss");
    private final ServerBossEvent bossEvent = (ServerBossEvent) new ServerBossEvent(CHEESE_BOSS_NAME_COMPONENT, BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);


    public CheeseBoss(EntityType<? extends CheeseBoss> type, Level level) {
        super(type, level);
        switchPhase("camembert");
    }

    // TODO real attributes
    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.MAX_HEALTH, 500)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.ATTACK_KNOCKBACK, 1.2)
                .add(Attributes.ATTACK_DAMAGE, 6);
    }

    /**Animations*/
    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 20;
            this.idleAnimationState.start(this.tickCount);
        }
        else {
            --this.idleAnimationTimeout;
        }
//        if (this.punchingAnimationTimeout <= 0 && isPunching()) {
//            this.punchingAnimationTimeout = 20;
//            setPunching(false);
//            this.punchAnimationState.start(this.tickCount);
//        }
//        else {
//            --this.punchingAnimationTimeout;
//        }
//        if (this.spittingAnimationTimeout <= 0 && isSpitting()) {
//            this.spittingAnimationTimeout = 30;
//            setSpitting(false);
//            this.spitAnimationState.start(this.tickCount);
//        }
//        else {
//            --this.spittingAnimationTimeout;
//        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            setupAnimationStates();
        }
        if  (this.getBrain().getMemory(MemoryModuleTypeRegistry.CURRENT_PHASE.get()).isEmpty()) {
            this.getBrain().setMemory(MemoryModuleTypeRegistry.CURRENT_PHASE.get(), "cheddar");
        }
        tickAllCooldowns();
}

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 35.0f));

        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean bl = super.hurt(source, amount);
        if (!this.level().isClientSide && !this.isNoAi() && bl && source.getEntity() != null && source.getEntity() != this) {
            Entity entity = source.getEntity();
            if (this.brain.getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty() && entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                if (source.isDirect() || this.closerThan(livingEntity, 5.0F)) {
                    this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, livingEntity);
                }
            }
        }

        return bl;
    }


    /**boss bar event*/
    @Override
    protected void customServerAiStep() {
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
    }

    public void tickAllCooldowns() {
        this.getBrain().getMemory(MemoryModuleTypeRegistry.SWITCH_COOLDOWN.get()).ifPresent(cooldown -> {
            if (cooldown > 0) {
                this.getBrain().setMemory(MemoryModuleTypeRegistry.SWITCH_COOLDOWN.get(), cooldown - 1);
            }
        });
        if (getCurrentPhase().equals("cheddar")) {
            this.getBrain().getMemory(MemoryModuleTypeRegistry.SPIKES_COOLDOWN.get()).ifPresent(cooldown -> {
                if (cooldown > 0) {
                    this.getBrain().setMemory(MemoryModuleTypeRegistry.SPIKES_COOLDOWN.get(), cooldown - 1);
                }
            });
            this.getBrain().getMemory(MemoryModuleTypeRegistry.CHEESE_RAIN_COOLDOWN.get()).ifPresent(cooldown -> {
                if (cooldown > 0) {
                    this.getBrain().setMemory(MemoryModuleTypeRegistry.CHEESE_RAIN_COOLDOWN.get(), cooldown - 1);
                }
            });
        }
        else if (getCurrentPhase().equals("camembert")) {
            this.getBrain().getMemory(MemoryModuleTypeRegistry.CHEESE_WHEEL_COOLDOWN.get()).ifPresent(cooldown -> {
                if (cooldown > 0) {
                    this.getBrain().setMemory(MemoryModuleTypeRegistry.CHEESE_WHEEL_COOLDOWN.get(), cooldown - 1);
                }
            });
            this.getBrain().getMemory(MemoryModuleTypeRegistry.MOLD_BOMB_COOLDOWN.get()).ifPresent(cooldown -> {
                if (cooldown > 0) {
                    this.getBrain().setMemory(MemoryModuleTypeRegistry.MOLD_BOMB_COOLDOWN.get(), cooldown - 1);
                }
            });
        }
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return NetworkManager.createAddEntityPacket(this, entity);
    }

    @Override
    protected @NotNull Brain<?> makeBrain(Dynamic<?> dynamic) {
        return CheeseBossAi.makeBrain(this, dynamic);
    }

    @Contract(value = "null->false")
    public boolean canTargetEntity(@Nullable Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity) ||
                this.level() != entity.level() ||
                !EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) ||
                this.isAlliedTo(entity) ||
                livingEntity.getType() == EntityType.ARMOR_STAND ||
                livingEntity.getType() == EntityRegistry.CHEESE_BOSS.get() ||
                livingEntity.isInvulnerable() || livingEntity.isDeadOrDying()) {
            return false;
        }
        return this.level().getWorldBorder().isWithinBounds(livingEntity.getBoundingBox());
    }

    public String getCurrentPhase() {
        return this.getBrain().getMemory(MemoryModuleTypeRegistry.CURRENT_PHASE.get()).orElse("cheddar");
    }

    public void switchPhase(String phase) {
        this.getBrain().setMemory(MemoryModuleTypeRegistry.CURRENT_PHASE.get(), phase);
        this.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE);
    }

    protected static void spawnCheeseSpikes(CheeseBoss cheeseBoss) {
        // Logic to spawn cheese spikes around or in front of the Cheese Boss

    }
}
