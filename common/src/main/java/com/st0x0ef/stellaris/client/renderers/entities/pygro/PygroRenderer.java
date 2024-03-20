package com.st0x0ef.stellaris.client.renderers.entities.pygro;

import com.st0x0ef.stellaris.Stellaris;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;

public class PygroRenderer extends HumanoidMobRenderer<Mob, PygroModel<Mob>> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/entity/pygro.png");

    public PygroRenderer(EntityRendererProvider.Context context) {
        super(context, new PygroModel<>(context.bakeLayer(PygroModel.LAYER_LOCATION)), 0.5f);
    }


    private static PygroModel<Mob> createModel(EntityModelSet p_174350_, ModelLayerLocation p_174351_) {

        return new PygroModel<>(p_174350_.bakeLayer(p_174351_));
    }

    public ResourceLocation getTextureLocation(Mob p_115708_) {
        return TEXTURE;
    }

    protected boolean isShaking(Mob p_115712_) {
        return super.isShaking(p_115712_) || p_115712_ instanceof AbstractPiglin && ((AbstractPiglin)p_115712_).isConverting();
    }
}