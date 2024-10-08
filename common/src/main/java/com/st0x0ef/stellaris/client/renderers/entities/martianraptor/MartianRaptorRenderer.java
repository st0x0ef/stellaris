package com.st0x0ef.stellaris.client.renderers.entities.martianraptor;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.mobs.MartianRaptor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class MartianRaptorRenderer extends MobRenderer<MartianRaptor, MartianRaptorModel<MartianRaptor>> {

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/entity/martian_raptor.png");

    public MartianRaptorRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new MartianRaptorModel<>(renderManagerIn.bakeLayer(MartianRaptorModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(MartianRaptor p_114482_) {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(MartianRaptor livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return livingEntity != null && camera.isVisible(livingEntity.getBoundingBoxForCulling());
    }
}