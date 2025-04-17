package com.st0x0ef.stellaris.client.renderers.entities.vehicle;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.st0x0ef.stellaris.common.entities.vehicles.IVehicleEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class AbstractVehicleRenderer<T extends Entity, S extends VehicleRenderState, M extends EntityModel<S>> extends EntityRenderer<T, S> implements RenderLayerParent<S, M> {
    protected final M model;
    protected final List<RenderLayer<S, M>> layers = Lists.newArrayList();

    public AbstractVehicleRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context);
        this.model = model;
        this.shadowRadius = shadowRadius;
    }

    public M getModel() {
        return this.model;
    }

    @Override
    public void render(S renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        boolean shouldSit = renderState.entity.isPassenger() && (renderState.entity.getVehicle() != null);
        //this.model.riding = shouldSit;
        float f = Mth.rotLerp(renderState.partialTick, renderState.entity.yRotO, renderState.entity.getYRot());
        float f1 = Mth.rotLerp(renderState.partialTick, renderState.entity.yRotO, renderState.entity.getYRot());
        float f2 = f1 - f;
        if (shouldSit && renderState.entity.getVehicle() instanceof LivingEntity livingentity) {
            f = Mth.rotLerp(renderState.partialTick, livingentity.yBodyRotO, livingentity.yBodyRot);
            f2 = f1 - f;
            float f3 = Mth.wrapDegrees(f2);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            f2 = f1 - f;
        }

        float f6 = Mth.lerp(renderState.partialTick, renderState.entity.xRotO, renderState.entity.getXRot());

        float f7 = this.getBob(renderState.entity, renderState.partialTick);
        this.setupRotations(renderState.entity, poseStack, f, renderState.partialTick);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0D, -1.501F, 0.0D);
        float f8 = 0.0F;
        float f5 = 0.0F;


        //this.model.prepareMobModel(renderState.entity, f5, f8, renderState.ageInTicks);
        this.model.setupAnim(renderState);
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = this.isBodyVisible(renderState.entity);
        boolean flag1 = !flag && !renderState.entity.isInvisibleTo(minecraft.player);
        boolean flag2 = minecraft.shouldEntityAppearGlowing(renderState.entity);
        RenderType rendertype = this.getRenderType(renderState.entity, flag, flag1, flag2);
        if (rendertype != null) {
            VertexConsumer vertexconsumer = bufferSource.getBuffer(rendertype);
            int i = getOverlayCoords(this.getWhiteOverlayProgress());
            this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, i, -1);
        }

        if (!renderState.entity.isSpectator()) {
            for(RenderLayer<S, M> renderlayer : this.layers) {
                renderlayer.render(poseStack, bufferSource, packedLight, renderState, f2, f6);
            }
        }

        poseStack.popPose();
        super.render(renderState, poseStack, bufferSource, packedLight);
    }


    @Nullable
    protected RenderType getRenderType(IVehicleEntity entity, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
        ResourceLocation resourcelocation = this.getTextureLocation(entity);
        if (p_115324_) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (p_115323_) {
            return this.model.renderType(resourcelocation);
        } else {
            return p_115325_ ? RenderType.outline(resourcelocation) : null;
        }
    }

    protected boolean isBodyVisible(IVehicleEntity entity) {
        return !entity.isInvisible();
    }

    protected boolean isShaking(IVehicleEntity entity) {
        return false;
    }

    public static int getOverlayCoords(float p_115340_) {
        return OverlayTexture.pack(OverlayTexture.u(p_115340_), OverlayTexture.v(false));
    }

    protected float getWhiteOverlayProgress() {
        return 0.0F;
    }

    protected void setupRotations(IVehicleEntity entity, PoseStack poseStack, float p_115320_, float p_115321_) {
        if (this.isShaking(entity)) {
            if (!Minecraft.getInstance().isPaused()) {
                double shakeDirection1 = (p_115321_ * (entity.level().random.nextBoolean() ? 1 : -1)) / 50;
                double shakeDirection2 = (p_115321_ * (entity.level().random.nextBoolean() ? 1 : -1)) / 50;
                double shakeDirection3 = (p_115321_ * (entity.level().random.nextBoolean() ? 1 : -1)) / 50;
                poseStack.translate(shakeDirection1, shakeDirection2, shakeDirection3);
            }
        }
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - p_115320_));
    }

    protected float getBob(IVehicleEntity entity, float tick) {
        return (float)entity.tickCount + tick;
    }

    protected ResourceLocation getTextureLocation(IVehicleEntity rocket) {
        return null;
    }
}