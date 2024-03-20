package com.st0x0ef.stellaris.client.renderers.entities.martianraptor;

import com.st0x0ef.stellaris.common.entities.MartianRaptor;
import com.st0x0ef.stellaris.Stellaris;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MartianRaptorRenderer extends MobRenderer<MartianRaptor, EntityModel<MartianRaptor>> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/entity/martian_raptor.png");

    public MartianRaptorRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new MartianRaptorModel<>(renderManagerIn.bakeLayer(MartianRaptorModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(MartianRaptor p_114482_) {
        return TEXTURE;
    }
}