package com.st0x0ef.stellaris.client.skys.helper;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.StellarisClient;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class SkyHelper {
    @Nullable
    private static VertexBuffer customCloudBuffer = null;

    public static void drawStars(VertexBuffer vertexBuffer, Matrix4f matrix4f, Matrix4f projectionMatrix,
                                 ShaderInstance shaderInstance, boolean blend) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        FogRenderer.setupNoFog();
        vertexBuffer.bind();
        vertexBuffer.drawWithShader(matrix4f, projectionMatrix, shaderInstance);
        VertexBuffer.unbind();

        if (blend) {
            RenderSystem.disableBlend();
        }
    }

    public static void renderCustomClouds(PoseStack poseStack, Matrix4f projectionMatrix, Matrix4f frustrumMatrix, float partialTick, double camX, double camY, double camZ, ResourceLocation cloudsTexture) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, cloudsTexture);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        if (customCloudBuffer == null) {
            generateCustomClouds();
        }

        assert poseStack != null;
        poseStack.pushPose();
        poseStack.translate(-camX, -camY, -camZ);
        poseStack.scale(12.0F, 1.0F, 12.0F);

        if (customCloudBuffer != null) {
            customCloudBuffer.bind();
            RenderType renderType = RenderType.clouds();
            renderType.setupRenderState();
            ShaderInstance shaderInstance = RenderSystem.getShader();
            customCloudBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderInstance);
            renderType.clearRenderState();
            VertexBuffer.unbind();
        }

        poseStack.popPose();
        RenderSystem.disableBlend();
    }


    private static void generateCustomClouds() {
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        float cloudHeight = 195.0F;
        float cloudScale = 256.0F;

        bufferBuilder.vertex(-cloudScale, cloudHeight, -cloudScale).uv(0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(cloudScale, cloudHeight, -cloudScale).uv(1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(cloudScale, cloudHeight, cloudScale).uv(1.0F, 1.0F).endVertex();
        bufferBuilder.vertex(-cloudScale, cloudHeight, cloudScale).uv(0.0F, 1.0F).endVertex();

        customCloudBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        customCloudBuffer.upload(bufferBuilder.end());
    }

    public static Matrix4f setMatrixRot(PoseStack poseStack,
                                        Triple<Quaternionf, Quaternionf, Quaternionf> quaternionTriple) {
        poseStack.pushPose();

        Quaternionf left = quaternionTriple.getLeft();
        Quaternionf middle = quaternionTriple.getMiddle();
        Quaternionf right = quaternionTriple.getRight();

        if (left != null) {
            poseStack.mulPose(quaternionTriple.getLeft());
        }

        if (middle != null) {
            poseStack.mulPose(quaternionTriple.getMiddle());
        }

        if (right != null) {
            poseStack.mulPose(quaternionTriple.getRight());
        }

        Matrix4f matrix4f = poseStack.last().pose();
        poseStack.popPose();

        return matrix4f;
    }
}
