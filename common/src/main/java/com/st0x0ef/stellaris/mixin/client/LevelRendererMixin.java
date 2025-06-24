package com.st0x0ef.stellaris.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.common.registry.EffectsRegistry;
import com.st0x0ef.stellaris.common.registry.SoundRegistry;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Unique
    private static final ResourceLocation stellaris$TEXTURE = ResourceLocationUtils.texture("environment/sandstorm");

    @Final
    @Shadow
    @Mutable
    private Minecraft minecraft;

    @Shadow
    @Nullable
    private ClientLevel level;

    @Inject(
            method = "renderSnowAndRain",
            at = @At(value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V",
                    shift = At.Shift.AFTER,
                    ordinal = 0
            )
    )
    public void renderSandStorm(LightTexture lightTexture, float partialTick, double camX, double camY, double camZ, CallbackInfo ci, @Local BlockPos.MutableBlockPos mutableBlockPos) {
        if (level != null) {
            if (level.getBiome(mutableBlockPos).is(TagRegistry.SANDSTORM_BIOMES_TAG)) {
                RenderSystem.setShaderTexture(0, stellaris$TEXTURE);

                LocalPlayer player = minecraft.player;
                if (player != null) {
                    if (Utils.entityHasBlockAbove(player, null, null)) {
                        player.addEffect(new MobEffectInstance(EffectsRegistry.getHolder(EffectsRegistry.SANDSTORM), 30));
                    }
                }
            }
        }
    }

    @ModifyExpressionValue(method = "tickRain", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;WEATHER_RAIN:Lnet/minecraft/sounds/SoundEvent;"))
    private SoundEvent replaceRainSound(SoundEvent original, @Local(ordinal = 1) BlockPos pos) {
        if (level != null && pos != null) {
            if (level.getBiome(pos).is(TagRegistry.SANDSTORM_BIOMES_TAG)) {
                return SoundRegistry.WIND_SOUND.get();
            }
        }
        return original;
    }
}
