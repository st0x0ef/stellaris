package com.st0x0ef.stellaris.neoforge.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.skys.record.SkyPropertiesData;
import com.st0x0ef.stellaris.common.items.CustomArmorItem;
import com.st0x0ef.stellaris.platform.neoforge.ClientUtilsPlatformImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin  {

    @Mutable
    @Shadow
    private ClientLevel level;

    @Inject(method = "renderClouds", at = @At(value = "HEAD"), cancellable = true)
    private void canCloudRenderer(PoseStack poseStack, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, double camX, double camY, double camZ, CallbackInfo ci) {
        if (SkyPropertiesData.SKY_PROPERTIES.containsKey(level.dimension())) {
            if(!SkyPropertiesData.SKY_PROPERTIES.get(level.dimension()).hasCloud()) {
                ci.cancel();
            }
        }
    }

}
