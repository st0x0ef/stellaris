package com.st0x0ef.stellaris.client.renderers.entities.vehicle.lander;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.VehicleRenderState;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.AbstractVehicleRenderer;
import com.st0x0ef.stellaris.common.entities.vehicles.IVehicleEntity;
import com.st0x0ef.stellaris.common.entities.vehicles.LanderEntity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LanderRenderer extends AbstractVehicleRenderer<LanderEntity, VehicleRenderState, LanderModel> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/vehicle/lander.png");

    public LanderRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new LanderModel(renderManagerIn.bakeLayer(LanderModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(IVehicleEntity entity) {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(LanderEntity entity, Frustum frustum, double x, double y, double z) {
        return frustum.isVisible(entity.getBoundingBox().inflate(3));
    }

    @Override
    public VehicleRenderState createRenderState() {
        return new VehicleRenderState();
    }

    @Override
    public void extractRenderState(LanderEntity entity, VehicleRenderState entityRenderState, float f) {
        super.extractRenderState(entity, entityRenderState, f);
        entityRenderState.entity = entity;
        entityRenderState.partialTick = f;
    }
}