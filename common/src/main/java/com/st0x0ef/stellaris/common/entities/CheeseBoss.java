package com.st0x0ef.stellaris.common.entities;

import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class CheeseBoss extends Mob implements Enemy {
    public CheeseBoss(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6)
                .add(Attributes.ATTACK_KNOCKBACK, 0.6)
                .add(Attributes.ATTACK_DAMAGE, 6);
    }
}
