package com.st0x0ef.stellaris.client.renderers.entities.starcrawler;

import com.st0x0ef.stellaris.common.entities.mobs.StarCrawler;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class StarCrawlerRenderer extends MobRenderer<StarCrawler, StarCrawlerModel<StarCrawler>> {

    public static final ResourceLocation TEXTURE = ResourceLocationUtils.texture("entity/star_crawler");

    public StarCrawlerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new StarCrawlerModel<>(renderManagerIn.bakeLayer(StarCrawlerModel.LAYER_LOCATION)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(StarCrawler p_114482_) {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(StarCrawler livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return livingEntity != null && camera.isVisible(livingEntity.getBoundingBoxForCulling());
    }
}