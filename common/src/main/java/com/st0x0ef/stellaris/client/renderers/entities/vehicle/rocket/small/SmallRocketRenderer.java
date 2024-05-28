package com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.small;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.VehicleRenderer;
import com.st0x0ef.stellaris.common.entities.RocketEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;


public class SmallRocketRenderer extends VehicleRenderer<RocketEntity, SmallRocketModel<RocketEntity>> {
    public ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/vehicle/rocket_skin/small/standard.png");

    public SmallRocketRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new SmallRocketModel<>(renderManagerIn.bakeLayer(SmallRocketModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(RocketEntity rocket) {
        return new ResourceLocation(rocket.getFullSkinTexture());
    }

    @Override
    protected boolean isShaking(RocketEntity rocket) {
        return rocket.getEntityData().get(RocketEntity.ROCKET_START);
    }
}