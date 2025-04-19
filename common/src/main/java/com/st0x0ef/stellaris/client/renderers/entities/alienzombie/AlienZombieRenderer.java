package com.st0x0ef.stellaris.client.renderers.entities.alienzombie;

import com.st0x0ef.stellaris.common.entities.mobs.AlienZombie;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

import static com.st0x0ef.stellaris.Stellaris.texture;

@Environment(EnvType.CLIENT)
public class AlienZombieRenderer extends MobRenderer<AlienZombie, LivingEntityRenderState, AlienZombieModel> {

    public static final ResourceLocation TEXTURE = texture("entity/alien_zombie");

    public AlienZombieRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new AlienZombieModel(renderManagerIn.bakeLayer(AlienZombieModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState entity) {
        return TEXTURE;
    }
}