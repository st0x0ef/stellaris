package com.st0x0ef.stellaris.client.skies.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import org.apache.commons.lang3.BooleanUtils;
import org.joml.Matrix4f;

import java.util.Random;

public class StarHelper {
    public static VertexBuffer createStars(float scale, int amountFancy, int r, int g, int b) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        VertexBuffer vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);

        Random random = new Random();
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        GraphicsStatus graphicsMode = Minecraft.getInstance().options.graphicsMode().get();
        int stars = amountFancy / (BooleanUtils.toInteger(graphicsMode == GraphicsStatus.FANCY || graphicsMode == GraphicsStatus.FABULOUS) + 1);

        for (int i = 0; i < stars; i++) {
            float d0 = random.nextFloat() * 2.0F - 1.0F;
            float d1 = random.nextFloat() * 2.0F - 1.0F;
            float d2 = random.nextFloat() * 2.0F - 1.0F;
            float d3 = scale + random.nextFloat() * 0.1F;
            float d4 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d4 < 1.0f && d4 > 0.01f) {
                d4 = (float) (1.0f / Math.sqrt(d4));
                d0 *= d4;
                d1 *= d4;
                d2 *= d4;
                float d5 = d0 * 100.0f;
                float d6 = d1 * 100.0f;
                float d7 = d2 * 100.0f;
                float d8 = (float) Math.atan2(d0, d2);
                float d9 = (float) Math.sin(d8);
                float d10 = (float) Math.cos(d8);
                float d11 = (float) Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                float d12 = (float) Math.sin(d11);
                float d13 = (float) Math.cos(d11);
                float d14 = (float) (random.nextDouble() * Math.PI * 2.0D);
                float d15 = (float) Math.sin(d14);
                float d16 = (float) Math.cos(d14);

                for (int j = 0; j < 4; ++j) {
                    float d18 = ((j & 2) - 1) * d3;
                    float d19 = ((j + 1 & 2) - 1) * d3;
                    float d21 = d18 * d16 - d19 * d15;
                    float d22 = d19 * d16 + d18 * d15;
                    float d23 = d21 * d12 + 0f * d13;
                    float d24 = 0f * d12 - d21 * d13;
                    float d25 = d24 * d9 - d22 * d10;
                    float d26 = d22 * d9 + d24 * d10;

                    int color1 = r == -1 ? i : r;
                    int color2 = g == -1 ? i : g;
                    int color3 = b == -1 ? i : b;

                    bufferBuilder.vertex(d5 + d25, d6 + d23, d7 + d26).color(color1, color2, color3, 0xAA).endVertex();
                }
            }
        }

        vertexBuffer.bind();
        vertexBuffer.upload(bufferBuilder.end());
        VertexBuffer.unbind();
        return vertexBuffer;
    }

    public static void drawStars(VertexBuffer vertexBuffer, PoseStack poseStack, Matrix4f projectionMatrix) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90));
        FogRenderer.setupNoFog();
        vertexBuffer.bind();
        vertexBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, GameRenderer.getPositionColorShader());
        VertexBuffer.unbind();
        poseStack.popPose();
    }
}