package com.st0x0ef.stellaris.client.renderers.entities.mogler;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.mobs.Mogler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;

@Environment(EnvType.CLIENT)
public class MoglerModel<T extends Mob & HoglinBase> extends HierarchicalModel<Mogler> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "mogler"), "main");
    private final ModelPart root;

    public MoglerModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -4.0F, -13.0F, 16.0F, 9.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.0F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(85, 30).addBox(-9.0F, -20.0F, 12.0F, 18.0F, 9.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 41).addBox(-10.0F, -18.0F, 5.0F, 20.0F, 9.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(58, 52).addBox(-11.0F, -18.0F, 2.0F, 22.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(64, 0).addBox(-12.0F, -22.0F, -4.0F, 24.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, -12.0F));

        PartDefinition cube_r5 = head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 66).addBox(-10.0F, -26.0F, -3.0F, 20.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.0F, 12.0F, 0.829F, 0.0F, 0.0F));

        PartDefinition cube_r6 = head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 22).addBox(-7.5F, -8.5F, -1.75F, 15.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.5F, -8.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition cube_r7 = head.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(62, 75).addBox(-9.0F, -8.5F, -1.75F, 18.0F, 13.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.5F, -8.0F, -0.6545F, 0.0F, 0.0F));

        PartDefinition jaw_l = head.addOrReplaceChild("jaw_l", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 3.5F, -8.0F, -0.6545F, 0.0F, 0.0F));

        PartDefinition cube_r8 = jaw_l.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(96, 99).addBox(5.25F, -1.5F, -0.75F, 5.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

        PartDefinition jaw_r = head.addOrReplaceChild("jaw_r", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 3.5F, -8.0F, -0.6545F, 0.0F, 0.0F));

        PartDefinition cube_r9 = jaw_r.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 110).addBox(-10.25F, -1.5F, -0.75F, 5.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5672F));

        PartDefinition front_legs = root.addOrReplaceChild("front_legs", CubeListBuilder.create(), PartPose.offset(9.5F, -9.5F, 12.5F));

        PartDefinition right_f = front_legs.addOrReplaceChild("right_f", CubeListBuilder.create().texOffs(64, 99).addBox(-3.5F, -3.5F, -4.5F, 7.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-19.0F, 0.0F, -19.0F));

        PartDefinition left_f = front_legs.addOrReplaceChild("left_f", CubeListBuilder.create().texOffs(32, 90).addBox(-3.5F, -3.5F, -4.5F, 7.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -19.0F));

        PartDefinition back_legs = root.addOrReplaceChild("back_legs", CubeListBuilder.create(), PartPose.offset(-9.5F, -9.5F, 12.5F));

        PartDefinition left_b = back_legs.addOrReplaceChild("left_b", CubeListBuilder.create().texOffs(0, 88).addBox(-3.5F, -3.5F, -4.5F, 7.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(19.0F, 0.0F, 0.0F));

        PartDefinition right_b = back_legs.addOrReplaceChild("right_b", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -3.5F, -4.5F, 7.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(Mogler entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(MoglerAnim.walk, limbSwing, limbSwingAmount, 5f, 2.5f);
        this.animate(entity.attackAnimationState, MoglerAnim.attack, ageInTicks, 1f);

    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.root.getChild("body").getChild("head").yRot = headYaw * ((float)Math.PI / 180f);
        this.root.getChild("body").getChild("head").xRot = headPitch *  ((float)Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (young) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.translate(0, 1.5f, 0);
        }
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}