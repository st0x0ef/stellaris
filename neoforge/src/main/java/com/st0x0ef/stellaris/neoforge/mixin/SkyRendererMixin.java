package com.st0x0ef.stellaris.neoforge.mixin;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.platform.PlatformClientUtilsHelper;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionSpecialEffects.class)
public class SkyRendererMixin {

    @Inject(method = "forType", at = @At("HEAD"), cancellable = true)
    private static void stellaris$forType(DimensionType type, CallbackInfoReturnable<DimensionSpecialEffects> cir) {
        ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, type.effectsLocation());
        if (PlatformClientUtilsHelper.DIMENSION_RENDERERS.containsKey(dimension)) {
            Stellaris.LOG.error("Uee");
            cir.setReturnValue(PlatformClientUtilsHelper.DIMENSION_RENDERERS.get(dimension));
        }
    }

}
