package com.st0x0ef.stellaris.client.renderers.entities.alien;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.mobs.alien.Alien;
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
public class AlienModel<T extends Alien> extends HierarchicalModel<Alien> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "alien"), "main");

	private final ModelPart root;

	public AlienModel(ModelPart root) { this.root = root.getChild("root"); }

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(100, 0).mirror().addBox(0.0F, -12.0F, -5.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 38).mirror().addBox(0.0F, -12.0F, -5.0F, 8.0F, 19.0F, 6.0F, new CubeDeformation(0.5F)).mirror(false)
				.texOffs(0, 38).mirror().addBox(0.0F, -12.0F, -5.0F, 8.0F, 19.0F, 6.0F, new CubeDeformation(0.7F)).mirror(false), PartPose.offset(-4.0F, -12.0F, 2.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -9.0F, -4.0F, 8.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-4.5F, -18.0F, -4.5F, 9.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(80, 18).mirror().addBox(-8.0F, -14.0F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(32, 0).addBox(-4.5F, -18.0F, -4.5F, 9.0F, 10.0F, 9.0F, new CubeDeformation(0.5F))
				.texOffs(0, 0).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 9.0F, 8.0F, new CubeDeformation(0.5F))
				.texOffs(24, 0).mirror().addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, -12.0F, -2.0F));

		PartDefinition leg0 = root.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(16, 81).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(2.0F, -12.0F, 0.0F));

		PartDefinition leg1 = root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 63).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-2.0F, -12.0F, 0.0F));

		PartDefinition arms = root.addOrReplaceChild("arms", CubeListBuilder.create().texOffs(40, 38).mirror().addBox(-4.0F, 2.1434F, -1.7952F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(44, 22).mirror().addBox(4.0F, -1.8566F, -1.7952F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(44, 22).addBox(-8.0F, -1.8566F, -1.7952F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(44, 22).mirror().addBox(4.0F, -1.8566F, -1.7952F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false)
				.texOffs(44, 22).addBox(-8.0F, -1.8566F, -1.7952F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, -22.0F, 0.0F, -0.9599F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Alien entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root.getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch);

		this.animateWalk(AlienAnim.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
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
