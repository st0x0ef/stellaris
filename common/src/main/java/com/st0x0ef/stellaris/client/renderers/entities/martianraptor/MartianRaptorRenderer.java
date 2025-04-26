package com.st0x0ef.stellaris.client.renderers.entities.martianraptor;

import com.st0x0ef.stellaris.common.entities.mobs.MartianRaptor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import static com.st0x0ef.stellaris.Stellaris.texture;

@Environment(EnvType.CLIENT)
public class MartianRaptorRenderer extends MobRenderer<MartianRaptor, MartianRaptorRenderState, MartianRaptorModel> {

    public static final ResourceLocation TEXTURE = texture("entity/martian_raptor");

    public MartianRaptorRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new MartianRaptorModel(renderManagerIn.bakeLayer(MartianRaptorModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public MartianRaptorRenderState createRenderState() {
        return new MartianRaptorRenderState();
    }

    @Override
    public void extractRenderState(MartianRaptor livingEntity, MartianRaptorRenderState state, float f) {
        super.extractRenderState(livingEntity, state, f);
        state.attackAnim = livingEntity.getAttackAnim();
    }

    @Override
    public ResourceLocation getTextureLocation(MartianRaptorRenderState state) {
        return TEXTURE;
    }
}