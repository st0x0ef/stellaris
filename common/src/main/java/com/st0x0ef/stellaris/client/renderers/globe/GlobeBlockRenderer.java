package com.st0x0ef.stellaris.client.renderers.globe;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.st0x0ef.stellaris.common.blocks.GlobeBlock;
import com.st0x0ef.stellaris.common.blocks.entities.GlobeBlockEntity;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class GlobeBlockRenderer<T extends GlobeBlockEntity> implements BlockEntityRenderer<GlobeBlockEntity> {

    public static final ResourceLocation EARTH_GLOBE_TEXTURE = ResourceLocationUtils.texture("block/globes/earth_globe");
    public static final ResourceLocation MOON_GLOBE_TEXTURE = ResourceLocationUtils.texture("block/globes/moon_globe");
    public static final ResourceLocation MARS_GLOBE_TEXTURE = ResourceLocationUtils.texture("block/globes/mars_globe");
    public static final ResourceLocation MERCURY_GLOBE_TEXTURE = ResourceLocationUtils.texture("block/globes/mercury_globe");
    public static final ResourceLocation VENUS_GLOBE_TEXTURE = ResourceLocationUtils.texture("block/globes/venus_globe");
    private GlobeModel<?> model;

    public GlobeBlockRenderer(BlockEntityRendererProvider.Context Context) {
    }

    @Override
    public void render(GlobeBlockEntity tileEntity, float particleTicks, PoseStack matrixStackIn, MultiBufferSource buffer, int combinedLight, int overlay) {
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

        ResourceLocation texture = getGlobeTexture(state.getBlock());
        if (texture == null) {
            return;
        }

        VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.entityTranslucent(texture));

        this.model.renderToBuffer(matrixStackIn, vertexBuilder, combinedLight, OverlayTexture.NO_OVERLAY, -1);
        mc.renderBuffers().bufferSource().endBatch();

        matrixStackIn.popPose();
    }

    public static ResourceLocation getGlobeTexture(ItemLike itemLike) {
        Item item = itemLike.asItem();
        if (item == ItemsRegistry.EARTH_GLOBE_ITEM.get()) {
            return EARTH_GLOBE_TEXTURE;
        }
        else if (item == ItemsRegistry.MOON_GLOBE_ITEM.get()) {
            return MOON_GLOBE_TEXTURE;
        }
        else if (item == ItemsRegistry.MARS_GLOBE_ITEM.get()) {
            return MARS_GLOBE_TEXTURE;
        }
        else if (item == ItemsRegistry.MERCURY_GLOBE_ITEM.get()) {
            return MERCURY_GLOBE_TEXTURE;
        }
        else if (item == ItemsRegistry.VENUS_GLOBE_ITEM.get()) {
            return VENUS_GLOBE_TEXTURE;
        }
        return null;
    }
}