package com.st0x0ef.stellaris.mixin.client;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rover.RoverItemRenderer;
import com.st0x0ef.stellaris.client.renderers.globe.GlobeItemRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpecialModelRenderers.class)
public class CustomItemModelRenderer {
    @Shadow
    @Final
    private static ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends SpecialModelRenderer.Unbaked>> ID_MAPPER;

    @Inject(at = @At(value = "HEAD"), method = "bootstrap")
    private static void addCustomItemRenderer(CallbackInfo ci) {
        ID_MAPPER.put(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "globe"), GlobeItemRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "rover"), RoverItemRenderer.Unbaked.MAP_CODEC);
    }
}
