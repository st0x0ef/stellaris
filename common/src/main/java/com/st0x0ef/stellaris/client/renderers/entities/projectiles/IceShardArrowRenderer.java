package com.st0x0ef.stellaris.client.renderers.entities.projectiles;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.IceShardArrowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class IceShardArrowRenderer extends ArrowRenderer<IceShardArrowEntity> {
    private static final ResourceLocation LAYER_LOCATION = new ResourceLocation(Stellaris.MODID,"textures/entity/ice_shard_arrow.png");

    public IceShardArrowRenderer(EntityRendererProvider.Context p_174165_) {
        super(p_174165_);
    }

    @Override
    public ResourceLocation getTextureLocation(IceShardArrowEntity entity) {
        return LAYER_LOCATION;
    }

}
