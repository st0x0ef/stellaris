package com.st0x0ef.stellaris.client.renderers.entities.vehicle.rover;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public record RoverItemRenderer(ResourceLocation texture, RoverModel model) implements NoDataSpecialModelRenderer {
    @Override
    public void render(ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        poseStack.pushPose();

        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        VertexConsumer vertexBuilder = bufferSource.getBuffer(RenderType.entityCutoutNoCullZOffset(RoverRenderer.TEXTURE));

        this.model.renderToBuffer(poseStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, -1);

        poseStack.popPose();
    }

    public record Unbaked(ResourceLocation texture) implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<RoverItemRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        ResourceLocation.CODEC.fieldOf("texture").forGetter(RoverItemRenderer.Unbaked::texture)
                ).apply(instance, RoverItemRenderer.Unbaked::new)
        );

        @Override
        public @NotNull NoDataSpecialModelRenderer bake(EntityModelSet modelSet) {
            return new RoverItemRenderer(
                    this.texture,
                    new RoverModel(modelSet.bakeLayer(RoverModel.LAYER_LOCATION))
            );
        }

        @Override
        public MapCodec<RoverItemRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}