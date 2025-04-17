package com.st0x0ef.stellaris.client.renderers.entities.customlightning;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class CustomLightningBoltRenderState extends LivingEntityRenderState {
    public float red, green, blue;
    public long seed;

    public CustomLightningBoltRenderState(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
}