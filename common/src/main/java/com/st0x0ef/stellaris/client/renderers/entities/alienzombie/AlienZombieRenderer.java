package com.st0x0ef.stellaris.client.renderers.entities.alienzombie;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.AlienZombie;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AlienZombieRenderer extends MobRenderer<AlienZombie, EntityModel<AlienZombie>> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/entity/alien_zombie.png");

    public AlienZombieRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new AlienZombieModel<>(renderManagerIn.bakeLayer(AlienZombieModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(AlienZombie entity) {
        return TEXTURE;
    }
}