package com.st0x0ef.stellaris.client.renderers.entities.projectiles;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.renderers.entities.mogler.MoglerModel;
import com.st0x0ef.stellaris.common.entities.IceShardArrowEntity;
import com.st0x0ef.stellaris.common.entities.Mogler;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
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
