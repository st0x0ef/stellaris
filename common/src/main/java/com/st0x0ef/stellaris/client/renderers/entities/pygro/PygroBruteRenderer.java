package com.st0x0ef.stellaris.client.renderers.entities.pygro;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.PygroBrute;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class PygroBruteRenderer extends HumanoidMobRenderer<PygroBrute, PygroModel<PygroBrute>> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/entity/pygro_brute.png");

    public PygroBruteRenderer(EntityRendererProvider.Context context) {
        super(context, new PygroModel<>(context.bakeLayer(PygroModel.LAYER_LOCATION)), 0.5f);
    }

    private static PygroModel<Mob> createModel(EntityModelSet p_174350_, ModelLayerLocation p_174351_) {

        return new PygroModel<>(p_174350_.bakeLayer(p_174351_));
    }

    public ResourceLocation getTextureLocation(PygroBrute p_115708_) {
        return TEXTURE;
    }

    protected boolean isShaking(PygroBrute p_115712_) {
        return super.isShaking(p_115712_) || p_115712_ != null && p_115712_.isConverting();
    }

    @Override
    public boolean shouldRender(PygroBrute livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return livingEntity != null && camera.isVisible(livingEntity.getBoundingBoxForCulling());
    }
}