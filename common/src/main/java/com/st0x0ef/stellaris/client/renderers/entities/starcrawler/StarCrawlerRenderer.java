package com.st0x0ef.stellaris.client.renderers.entities.starcrawler;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.StarCrawler;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class StarCrawlerRenderer extends MobRenderer<StarCrawler, StarCrawlerModel<StarCrawler>> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/entity/star_crawler.png");

    public StarCrawlerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new StarCrawlerModel<>(renderManagerIn.bakeLayer(StarCrawlerModel.LAYER_LOCATION)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(StarCrawler p_114482_) {
        return TEXTURE;
    }
}