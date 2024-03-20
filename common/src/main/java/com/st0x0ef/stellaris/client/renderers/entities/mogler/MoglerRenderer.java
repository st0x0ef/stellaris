package com.st0x0ef.stellaris.client.renderers.entities.mogler;


import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.Mogler;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;


public class MoglerRenderer extends MobRenderer<Mogler, EntityModel<Mogler>> {
    private static final ResourceLocation LAYER_LOCATION = new ResourceLocation(Stellaris.MODID,"textures/entity/mogler.png");

    public MoglerRenderer(EntityRendererProvider.Context p_174165_) {
        super(p_174165_, new MoglerModel<>(p_174165_.bakeLayer(MoglerModel.LAYER_LOCATION)), 0.7F);
    }

    public ResourceLocation getTextureLocation(Mogler p_114862_) {
        return LAYER_LOCATION;
    }

    protected boolean isShaking(Mogler p_114864_) {
        return super.isShaking(p_114864_) || p_114864_.isConverting();
    }
}