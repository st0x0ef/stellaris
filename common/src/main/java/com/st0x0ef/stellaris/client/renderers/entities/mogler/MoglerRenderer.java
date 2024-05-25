package com.st0x0ef.stellaris.client.renderers.entities.mogler;


import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.Mogler;
import com.st0x0ef.stellaris.common.entities.alien.Alien;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

@Environment(EnvType.CLIENT)
public class MoglerRenderer extends MobRenderer<Mogler, MoglerModel<Mogler>> {
    private static final ResourceLocation LAYER_LOCATION = new ResourceLocation(Stellaris.MODID,"textures/entity/mogler.png");

    public MoglerRenderer(EntityRendererProvider.Context context) {
        super(context, new MoglerModel<>(context.bakeLayer(MoglerModel.LAYER_LOCATION)), 0.7F);
    }

    public ResourceLocation getTextureLocation(Mogler mogler) {
        return LAYER_LOCATION;
    }

    protected boolean isShaking(Mogler mogler) {
        return super.isShaking(mogler) || mogler.isConverting();
    }
}