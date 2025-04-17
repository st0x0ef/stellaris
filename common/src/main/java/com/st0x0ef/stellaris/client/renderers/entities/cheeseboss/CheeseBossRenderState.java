package com.st0x0ef.stellaris.client.renderers.entities.cheeseboss;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class CheeseBossRenderState extends LivingEntityRenderState {
    public AnimationState idleAnimationState = new AnimationState();
    public int idleAnimationTimeout = 0;
    public AnimationState punchAnimationState = new AnimationState();
    public int punchingAnimationTimeout = 0;
    public boolean punching = false;
    public AnimationState spitAnimationState = new AnimationState();
    public int spittingAnimationTimeout = 0;

    public void copy(CheeseBossRenderState state) {
        this.idleAnimationState = state.idleAnimationState;
        this.idleAnimationTimeout = state.idleAnimationTimeout;
        this.punchAnimationState = state.punchAnimationState;
        this.punchingAnimationTimeout = state.punchingAnimationTimeout;
        this.punching = state.punching;
        this.spitAnimationState = state.spitAnimationState;
        this.spittingAnimationTimeout = state.spittingAnimationTimeout;
    }
}
