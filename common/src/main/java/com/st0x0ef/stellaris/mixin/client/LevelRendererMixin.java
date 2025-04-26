package com.st0x0ef.stellaris.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

import static com.st0x0ef.stellaris.Stellaris.texture;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Unique
    private static final ResourceLocation stellaris$TEXTURE = texture("environment/sandstorm");

    @Final
    @Shadow
    @Mutable
    private Minecraft minecraft;

    @Shadow
    @Nullable
    private ClientLevel level;

    /*@Inject(
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
    }*/
}
