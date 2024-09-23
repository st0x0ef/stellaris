package com.st0x0ef.stellaris.client.renderers.entities.vehicle.rover;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.vehicles.RoverEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;


public class RoverModel<T extends RoverEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "rover"), "main");

    private final ModelPart rover;

    public RoverModel(ModelPart root) {
        this.rover = root.getChild("rover");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition rover = partdefinition.addOrReplaceChild("rover", CubeListBuilder.create().texOffs(0, 0).addBox(-15.0F, -10.0F, -16.0F, 30.0F, 3.0F, 43.0F, new CubeDeformation(0.0F))
                .texOffs(88, 64).addBox(-18.0F, -9.6F, -17.0F, 36.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(88, 64).addBox(-18.0F, -9.6F, 25.0F, 36.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 46).addBox(-11.0F, -13.0F, -29.0F, 22.0F, 3.0F, 22.0F, new CubeDeformation(0.0F))
                .texOffs(66, 53).addBox(6.0F, -24.0F, -3.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(7.0F, -22.0F, -17.0F, 8.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(139, 28).addBox(6.0F, -20.0F, -10.0F, 10.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(139, 28).addBox(-16.0F, -20.0F, -10.0F, 10.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-15.0F, -22.0F, -17.0F, 8.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(103, 0).addBox(-9.0F, -35.0F, -23.0F, 2.0F, 22.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(103, 24).addBox(-14.0F, -14.0F, 4.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(32, 24).addBox(-9.0F, -23.0F, 17.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 71).addBox(-15.0F, -33.0F, 21.0F, 30.0F, 23.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(66, 46).addBox(-15.0F, -12.0F, 23.0F, 30.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(64, 71).addBox(-9.0F, -22.0F, 24.0F, 18.0F, 10.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 96).addBox(-9.0F, -24.0F, 24.0F, 18.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(32, 24).addBox(7.0F, -23.0F, 17.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(103, 24).addBox(2.0F, -14.0F, 4.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 24.0F, -4.0F));

        PartDefinition cube_r1 = rover.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(50, 101).addBox(2.5F, -1.0F, -10.0F, 12.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(50, 101).addBox(-13.5F, -1.0F, -10.0F, 12.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -23.0F, 18.0F, 1.2217F, 0.0F, 0.0F));

        PartDefinition cube_r2 = rover.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 55).addBox(-5.0F, -5.0F, -0.5F, 10.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -21.0F, 1.5F, 0.2182F, 0.0F, 0.0F));

        PartDefinition cube_r3 = rover.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(64, 71).addBox(-1.0F, -3.5F, -3.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -17.5F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition wheels = rover.addOrReplaceChild("wheels", CubeListBuilder.create(), PartPose.offset(-16.0F, 0.0F, 21.0F));

        PartDefinition wheel = wheels.addOrReplaceChild("wheel", CubeListBuilder.create(), PartPose.offset(-2.0F, -8.0F, 5.5F));

        PartDefinition bone18 = wheel.addOrReplaceChild("bone18", CubeListBuilder.create().texOffs(126, 0).addBox(-1.5F, -11.9355F, -1.3787F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(126, 0).addBox(-1.5F, 17.763F, -31.0772F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(126, 0).addBox(33.5F, 17.763F, -31.0772F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(126, 0).addBox(33.5F, -11.9355F, -1.3787F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -4.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bone19 = wheel.addOrReplaceChild("bone19", CubeListBuilder.create().texOffs(126, 0).addBox(-1.5F, 6.2787F, -7.0355F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(126, 0).addBox(-1.5F, 35.9772F, 22.663F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(126, 0).addBox(33.5F, 35.9772F, 22.663F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(126, 0).addBox(33.5F, 6.2787F, -7.0355F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -4.0F, -2.3562F, 0.0F, 0.0F));

        PartDefinition bone20 = wheel.addOrReplaceChild("bone20", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 0.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition bone21 = wheel.addOrReplaceChild("bone21", CubeListBuilder.create().texOffs(126, 0).addBox(-1.5F, -9.9F, 0.5F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(126, 0).addBox(-1.5F, -9.9F, -41.5F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(126, 0).addBox(33.5F, -9.9F, -41.5F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(126, 0).addBox(33.5F, -9.9F, 0.5F, 5.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.5F, -4.0F));

        PartDefinition wheel1 = wheels.addOrReplaceChild("wheel1", CubeListBuilder.create(), PartPose.offset(34.0F, -8.0F, -36.5F));

        PartDefinition bone10 = wheel1.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -8.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 4.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bone9 = wheel1.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -8.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 4.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));

        PartDefinition bone8 = wheel1.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -8.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 4.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition bone7 = wheel1.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -8.9F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 3.9F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 37).addBox(1.5F, -5.1F, -4.5F, 0.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 0.5F, 0.0F));

        PartDefinition wheel2 = wheels.addOrReplaceChild("wheel2", CubeListBuilder.create(), PartPose.offset(-2.0F, -8.0F, -36.5F));

        PartDefinition bone2 = wheel2.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -8.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 4.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bone3 = wheel2.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -8.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 4.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));

        PartDefinition bone4 = wheel2.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -8.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 4.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition bone5 = wheel2.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -8.9F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 3.9F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 37).addBox(1.5F, -5.1F, -4.5F, 0.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.5F, 0.0F));

        PartDefinition wheel3 = wheels.addOrReplaceChild("wheel3", CubeListBuilder.create(), PartPose.offset(34.0F, -8.0F, 5.5F));

        PartDefinition bone6 = wheel3.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -11.2284F, -0.6716F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 1.5716F, -0.6716F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.0F, -4.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bone11 = wheel3.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -11.2284F, -6.3284F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 1.5716F, -6.3284F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.0F, -4.0F, -2.3562F, 0.0F, 0.0F));

        PartDefinition bone12 = wheel3.addOrReplaceChild("bone12", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -12.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 0.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition bone13 = wheel3.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -8.9F, 0.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 3.9F, 0.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 37).addBox(1.5F, -5.1F, -0.5F, 0.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 0.5F, -4.0F));

        PartDefinition wheel4 = wheels.addOrReplaceChild("wheel4", CubeListBuilder.create(), PartPose.offset(-2.0F, -8.0F, 5.5F));

        PartDefinition bone14 = wheel4.addOrReplaceChild("bone14", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -11.2284F, -0.6716F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 1.5716F, -0.6716F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -4.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bone15 = wheel4.addOrReplaceChild("bone15", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -11.2284F, -6.3284F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 1.5716F, -6.3284F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -4.0F, -2.3562F, 0.0F, 0.0F));

        PartDefinition bone16 = wheel4.addOrReplaceChild("bone16", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -12.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 0.4F, -3.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition bone17 = wheel4.addOrReplaceChild("bone17", CubeListBuilder.create().texOffs(111, 0).addBox(-0.5F, -8.9F, 0.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(111, 0).addBox(-0.5F, 3.9F, 0.5F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 37).addBox(1.5F, -5.1F, -0.5F, 0.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.5F, -4.0F));

        PartDefinition antenna = rover.addOrReplaceChild("antenna", CubeListBuilder.create().texOffs(28, 0).addBox(10.9319F, -0.6943F, 0.7497F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(4.9319F, -1.1943F, 0.2497F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 40).addBox(-6.0681F, 0.8057F, 1.7497F, 17.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(66, 46).addBox(0.9319F, -0.1943F, 0.2497F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(4.9319F, -7.1943F, -5.7503F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, -35.275F, -22.0F, 0.0F, 0.6545F, -0.3927F));

        PartDefinition cube_r4 = antenna.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 40).addBox(-8.5F, 0.0F, -0.5F, 17.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.4319F, 0.8057F, 2.2497F, -1.6144F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(T entity, float f, float f1, float f2, float f3, float f4) {
        this.rover.yRot = f3 / (180F / (float) Math.PI);

        this.rover.getChild("wheels").getChild("wheel1").xRot = f2 / (180F / (float) Math.PI);
        this.rover.getChild("wheels").getChild("wheel2").xRot = f2 / (180F / (float) Math.PI);
        this.rover.getChild("wheels").getChild("wheel3").xRot = f2 / (180F / (float) Math.PI);
        this.rover.getChild("wheels").getChild("wheel4").xRot = f2 / (180F / (float) Math.PI);

        if (entity.getforward()) {
            this.rover.getChild("wheels").getChild("wheel1").xRot = f / 4;
            this.rover.getChild("wheels").getChild("wheel2").xRot = f / 4;
            this.rover.getChild("wheels").getChild("wheel3").xRot = f / 4;
            this.rover.getChild("wheels").getChild("wheel4").xRot = f / 4;
        }
        if (!entity.getforward()) {
            this.rover.getChild("wheels").getChild("wheel1").xRot = f / 4;
            this.rover.getChild("wheels").getChild("wheel2").xRot = f / 4;
            this.rover.getChild("wheels").getChild("wheel3").xRot = f / 4;
            this.rover.getChild("wheels").getChild("wheel4").xRot = f / 4;
        }

        this.rover.getChild("antenna").yRot = f2 / 20f;
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.rover.render(poseStack, buffer, packedLight, packedOverlay);
    }
}