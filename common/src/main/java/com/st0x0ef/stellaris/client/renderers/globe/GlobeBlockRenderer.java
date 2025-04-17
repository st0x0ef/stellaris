package com.st0x0ef.stellaris.client.renderers.globe;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.st0x0ef.stellaris.common.blocks.GlobeBlock;
import com.st0x0ef.stellaris.common.blocks.entities.GlobeBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class GlobeBlockRenderer implements BlockEntityRenderer<GlobeBlockEntity> {
    private final GlobeModel model;

    public GlobeBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new GlobeModel(context.bakeLayer(GlobeModel.LAYER_LOCATION));
    }

    @Override
    public void render(GlobeBlockEntity blockEntity, float particleTicks, PoseStack matrixStackIn, MultiBufferSource buffer, int combinedLight, int overlay, Vec3 vec3) {
        if (blockEntity.getLevel() == null) return;

        BlockState state = blockEntity.getBlockState();

        if (!(state.getBlock() instanceof GlobeBlock)) return;

        Minecraft mc = Minecraft.getInstance();
        Direction direction = state.getValue(GlobeBlock.FACING);

        matrixStackIn.pushPose();

        matrixStackIn.translate(0.5D, 1.5D, 0.5D);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(direction.toYRot()));

        /** Animation */
        this.model.setupAnim(blockEntity, particleTicks);

        VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.entityTranslucent(((GlobeBlock) state.getBlock()).texture));

        this.model.renderToBuffer(matrixStackIn, vertexBuilder, combinedLight, OverlayTexture.NO_OVERLAY, -1);
        mc.renderBuffers().bufferSource().endBatch();

        matrixStackIn.popPose();
    }
}