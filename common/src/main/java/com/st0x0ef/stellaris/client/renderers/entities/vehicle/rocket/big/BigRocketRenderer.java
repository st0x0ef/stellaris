package com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.big;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.VehicleRenderState;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.AbstractVehicleRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.RocketModel;
import com.st0x0ef.stellaris.common.entities.vehicles.IVehicleEntity;
import com.st0x0ef.stellaris.common.entities.vehicles.RocketEntity;
import com.st0x0ef.stellaris.common.vehicle_upgrade.ModelUpgrade;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BigRocketRenderer extends AbstractVehicleRenderer<RocketEntity, VehicleRenderState, BigRocketModel> {
    public ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/vehicle/rocket_skin/big/standard.png");

    public BigRocketRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new BigRocketModel(renderManagerIn.bakeLayer(BigRocketModel.LAYER_LOCATION)), 0.5f);
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
    protected ResourceLocation getTextureLocation(IVehicleEntity entity) {
        RocketEntity rocket = (RocketEntity) entity;
        rocket.MODEL_UPGRADE = new ModelUpgrade(RocketModel.BIG);
        rocket.setModelData();
        return rocket.getFullSkinTexture();
    }

    @Override
    protected boolean isShaking(IVehicleEntity rocket) {
        return rocket.getEntityData().get(RocketEntity.ROCKET_START);
    }
}