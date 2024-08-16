package com.st0x0ef.stellaris.common.entities.cheese_boss.goals;

import com.st0x0ef.stellaris.common.entities.cheese_boss.CheeseBoss;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class CheeseMeleeAttackGoal extends MeleeAttackGoal {
    private final CheeseBoss cheeseBoss;

    public CheeseMeleeAttackGoal(CheeseBoss cheeseBoss, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(cheeseBoss, speedModifier, followingTargetEvenIfNotSeen);
        this.cheeseBoss = cheeseBoss;
    }

    public void start() {
        super.start();
        this.cheeseBoss.setPunching(true);
    }

    public void stop() {
        super.stop();
        this.cheeseBoss.setPunching(false);
    }

}
