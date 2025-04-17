package com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.small;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.VehicleRenderState;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.AbstractVehicleRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.RocketModel;
import com.st0x0ef.stellaris.common.entities.vehicles.IVehicleEntity;
import com.st0x0ef.stellaris.common.entities.vehicles.RocketEntity;
import com.st0x0ef.stellaris.common.vehicle_upgrade.ModelUpgrade;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;


public class SmallRocketRenderer extends AbstractVehicleRenderer<RocketEntity, VehicleRenderState, SmallRocketModel> {
    public ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/vehicle/rocket_skin/small/standard.png");

    public SmallRocketRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new SmallRocketModel(renderManagerIn.bakeLayer(SmallRocketModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public VehicleRenderState createRenderState() {
        return new VehicleRenderState();
    }

    @Override
    public void extractRenderState(RocketEntity entity, VehicleRenderState entityRenderState, float f) {
        super.extractRenderState(entity, entityRenderState, f);
        entityRenderState.entity = entity;
        entityRenderState.partialTick = f;
    }


    @Override
    public ResourceLocation getTextureLocation(IVehicleEntity entity) {
        RocketEntity rocket = (RocketEntity) entity;
        rocket.MODEL_UPGRADE = new ModelUpgrade(RocketModel.SMALL);
        return rocket.getFullSkinTexture();
    }

    @Override
    protected boolean isShaking(IVehicleEntity rocket) {
        return rocket.getEntityData().get(RocketEntity.ROCKET_START);
    }
}