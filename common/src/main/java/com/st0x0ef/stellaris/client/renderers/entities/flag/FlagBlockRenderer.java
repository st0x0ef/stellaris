package com.st0x0ef.stellaris.client.renderers.entities.flag;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.FlagBlock;
import com.st0x0ef.stellaris.common.blocks.entities.FlagBlockEntity;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector4d;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FlagBlockRenderer implements BlockEntityRenderer<FlagBlockEntity> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocationUtils.id("flag"), "main");

    private final ModelPart flag;
    private final ModelPart pole;
    private final ModelPart base;

    private final Map<SkullBlock.Type, FlagHeadModel> MODELS;

    public FlagBlockRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelPart = context.bakeLayer(LAYER_LOCATION);
        this.flag = modelPart.getChild("flag");
        this.pole = modelPart.getChild("pole");
        this.base = modelPart.getChild("base");
        this.MODELS = createModels();
    }

    public static Map<SkullBlock.Type, FlagHeadModel> createModels() {
        Map<SkullBlock.Type, FlagHeadModel> models = Maps.newHashMap();
        Minecraft minecraft = Minecraft.getInstance();
        Map<String, ModelPart> map = Map.of("head", new FlagHeadModel(minecraft.getEntityModels().bakeLayer(FlagHeadModel.LAYER_LOCATION)).head);
        ModelPart modelPart = new ModelPart(Collections.emptyList(), map);

        FlagHeadModel genericheadmodel = new FlagHeadModel(modelPart);

        models.put(SkullBlock.Types.PLAYER, genericheadmodel);
        return models;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 35).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 4).addBox(-5.0F, -2.0F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(40, 16).addBox(-2.0F, -11.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition pole = partdefinition.addOrReplaceChild("pole", CubeListBuilder.create().texOffs(10, 0).addBox(-22.0F, -46.0F, -1.0F, 23.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 35).addBox(-2.0F, -48.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 22).addBox(-2.0F, -35.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 4).addBox(-1.0F, -45.0F, -1.0F, 2.0F, 44.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition flag = partdefinition.addOrReplaceChild("flag", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition flag_content_r1 = flag.addOrReplaceChild("flag_content   ", CubeListBuilder.create().texOffs(43, 36).addBox(0.0F, -20.0F, -1.0F, 10.0F, 20.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(5, 4).addBox(0.0F, -20.0F, -0.5F, 10.0F, 20.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-21.0F, -44.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void render(FlagBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        boolean bl = blockEntity.getLevel() == null;
        Direction direction = blockEntity.getBlockState().getValue(FlagBlock.FACING);

        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("stellaris", "textures/block/flag/flag_" + blockEntity.getColor().getName() + ".png");
        poseStack.pushPose();

        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        poseStack.translate(-0.5D, -1.5, 0.5D);
        this.base.render(poseStack, bufferSource.getBuffer(RenderType.entityCutout(texture)), packedLight, packedOverlay);





        if(direction == Direction.WEST || direction == Direction.EAST) {
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
        }
        this.flag.render(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCullZOffset(texture)), packedLight, packedOverlay);
        this.pole.render(poseStack, bufferSource.getBuffer(RenderType.entityCutout(texture)), packedLight, packedOverlay);

        Map<Direction, Vector4d> rotationMap = Map.of(
                Direction.NORTH, new Vector4d(-180, 1.13D, 2.05D, 0.7D),
                Direction.SOUTH, new Vector4d(-180, 1.13D, 2.05D, 0.8D),

                Direction.WEST, new Vector4d(90, 0.7D, 2D, 1.2D),
                Direction.EAST, new Vector4d(90, 0.8D, 2D, 1.2D)

        );

        poseStack.popPose();

        for(Direction dir : List.of(direction.getOpposite(), direction)) {
            poseStack.pushPose();

            Vector4d directionInfo = rotationMap.get(dir);
            poseStack.translate(directionInfo.y, directionInfo.z, directionInfo.w);

            var testProfile = new ResolvableProfile(new GameProfile(UUID.fromString("fe40f09c-fdaa-497f-8e2b-bed31180bfbd"), "TATHAN_06"));
            RenderType renderType = SkullBlockRenderer.getRenderType(SkullBlock.Types.PLAYER, testProfile);

            renderSkull((float) directionInfo.x, 0.0F, poseStack, bufferSource, packedLight, SkullBlock.Types.PLAYER, renderType);

            poseStack.popPose();
        }

    }

    public void renderSkull(float yRot, float mouthAnimation, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, SkullBlock.Type type, RenderType renderType) {
        FlagHeadModel genericheadmodel = this.MODELS.get(type);

        poseStack.pushPose();

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

        genericheadmodel.setupAnim(mouthAnimation, yRot, 0.0F);
        genericheadmodel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
        poseStack.popPose();
    }

    public boolean shouldRenderOffScreen(FlagBlockEntity p_112306_) {
        return true;
    }

    public int getViewDistance() {
        return 256;
    }

    @Override
    public boolean shouldRender(FlagBlockEntity flag, Vec3 pos) {
        return Vec3.atCenterOf(flag.getBlockPos()).multiply(1.0D, 0.0D, 1.0D).closerThan(pos.multiply(1.0D, 0.0D, 1.0D), this.getViewDistance());
    }


}
