package com.st0x0ef.stellaris.client.renderers.armors;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.injectables.targets.ArchitecturyTarget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class JetSuitModel extends HumanoidModel<LivingEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Stellaris.MODID, "jet_suit"), "main");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/entity/armor/jet_suit.png");

    private final ModelPart visor;
    private final ModelPart belt;
    private final ModelPart rightBoot;
    private final ModelPart leftBoot;

    private final EquipmentSlot slot;
    @Nullable
    private final HumanoidModel<LivingEntity> parentModel;

    public JetSuitModel(ModelPart root, EquipmentSlot slot, ItemStack stack, @Nullable HumanoidModel<LivingEntity> parentModel) {
        super(root, RenderType::entityTranslucent);

        this.visor = root.getChild("visor");
        this.belt = root.getChild("belt");
        this.rightBoot = root.getChild("left_boot");
        this.leftBoot = root.getChild("right_boot");
        this.slot = slot;
        this.parentModel = parentModel;
        this.setVisible();
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (Objects.equals(ArchitecturyTarget.getCurrentTarget(), "neoforge")) {
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            buffer = bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE));
        }

        if (this.parentModel == null) return;
        this.visor.copyFrom(parentModel.head);
        this.belt.copyFrom(parentModel.body);
        this.rightBoot.copyFrom(parentModel.rightLeg);
        this.leftBoot.copyFrom(parentModel.leftLeg);
        parentModel.copyPropertiesTo(this);

        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, 0, 0, 0, alpha); // TODO : check rgb
    }

    @Override
    public void setAllVisible(boolean visible) {
        super.setAllVisible(visible);
        this.visor.visible = visible;
        this.belt.visible = visible;
        this.rightBoot.visible = visible;
        this.leftBoot.visible = visible;
    }

    private void setVisible() {
        this.setAllVisible(false);
        switch (this.slot) {
            case HEAD -> {
                this.head.visible = true;
                this.visor.visible = true;
            }
            case CHEST -> {
                this.body.visible = true;
                this.rightArm.visible = true;
                this.leftArm.visible = true;
            }
            case LEGS -> {
                this.belt.visible = true;
                this.rightLeg.visible = true;
                this.leftLeg.visible = true;
            }
            case FEET -> {
                this.rightBoot.visible = true;
                this.leftBoot.visible = true;
            }
        }
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(head, visor);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(body, rightArm, leftArm, rightLeg, leftLeg, hat, belt, rightBoot, leftBoot);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F))
                .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F))
                .texOffs(0, 32).addBox(3.0F, -13.0F, 1.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0175F, 0.0873F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(28, 28).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(50, 29).addBox(-3.0F, 5.0F, -2.5F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.25F))
                .texOffs(0, 55).addBox(-2.5F, 1.0F, 2.75F, 5.0F, 8.0F, 1.0F, new CubeDeformation(0.75F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(32, 31).addBox(-2.0F, -5.0F, 0.75F, 0.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 2.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition body_r2 = body.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(32, 31).addBox(2.0F, -5.0F, 0.75F, 0.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 2.0F, 0.0F, 0.3491F, 0.0F));

        PartDefinition rightArm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(20, 44).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.26F)).texOffs(48, 8).addBox(-3.0F, 6.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition leftArm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.26F)).texOffs(48, 0).addBox(-1.0F, 6.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition rightLeg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(48, 44).addBox(-2.0F, 5.7F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F))
                .texOffs(48, 54).addBox(-2.0F, 5.7F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.27F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition leftLeg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(48, 44).addBox(-2.0F, 5.7F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F))
                .texOffs(48, 54).addBox(-2.0F, 5.7F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.27F)), PartPose.offset(2.0F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}