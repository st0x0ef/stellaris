package com.st0x0ef.stellaris.client.renderers.entities.martianraptor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.st0x0ef.stellaris.common.entities.mobs.MartianRaptor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

import static com.st0x0ef.stellaris.Stellaris.id;

@Environment(EnvType.CLIENT)
public class MartianRaptorModel<T extends MartianRaptor> extends HierarchicalModel<MartianRaptor> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(id("martian_raptor"), "main");

    private final ModelPart root;

    public MartianRaptorModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, 4.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.0F, -6.0F, 8.0F, 10.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -4.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -3.0F));

        PartDefinition bone2 = head.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(34, 34).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -8.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition cube_r1 = bone2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(52, 71).mirror().addBox(-1.0789F, -1.4145F, -2.25F, 3.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.8289F, 4.4542F, 3.0357F, 0.0F, 0.0F, -0.3491F));

        PartDefinition cube_r2 = bone2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(52, 71).addBox(-1.5789F, -1.5855F, -2.25F, 3.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1711F, 4.4542F, 3.0357F, 0.0F, 0.0F, 0.3491F));

        PartDefinition bone = bone2.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.25F, -6.75F, 6.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 42).addBox(-6.0F, -5.5F, -4.5F, 10.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2012F, 0.0F, -0.1198F, -0.0436F, 0.6981F, 0.0F));

        PartDefinition cube_r4 = bone.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-4.0F, -5.5F, -4.5F, 10.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.7988F, 0.0F, -0.1198F, -0.0436F, -0.6981F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 9.0F));

        PartDefinition tail_bone_1 = tail.addOrReplaceChild("tail_bone_1", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5921F, -1.2714F));

        PartDefinition cube_r5 = tail_bone_1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 25).addBox(-3.5F, -3.7396F, -1.4024F, 7.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6545F, 0.0F, 0.0F));

        PartDefinition cube_r6 = tail_bone_1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(24, 26).addBox(-10.5F, -2.0F, -5.5F, 8.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, 3.2986F, 6.6902F, 1.1345F, 0.0F, 0.0F));

        PartDefinition tail_bone_2 = tail_bone_1.addOrReplaceChild("tail_bone_2", CubeListBuilder.create(), PartPose.offset(0.5F, 4.2986F, 6.6902F));

        PartDefinition cube_r7 = tail_bone_2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(53, 0).addBox(-10.5F, -2.0F, -5.5F, 8.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 4.0F, 1.3526F, 0.0F, 0.0F));

        PartDefinition cube_r8 = tail_bone_2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(31, 0).addBox(-3.0F, -2.0307F, -0.9037F, 6.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.1844F, -0.3339F, -0.1309F, 0.0F, 0.0F));

        PartDefinition cube_r9 = tail_bone_2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(61, 31).addBox(-3.5F, -1.0F, -2.5F, 7.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 3.0408F, 7.1318F, 1.6144F, 0.0F, 0.0F));

        PartDefinition tail_bone_3 = tail_bone_2.addOrReplaceChild("tail_bone_3", CubeListBuilder.create(), PartPose.offset(-0.5F, 1.4301F, 7.7314F));

        PartDefinition cube_r10 = tail_bone_3.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(20, 50).addBox(-2.0F, -2.0F, -5.0F, 4.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition cube_r11 = tail_bone_3.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(63, 7).addBox(-2.5F, -1.0F, -1.5F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.3607F, 3.4004F, 1.7017F, 0.0F, 0.0F));

        PartDefinition leg_l = root.addOrReplaceChild("leg_l", CubeListBuilder.create().texOffs(14, 69).addBox(4.0F, 14.7065F, -6.0813F, 5.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 1.0F));

        PartDefinition cube_r12 = leg_l.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(36, 64).addBox(-2.5F, -7.0F, -2.5F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 10.7065F, 1.4187F, -0.6545F, 0.0F, 0.0F));

        PartDefinition cube_r13 = leg_l.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 58).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, 2.7065F, 1.4187F, 0.6545F, 0.0F, 0.0F));

        PartDefinition leg_r = root.addOrReplaceChild("leg_r", CubeListBuilder.create().texOffs(14, 69).mirror().addBox(-9.0F, 14.7065F, -6.0813F, 5.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -3.0F, 1.0F));

        PartDefinition cube_r14 = leg_r.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(36, 64).mirror().addBox(-2.5F, -7.0F, -2.5F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, 10.7065F, 1.4187F, -0.6545F, 0.0F, 0.0F));

        PartDefinition cube_r15 = leg_r.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 58).mirror().addBox(-2.5F, -6.0F, -2.5F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.5F, 2.7065F, 1.4187F, 0.6545F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(MartianRaptor entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(MartianRaptorAnim.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(entity.attackAnimationState, MartianRaptorAnim.attack, ageInTicks, 2f);

    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.root.getChild("body").getChild("head").yRot = headYaw * ((float)Math.PI / 180f);
        this.root.getChild("body").getChild("head").xRot = headPitch *  ((float)Math.PI / 180f);
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