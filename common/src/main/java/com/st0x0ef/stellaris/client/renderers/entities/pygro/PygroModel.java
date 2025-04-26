package com.st0x0ef.stellaris.client.renderers.entities.pygro;

import com.st0x0ef.stellaris.Stellaris;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class PygroModel extends HumanoidModel<PygroRenderState> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "pygro"), "main");

    public final ModelPart rightEar = this.head.getChild("right_ear");
    private final ModelPart leftEar = this.head.getChild("left_ear");

    private final PartPose bodyDefault = this.body.storePose();
    private final PartPose headDefault = this.head.storePose();
    private final PartPose leftArmDefault = this.leftArm.storePose();
    private final PartPose rightArmDefault = this.rightArm.storePose();

    public PygroModel(ModelPart root) {
        super(root);
    }

    public static MeshDefinition createMesh(CubeDeformation p_170812_) {
        MeshDefinition meshdefinition = PlayerModel.createMesh(p_170812_, false);
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, p_170812_), PartPose.ZERO);
        PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, p_170812_).texOffs(31, 1).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, p_170812_).texOffs(2, 4).addBox(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, p_170812_).texOffs(2, 0).addBox(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, p_170812_), PartPose.ZERO);
        partdefinition1.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(39, 6).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, p_170812_), PartPose.offsetAndRotation(4.5F, -6.0F, 0.0F, 0.0F, 0.0F, (-(float)Math.PI / 6F)));
        partdefinition1.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(39, 6).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, p_170812_), PartPose.offsetAndRotation(-4.5F, -6.0F, 0.0F, 0.0F, 0.0F, ((float)Math.PI / 6F)));
        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

        //EYES
        PartDefinition eyes = partdefinition1.addOrReplaceChild("eyesg", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        eyes.addOrReplaceChild("eyes", CubeListBuilder.create().texOffs(46, 0).addBox(-4.5F, -4.5F, -0.75F, 9.0F, 7.0F, 0.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -7.5F, -4.0F, 0.3054F, 0.0F, 0.0F));

        //NOSE 1
        PartDefinition fang = partdefinition1.addOrReplaceChild("noseg1", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        fang.addOrReplaceChild("nose1", CubeListBuilder.create().texOffs(33, 2).addBox(-1.25F, -1.35F, -0.5F, 3.0F, 3.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-2.0F, -25.0F, -4.5F, 0.0631F, 0.3435F, 0.1855F));

        //NOSE 2
        PartDefinition fang2 = partdefinition1.addOrReplaceChild("noseg2", CubeListBuilder.create(), PartPose.offset(4.5F, 24.0F, 0.0F));
        fang2.addOrReplaceChild("nose2", CubeListBuilder.create().texOffs(33, 2).addBox(-2.15F, -1.45F, -0.35F, 3.0F, 3.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-2.0F, -25.0F, -4.5F, 0.0631F, -0.3435F, -0.1855F));

        return meshdefinition;
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(PygroModel.createMesh(CubeDeformation.NONE), 64, 64);
    }

    @Override
    public void setupAnim(PygroRenderState state) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(state.yRot, state.xRot);

        this.animateWalk(PygroAnim.walk, state.walkAnimationPos, state.walkAnimationSpeed, 2f, 2.5f);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.head.yRot = headYaw * ((float)Math.PI / 180f);
        this.head.xRot = headPitch *  ((float)Math.PI / 180f);
    }
}