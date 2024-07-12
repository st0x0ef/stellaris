package com.st0x0ef.stellaris.client.skys.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.mixin.client.LevelRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Triple;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class SkyHelper {

    /** PLANETS */
    public static final ResourceLocation SUN = new ResourceLocation(Stellaris.MODID,
            "textures/environment/star/sun.png");
    public static final ResourceLocation MOON_PHASE = new ResourceLocation(Stellaris.MODID,
            "textures/environment/planet/moon_phases.png");
    public static final ResourceLocation EARTH = new ResourceLocation(Stellaris.MODID,
            "textures/environment/solar_system/earth.png");
    public static final ResourceLocation MARS = new ResourceLocation(Stellaris.MODID,
            "textures/environment/solar_system/mars.png");
    public static final ResourceLocation PHOBOS = new ResourceLocation(Stellaris.MODID,
            "textures/environment/solar_system/phobos.png");
    public static final ResourceLocation DEIMOS = new ResourceLocation(Stellaris.MODID,
            "textures/environment/solar_system/deimos.png");

    /** LIGHTS */
    public static final ResourceLocation PLANET_LIGHT = new ResourceLocation(Stellaris.MODID,
            "textures/environment/planet/planet_light.png");
    public static final ResourceLocation PLANET_PHASE_LIGHT = new ResourceLocation(Stellaris.MODID,
            "textures/environment/planet/planet_phases_light.png");

    /** RAIN */
    public static final ResourceLocation MARS_DUST = new ResourceLocation(Stellaris.MODID,
            "textures/environment/mars_dust.png");
    public static final ResourceLocation SNOW = new ResourceLocation("textures/environment/snow.png");

    private static int alpha = 255;

    public static void drawStars(VertexBuffer vertexBuffer, Matrix4f matrix4f, Matrix4f projectionMatrix, ShaderInstance shaderInstance, Runnable setupFog, boolean blend) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        FogRenderer.setupNoFog();
        vertexBuffer.bind();
        vertexBuffer.drawWithShader(matrix4f, projectionMatrix, shaderInstance);
        VertexBuffer.unbind();

        if (blend) {
            RenderSystem.disableBlend();
        }
    }

    public static void drawPlanetWithMoonPhaseAndWithLight(ResourceLocation texture, Vec3 lightColor,
                                                           BufferBuilder bufferBuilder, Matrix4f matrix4f, float size, float lightSize, float y, Minecraft mc,
                                                           boolean blend) {
        drawPlanetWithMoonPhaseAndWithLight(texture, lightColor, bufferBuilder, matrix4f, size, lightSize, y, mc, blend,
                mc.level.getMoonPhase());
    }

    public static void drawPlanetWithMoonPhaseAndWithLight(ResourceLocation texture, Vec3 lightColor,
                                                           BufferBuilder bufferBuilder, Matrix4f matrix4f, float size, float lightSize, float y, Minecraft mc,
                                                           boolean blend, int phase) {
        drawPlanetWithMoonPhase(texture, new Vec3(255, 255, 255), bufferBuilder, matrix4f, size, y, mc, blend, phase);
        drawPlanetWithMoonPhase(PLANET_PHASE_LIGHT, lightColor, bufferBuilder, matrix4f, lightSize, y, mc, true, phase);
    }

    public static void drawPlanetWithMoonPhase(ResourceLocation texture, Vec3 color, BufferBuilder bufferBuilder,
                                               Matrix4f matrix4f, float size, float y, Minecraft mc, boolean blend, int phase) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        int r = (int) color.x();
        int g = (int) color.y();
        int b = (int) color.z();

        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, texture);
        int l = phase % 4;
        int i1 = phase / 4 % 2;
        float f13 = (float) (l) / 4.0F;
        float f14 = (float) (i1) / 2.0F;
        float f15 = (float) (l + 1) / 4.0F;
        float f16 = (float) (i1 + 1) / 2.0F;
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferBuilder.vertex(matrix4f, -size, -y, size).color(r, g, b, 255).uv(f15, f16).endVertex();
        bufferBuilder.vertex(matrix4f, size, -y, size).color(r, g, b, 255).uv(f13, f16).endVertex();
        bufferBuilder.vertex(matrix4f, size, -y, -size).color(r, g, b, 255).uv(f13, f14).endVertex();
        bufferBuilder.vertex(matrix4f, -size, -y, -size).color(r, g, b, 255).uv(f15, f14).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());

        if (blend) {
            RenderSystem.disableBlend();
        }
    }

    public static void drawSunWithLight(ResourceLocation texture, Vec3 sunColor, Vec3 lightColor,
                                        BufferBuilder bufferBuilder, Matrix4f matrix4f, float size, float lightSize, float y, boolean blend) {
        drawPlanet(texture, sunColor, bufferBuilder, matrix4f, size, y, blend);
        drawPlanet(SkyHelper.PLANET_LIGHT, lightColor, bufferBuilder, matrix4f, lightSize, y, true);
    }

    public static void drawPlanetWithLight(ResourceLocation texture, Vec3 lightColor, BufferBuilder bufferBuilder,
                                           Matrix4f matrix4f, float size, float lightSize, float y, boolean blend) {
        drawPlanet(texture, new Vec3(255, 255, 255), bufferBuilder, matrix4f, size, y, blend);
        drawPlanet(SkyHelper.PLANET_LIGHT, lightColor, bufferBuilder, matrix4f, lightSize, y, true);
    }

    public static void drawPlanet(ResourceLocation texture, Vec3 color, BufferBuilder bufferBuilder, Matrix4f matrix4f,
                                  float size, float y, boolean blend) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }
        int r = (int) color.x();
        int g = (int) color.y();
        int b = (int) color.z();
        int a = alpha;

        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, texture);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferBuilder.vertex(matrix4f, -size, y, -size).color(r, g, b, a).uv(1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, size, y, -size).color(r, g, b, a).uv(0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, size, y, size).color(r, g, b, a).uv(0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(matrix4f, -size, y, size).color(r, g, b, a).uv(1.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());

        if (blend) {
            RenderSystem.disableBlend();
        }
    }


    public static void drawSatellites(Vec3 color, BufferBuilder bufferBuilder, Matrix4f matrix4f, float sinTick,
                                      float cosTick, float xAngle, float yAngle, float zAngle, float size, float y, boolean blend) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        int r = (int) color.x();
        int g = (int) color.y();
        int b = (int) color.z();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.vertex(matrix4f, sinTick * xAngle - size, y + cosTick * yAngle, cosTick * zAngle - size)
                .color(r, g, b, 255).endVertex();
        bufferBuilder.vertex(matrix4f, sinTick * xAngle + size, y + cosTick * yAngle, cosTick * zAngle - size)
                .color(r, g, b, 255).endVertex();
        bufferBuilder.vertex(matrix4f, sinTick * xAngle + size, y + cosTick * yAngle, cosTick * zAngle + size)
                .color(r, g, b, 255).endVertex();
        bufferBuilder.vertex(matrix4f, sinTick * xAngle - size, y + cosTick * yAngle, cosTick * zAngle + size)
                .color(r, g, b, 255).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());

        if (blend) {
            RenderSystem.disableBlend();
        }
    }

    public static void setupSunRiseColor(PoseStack poseStack, BufferBuilder bufferBuilder, float partialTick,
                                         Minecraft mc, boolean blend) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
        }

        float[] afloat = mc.level.effects().getSunriseColor(mc.level.getTimeOfDay(partialTick), partialTick);
        if (afloat != null) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            float f3 = Mth.sin(mc.level.getSunAngle(partialTick)) < 0.0F ? 180.0F : 0.0F;
            poseStack.mulPose(Axis.ZP.rotationDegrees(f3));
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            float f4 = afloat[0];
            float f5 = afloat[1];
            float f6 = afloat[2];
            Matrix4f matrix4f = poseStack.last().pose();
            bufferBuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
            bufferBuilder.vertex(matrix4f, 0.0F, 100.0F, 0.0F).color(f4, f5, f6, afloat[3]).endVertex();
            int i = 16;

            for (int j = 0; j <= i; ++j) {
                float f7 = (float) j * ((float) Math.PI * 2F) / 16.0F;
                float f8 = Mth.sin(f7);
                float f9 = Mth.cos(f7);
                bufferBuilder.vertex(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * afloat[3])
                        .color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
            }

            BufferUploader.drawWithShader(bufferBuilder.end());
            poseStack.popPose();
        }

        if (blend) {
            RenderSystem.disableBlend();
        }
    }

    public static void setupShaderColor(Minecraft mc, float r, float g, float b) {
        if (mc.level.effects().hasGround()) {
            RenderSystem.setShaderColor(r * 0.2F + 0.04F, g * 0.2F + 0.04F, b * 0.6F + 0.1F, 1.0F);
        } else {
            RenderSystem.setShaderColor(r, g, b, 1.0F);
        }
    }

    public static void drawSky(Minecraft mc, Matrix4f matrix4f, Matrix4f projectionMatrix,
                               ShaderInstance shaderInstance) {

        ((LevelRendererAccessor) mc.levelRenderer).stellaris$getSkyBuffer().bind();
        ((LevelRendererAccessor) mc.levelRenderer).stellaris$getSkyBuffer().drawWithShader(matrix4f, projectionMatrix, shaderInstance);
        VertexBuffer.unbind();
    }

    public static void setupRainSize(float[] rainSizeX, float[] rainSizeZ) {
        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < 32; ++j) {
                float f9 = (float) (j - 16);
                float f1 = (float) (i - 16);
                float f2 = Mth.sqrt(f9 * f9 + f1 * f1);
                rainSizeX[i << 5 | j] = -f1 / f2;
                rainSizeZ[i << 5 | j] = f9 / f2;
            }
        }
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