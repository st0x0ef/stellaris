package com.st0x0ef.stellaris.client.renderers.entities.pygro;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.renderers.entities.mogler.MoglerAnim;
import com.st0x0ef.stellaris.common.entities.mobs.PygroBrute;
import com.st0x0ef.stellaris.common.entities.mobs.pygro.Pygro;
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
public class PygroBruteModel<T extends PygroBrute> extends HierarchicalModel<PygroBrute> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "pygro_brute"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart fang;
    private final ModelPart fang2;
    private final ModelPart ear2;
    private final ModelPart ear;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart right_leg;
    private final ModelPart leg_left;

    public PygroBruteModel(ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.head = this.body.getChild("head");
        this.fang = this.head.getChild("fang");
        this.fang2 = this.head.getChild("fang2");
        this.ear2 = this.head.getChild("ear2");
        this.ear = this.head.getChild("ear");
        this.right_arm = this.body.getChild("right_arm");
        this.left_arm = this.body.getChild("left_arm");
        this.right_leg = this.root.getChild("right_leg");
        this.leg_left = this.root.getChild("leg_left");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, -12.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bipedHead_r1 = head.addOrReplaceChild("bipedHead_r1", CubeListBuilder.create().texOffs(46, 0).addBox(-4.5F, -4.5F, -0.75F, 9.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.5F, -4.0F, 0.3054F, 0.0F, 0.0F));

        PartDefinition fang = head.addOrReplaceChild("fang", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = fang.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(31, 1).mirror().addBox(-1.25F, -1.35F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -25.0F, -4.5F, 0.0631F, 0.3435F, 0.1855F));

        PartDefinition fang2 = head.addOrReplaceChild("fang2", CubeListBuilder.create(), PartPose.offset(4.5F, 24.0F, 0.0F));

        PartDefinition cube_r2 = fang2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(31, 1).addBox(-2.15F, -1.45F, -0.35F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -25.0F, -4.5F, 0.0631F, -0.3435F, -0.1855F));

        PartDefinition ear2 = head.addOrReplaceChild("ear2", CubeListBuilder.create().texOffs(52, 53).mirror().addBox(-0.5F, -0.5F, -2.0F, 1.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.5F, -6.5F, 0.0F, 0.0F, 0.0F, 0.1309F));

        PartDefinition ear = head.addOrReplaceChild("ear", CubeListBuilder.create().texOffs(52, 53).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -6.5F, 0.0F, 0.0F, 0.0F, -0.1309F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(32, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leg_left = root.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(32, 48).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.8F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(PygroBrute entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(PygroBruteAnim.walk, limbSwing, limbSwingAmount, 3f, 2.5f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (young) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.translate(0, 1.5f, 0);
        }
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.root.getChild("body").getChild("head").yRot = headYaw * ((float)Math.PI / 180f);
        this.root.getChild("body").getChild("head").xRot = headPitch *  ((float)Math.PI / 180f);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}