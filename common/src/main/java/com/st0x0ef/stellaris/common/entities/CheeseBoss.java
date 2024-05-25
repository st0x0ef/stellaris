package com.st0x0ef.stellaris.common.entities;

import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;

public class CheeseBoss extends Monster implements Enemy, RangedAttackMob {

    private static final Component CHEESE_BOSS_NAME_COMPONENT = Component.translatable("event.stellaris.cheeseboss");
    private final ServerBossEvent bossEvent = (ServerBossEvent)new ServerBossEvent(CHEESE_BOSS_NAME_COMPONENT, BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);


    public CheeseBoss(EntityType<? extends Monster> entityType, Level level) {
        super(EntityRegistry.CHEESE_BOSS.get(), level);
    }

    //TODO real attributes
    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6)
                .add(Attributes.ATTACK_KNOCKBACK, 0.6)
                .add(Attributes.ATTACK_DAMAGE, 6);
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

    /**ranged attack*/
    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {

    }
}
