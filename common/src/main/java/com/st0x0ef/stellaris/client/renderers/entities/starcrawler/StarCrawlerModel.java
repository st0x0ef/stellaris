package com.st0x0ef.stellaris.client.renderers.entities.starcrawler;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.mobs.StarCrawler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class StarCrawlerModel<T extends StarCrawler> extends HierarchicalModel<StarCrawler> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "star_crawler"), "main");

    private final ModelPart root;

    public StarCrawlerModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -13.0F, -8.0F, 16.0F, 10.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 26).addBox(-7.0F, -9.0F, -7.0F, 14.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition arm1 = root.addOrReplaceChild("arm1", CubeListBuilder.create().texOffs(48, 48).addBox(-6.0F, -3.9F, 0.0F, 12.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(42, 26).addBox(-5.0F, 4.1F, -2.0F, 10.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 7.0F));

        PartDefinition cube_r1 = arm1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(48, 64).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 4.3F, 4.0F, 0.0F, 3.1416F, -0.3491F));

        PartDefinition cube_r2 = arm1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(58, 64).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 4.3F, 3.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition limb = arm1.addOrReplaceChild("limb", CubeListBuilder.create().texOffs(48, 0).addBox(-5.0F, -4.3F, 0.25F, 10.0F, 7.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 64).addBox(-4.0F, 2.7F, -0.75F, 8.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.3F, 6.75F));

        PartDefinition cube_r3 = limb.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(59, 38).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 3.0F, 5.25F, 0.0F, 3.1416F, -0.3491F));

        PartDefinition cube_r4 = limb.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(57, 16).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 3.0F, 4.25F, 0.0F, 0.0F, 0.3491F));

        PartDefinition hand = limb.addOrReplaceChild("hand", CubeListBuilder.create().texOffs(0, 49).addBox(-4.0F, -3.3F, 0.25F, 8.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(25, 55).addBox(-3.0F, 2.7F, -0.75F, 6.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.1F, 8.0F));

        PartDefinition cube_r5 = hand.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(39, 49).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 2.9F, 5.25F, 0.0F, 3.1416F, -0.3491F));

        PartDefinition cube_r6 = hand.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(49, 38).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.9F, 4.25F, 0.0F, 0.0F, 0.3491F));

        PartDefinition arm2 = root.addOrReplaceChild("arm2", CubeListBuilder.create().texOffs(48, 48).addBox(-6.0F, -3.9F, 0.0F, 12.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(42, 26).addBox(-5.0F, 4.1F, -2.0F, 10.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -7.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r7 = arm2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(48, 64).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 4.3F, 4.0F, 0.0F, 3.1416F, -0.3491F));

        PartDefinition cube_r8 = arm2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(58, 64).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 4.3F, 3.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition limb2 = arm2.addOrReplaceChild("limb2", CubeListBuilder.create().texOffs(48, 0).addBox(-5.0F, -4.3F, 0.25F, 10.0F, 7.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 64).addBox(-4.0F, 2.7F, -0.75F, 8.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.3F, 6.75F));

        PartDefinition cube_r9 = limb2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(59, 38).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 3.0F, 5.25F, 0.0F, 3.1416F, -0.3491F));

        PartDefinition cube_r10 = limb2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(57, 16).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 3.0F, 4.25F, 0.0F, 0.0F, 0.3491F));

        PartDefinition hand2 = limb2.addOrReplaceChild("hand2", CubeListBuilder.create().texOffs(0, 49).addBox(-4.0F, -3.3F, 0.25F, 8.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(25, 55).addBox(-3.0F, 2.7F, -0.75F, 6.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.1F, 8.0F));

        PartDefinition cube_r11 = hand2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(39, 49).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 2.9F, 5.25F, 0.0F, 3.1416F, -0.3491F));

        PartDefinition cube_r12 = hand2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(49, 38).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.9F, 4.25F, 0.0F, 0.0F, 0.3491F));

        PartDefinition arm3 = root.addOrReplaceChild("arm3", CubeListBuilder.create().texOffs(48, 48).addBox(-6.0F, -3.9F, 0.0F, 12.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(42, 26).addBox(-5.0F, 4.1F, -2.0F, 10.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -7.0F, -3.1416F, 0.0F, 3.1416F));

        PartDefinition cube_r13 = arm3.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(48, 64).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 4.3F, 4.0F, 0.0F, 3.1416F, -0.3491F));

        PartDefinition cube_r14 = arm3.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(58, 64).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 4.3F, 3.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition limb3 = arm3.addOrReplaceChild("limb3", CubeListBuilder.create().texOffs(48, 0).addBox(-5.0F, -4.3F, 0.25F, 10.0F, 7.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 64).addBox(-4.0F, 2.7F, -0.75F, 8.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.3F, 6.75F));

        PartDefinition cube_r15 = limb3.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(59, 38).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 3.0F, 5.25F, 0.0F, 3.1416F, -0.3491F));

        PartDefinition cube_r16 = limb3.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(57, 16).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 3.0F, 4.25F, 0.0F, 0.0F, 0.3491F));

        PartDefinition hand3 = limb3.addOrReplaceChild("hand3", CubeListBuilder.create().texOffs(0, 49).addBox(-4.0F, -3.3F, 0.25F, 8.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(25, 55).addBox(-3.0F, 2.7F, -0.75F, 6.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.1F, 8.0F));

        PartDefinition cube_r17 = hand3.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(39, 49).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 2.9F, 5.25F, 0.0F, 3.1416F, -0.3491F));

        PartDefinition cube_r18 = hand3.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(49, 38).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.9F, 4.25F, 0.0F, 0.0F, 0.3491F));

        PartDefinition arm4 = root.addOrReplaceChild("arm4", CubeListBuilder.create().texOffs(48, 48).addBox(-6.0F, -3.9F, 0.0F, 12.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(42, 26).addBox(-5.0F, 4.1F, -2.0F, 10.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -7.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r19 = arm4.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(48, 64).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 4.3F, 4.0F, 0.0F, 3.1416F, -0.3491F));

        PartDefinition cube_r20 = arm4.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(58, 64).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 4.3F, 3.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition limb4 = arm4.addOrReplaceChild("limb4", CubeListBuilder.create().texOffs(48, 0).addBox(-5.0F, -4.3F, 0.25F, 10.0F, 7.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 64).addBox(-4.0F, 2.7F, -0.75F, 8.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.3F, 6.75F));

        PartDefinition cube_r21 = limb4.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(59, 38).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 3.0F, 5.25F, 0.0F, 3.1416F, -0.3491F));

        PartDefinition cube_r22 = limb4.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(57, 16).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 3.0F, 4.25F, 0.0F, 0.0F, 0.3491F));

        PartDefinition hand4 = limb4.addOrReplaceChild("hand4", CubeListBuilder.create().texOffs(0, 49).addBox(-4.0F, -3.3F, 0.25F, 8.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(25, 55).addBox(-3.0F, 2.7F, -0.75F, 6.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.1F, 8.0F));

        PartDefinition cube_r23 = hand4.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(39, 49).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 2.9F, 5.25F, 0.0F, 3.1416F, -0.3491F));

        PartDefinition cube_r24 = hand4.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(49, 38).addBox(0.0F, 0.0F, -3.0F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.9F, 4.25F, 0.0F, 0.0F, 0.3491F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(StarCrawler entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw);

        this.animateWalk(StarCrawlerAnim.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
    }

    private void applyHeadRotation(float headYaw) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        this.root.yRot = headYaw * ((float)Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
