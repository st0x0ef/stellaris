package com.st0x0ef.stellaris.mixin.client;

import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevelRenderer.class)
public class SandStormMixin {
/*
    @Final
    @Shadow
    @Mutable
    private Minecraft minecraft;

    @Inject(
            method = "renderSnowAndRain",
            at = @At(value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V",
                    shift = At.Shift.AFTER,
                    ordinal = 0
            )
    )
    public void renderSandStorm(LightTexture lightTexture, float partialTick, double camX, double camY, double camZ, CallbackInfo ci, @Local BlockPos.MutableBlockPos mutableBlockPos) {
        if(this.minecraft.level != null) {
            Level level = this.minecraft.level;
            if (level.getBiome(mutableBlockPos).is(TagRegistry.SANDSTORM_BIOMES_TAG)) {
                RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/environment/sandstorm.png"));

                if(Utils.entityHasBlockAbove(this.minecraft.player, null, null)) {
                    this.minecraft.player.addEffect(new MobEffectInstance(EffectsRegistry.SANDSTORM, 30));

                }
            }
        }
    }

    @WrapOperation(
            method = "tickRain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V")
    )
    private void bypassExpensiveCalculationIfNecessary(ClientLevel instance, BlockPos blockPos, SoundEvent soundEvent, SoundSource soundSource, float rainLevel, float v, boolean rainSoundTime, Operation<Void> original) {
        if(this.minecraft.level != null) {
            Level level = this.minecraft.level;
            if (level.getBiome(blockPos).is(TagRegistry.SANDSTORM_BIOMES_TAG)) {
                this.minecraft.level.playLocalSound(blockPos, SoundRegistry.WIND_SOUND.get(), SoundSource.WEATHER, 0.1F, 0.5F, false);

            } else {
                original.call(instance, blockPos, soundEvent, soundSource, rainLevel, v, rainSoundTime);
            }
        }

    }
*/ // TODO : rewrite that
}
