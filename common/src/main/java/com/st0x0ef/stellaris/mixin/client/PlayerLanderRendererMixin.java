package com.st0x0ef.stellaris.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.LanderEntity;
import com.st0x0ef.stellaris.common.entities.RocketEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerLanderRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    @Unique
    private LivingEntityRenderer entityRenderer = (LivingEntityRenderer) ((Object) this);


    public PlayerLanderRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(at = @At(value = "HEAD"), method = "render", cancellable = true)
    private void render(AbstractClientPlayer entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo info) {
        PlayerModel<AbstractClientPlayer> playerModel = (PlayerModel)entityRenderer.getModel();


        if(entity.getVehicle() instanceof LanderEntity) {
            info.cancel();
        } else if(entity.getVehicle() instanceof RocketEntity) {
           // playerModel.setAllVisible(false);
            playerModel.riding = false;
            entity.setPose(Pose.STANDING);

        }
    }
}
