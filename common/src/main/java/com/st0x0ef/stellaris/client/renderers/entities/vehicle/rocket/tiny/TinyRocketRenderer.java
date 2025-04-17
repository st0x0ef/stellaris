package com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.tiny;

import com.st0x0ef.stellaris.client.renderers.entities.vehicle.VehicleRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.RocketModel;
import com.st0x0ef.stellaris.common.entities.vehicles.RocketEntity;
import com.st0x0ef.stellaris.common.vehicle_upgrade.ModelUpgrade;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import static com.st0x0ef.stellaris.Stellaris.texture;

public class TinyRocketRenderer extends VehicleRenderer<RocketEntity, TinyRocketModel<RocketEntity>> {
    public ResourceLocation TEXTURE = texture("vehicle/rocket_skin/tiny/standard");

    public TinyRocketRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new TinyRocketModel<>(renderManagerIn.bakeLayer(TinyRocketModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(RocketEntity rocket) {
        rocket.MODEL_UPGRADE = new ModelUpgrade(RocketModel.TINY);
        return rocket.getFullSkinTexture();
    }

    @Override
    protected boolean isShaking(RocketEntity rocket) {
        return rocket.getEntityData().get(RocketEntity.ROCKET_START);
    }
}