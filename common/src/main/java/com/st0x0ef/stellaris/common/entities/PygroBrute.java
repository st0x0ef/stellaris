package com.st0x0ef.stellaris.common.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.level.Level;

public class PygroBrute extends PiglinBrute {
    public PygroBrute(EntityType<? extends PiglinBrute> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.MAX_HEALTH, 16)
                .add(Attributes.ATTACK_DAMAGE, 5);
    }
    private boolean PYGRO_BRUTE_SPAWN = true;
    @Override
    public void tick() {
        super.tick();
        if (!PYGRO_BRUTE_SPAWN) {
            if (!this.level().isClientSide) {
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }
}
