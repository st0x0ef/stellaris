package com.st0x0ef.stellaris.client.renderers.globe;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.st0x0ef.stellaris.common.blocks.GlobeBlock;
import com.st0x0ef.stellaris.common.blocks.entities.GlobeTileEntity;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class GlobeBlockRenderer<T extends GlobeTileEntity> implements BlockEntityRenderer<GlobeTileEntity> {

    private static ItemStack stack1 = new ItemStack(ItemsRegistry.EARTH_GLOBE_ITEM);
    private static ItemStack stack3 = new ItemStack(ItemsRegistry.MARS_GLOBE_ITEM);
    private static ItemStack stack4 = new ItemStack(ItemsRegistry.MOON_GLOBE_ITEM);
    private static ItemStack stack5 = new ItemStack(ItemsRegistry.MERCURY_GLOBE_ITEM);
    private static ItemStack stack6 = new ItemStack(ItemsRegistry.VENUS_GLOBE_ITEM);
    private GlobeModel<?> model;

    public GlobeBlockRenderer(BlockEntityRendererProvider.Context Context) {}

    @Override
    public void render(GlobeTileEntity tileEntity, float particleTicks, PoseStack matrixStackIn, MultiBufferSource buffer, int combinedLight, int overlay) {
        BlockState state = tileEntity.getLevel().getBlockState(tileEntity.getBlockPos());

        if (!(state.getBlock() instanceof GlobeBlock)) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        BlockState blockstate = tileEntity.getBlockState();
        Direction direction = blockstate.getValue(GlobeBlock.FACING);

        matrixStackIn.pushPose();

        matrixStackIn.translate(0.5D, 1.5D, 0.5D);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(direction.toYRot()));

        if (this.model == null) {
            this.model = new GlobeModel<>(mc.getEntityModels().bakeLayer(GlobeModel.LAYER_LOCATION));
        }


        /** Animation */
        this.model.setupAnim(tileEntity, particleTicks);

        VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.entityTranslucent(((GlobeBlock) state.getBlock()).texture));

        this.model.renderToBuffer(matrixStackIn, vertexBuilder, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderBuffers().bufferSource().endBatch();

        matrixStackIn.popPose();
    }

}