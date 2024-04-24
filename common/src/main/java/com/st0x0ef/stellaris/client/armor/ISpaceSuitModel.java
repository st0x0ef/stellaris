package com.st0x0ef.stellaris.client.armor;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public abstract class ISpaceSuitModel<T extends LivingEntity> extends HumanoidModel<T>
{
    private final LivingEntity entity;
    private final ItemStack itemStack;

    public ISpaceSuitModel(LivingEntity entity, ItemStack itemStack) {
        super(new EntityRendererProvider.Context(Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer(), Minecraft.getInstance().getBlockRenderer(), Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer(), Minecraft.getInstance().getResourceManager(), Minecraft.getInstance().getEntityModels(), Minecraft.getInstance().font).bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        this.entity = entity;
        this.itemStack = itemStack;
    }

    public VertexConsumer getVertex(RenderType renderType, boolean p_115187_, boolean p_115188_) {
        MultiBufferSource p_115185_ = Minecraft.getInstance().renderBuffers().bufferSource();
        return p_115188_ ? VertexMultiConsumer.create(p_115185_.getBuffer(p_115187_ ? RenderType.armorGlint() : RenderType.armorEntityGlint()), p_115185_.getBuffer(renderType)) : p_115185_.getBuffer(renderType);
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }
}
