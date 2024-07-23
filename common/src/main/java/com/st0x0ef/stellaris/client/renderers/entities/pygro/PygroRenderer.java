package com.st0x0ef.stellaris.client.renderers.entities.pygro;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.pygro.Pygro;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class PygroRenderer extends HumanoidMobRenderer<Pygro, PygroModel<Pygro>> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/entity/pygro.png");

    public PygroRenderer(EntityRendererProvider.Context context) {
        super(context, new PygroModel<>(context.bakeLayer(PygroModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Pygro entity) {
        return TEXTURE;
    }


    private static PygroModel<Mob> createModel(EntityModelSet p_174350_, ModelLayerLocation p_174351_) {
        return new PygroModel<>(p_174350_.bakeLayer(p_174351_));
    }

    protected boolean isShaking(Pygro p_115712_) {
        return super.isShaking(p_115712_) || p_115712_ != null && p_115712_.isConverting();
    }
}