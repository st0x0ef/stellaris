package com.st0x0ef.stellaris.client.renderers.globe;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.items.GlobeItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record GlobeItemRenderer(ResourceLocation texture, GlobeModel model) implements SpecialModelRenderer<ResourceLocation> {
    @Override
    public void render(ResourceLocation texture, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        poseStack.pushPose();

        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;

        VertexConsumer vertexBuilder = bufferSource.getBuffer(RenderType.entityCutoutNoCullZOffset(texture));

        /** Animation */
        if (level != null) {
            if (!mc.isPaused() && mc.getFps()>0) {
                model.globe.getChild("planet").yRot = (float) (level.getGameTime() + (1 / mc.getFps())) / -20;
            }
        }

        this.model.renderToBuffer(poseStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, -1);

        poseStack.popPose();
    }

    @Override
    public @Nullable ResourceLocation extractArgument(ItemStack itemStack) {
        if (itemStack.getItem() instanceof GlobeItem item) return item.getTexture();
        return texture;
    }

    public record Unbaked(ResourceLocation texture) implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        ResourceLocation.CODEC.fieldOf("texture").forGetter(GlobeItemRenderer.Unbaked::texture)
                ).apply(instance, GlobeItemRenderer.Unbaked::new)
        );

        @Override
        public @NotNull SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new GlobeItemRenderer(
                    this.texture,
                    new GlobeModel(modelSet.bakeLayer(GlobeModel.LAYER_LOCATION))
            );
        }

        @Override
        public MapCodec<GlobeItemRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}