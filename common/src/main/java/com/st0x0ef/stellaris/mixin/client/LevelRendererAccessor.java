package com.st0x0ef.stellaris.mixin.client;

import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {
    @Accessor("skyBuffer")
    VertexBuffer stellaris$getSkyBuffer();

    @Accessor("darkBuffer")
    VertexBuffer stellaris$getDarkBuffer();

    @Accessor("ticks")
    int stellaris$ticks();

}