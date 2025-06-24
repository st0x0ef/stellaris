package com.st0x0ef.stellaris.client.renderers.entities.vehicle.lander;

import com.st0x0ef.stellaris.client.renderers.entities.vehicle.VehicleRenderer;
import com.st0x0ef.stellaris.common.entities.vehicles.LanderEntity;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LanderRenderer extends VehicleRenderer<LanderEntity, LanderModel<LanderEntity>> {

    public static final ResourceLocation TEXTURE = ResourceLocationUtils.texture("vehicle/lander");

    public LanderRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new LanderModel<>(renderManagerIn.bakeLayer(LanderModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(LanderEntity entity) {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(LanderEntity entity, Frustum frustum, double x, double y, double z) {
        return frustum.isVisible(entity.getBoundingBox().inflate(3));
    }
}