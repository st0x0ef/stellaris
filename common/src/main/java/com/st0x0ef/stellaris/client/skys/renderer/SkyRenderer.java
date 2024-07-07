package com.st0x0ef.stellaris.client.skys.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.skys.record.SkyProperties;
import com.st0x0ef.stellaris.client.skys.record.SkyPropertiesData;
import com.st0x0ef.stellaris.common.utils.Utils;
import com.st0x0ef.stellaris.mixin.client.LevelRendererAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class SkyRenderer extends DimensionSpecialEffects {
    @Nullable
    private VertexBuffer star_buffer;

    public SkyRenderer() {
        super(Float.NaN, false, SkyType.NONE, false, false);
    }

    public static void render(ClientLevel level, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        SkyProperties properties = SkyPropertiesData.getSkyPropertiesById(Minecraft.getInstance().player.level().dimension());
        Stellaris.LOG.info(String.valueOf(Utils.getColorHexCode(properties.sunriseColor())));

        setupFog.run();
        if (isFoggy || inFog(camera)) return;
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();

        renderSky(bufferBuilder, level, partialTick, poseStack, projectionMatrix, properties);
    }

    public static void renderSky(BufferBuilder bufferBuilder, ClientLevel level, float partialTick, PoseStack poseStack, Matrix4f projectionMatrix, SkyProperties skyProperties) {
        FogRenderer.levelFogColor();
        ShaderInstance shader = RenderSystem.getShader();
        if (shader == null) return;

        var skyBuffer = ((LevelRendererAccessor) Minecraft.getInstance().levelRenderer).getSkyBuffer();
        skyBuffer.bind();
        if (poseStack != null) skyBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shader);
        VertexBuffer.unbind();
        RenderSystem.enableBlend();

        float[] color = getSunriseColor(level.getTimeOfDay(partialTick), partialTick, Utils.getColorHexCode(skyProperties.sunriseColor()));
        if (color != null) {
            renderSunrise(bufferBuilder, level, partialTick, poseStack, color);
        }
    }

    @Nullable
    public static float[] getSunriseColor(float timeOfDay, float partialTicks, int sunColor) {
        float timeCos = Mth.cos(timeOfDay * (float) (Math.PI * 2));
        if (timeCos >= -0.4f && timeCos <= 0.4f) {
            float time = timeCos / 0.4f * 0.5f + 0.5f;
            float alpha = 1 - (1 - Mth.sin(time * (float) Math.PI)) * 0.99F;
            alpha *= alpha;
            var rgba = new float[4];
            rgba[0] = time * 0.3f + FastColor.ARGB32.red(sunColor) / 255f * 0.7f;
            rgba[1] = time * time * 0.7f + FastColor.ARGB32.green(sunColor) / 255f * 0.5f;
            rgba[2] = FastColor.ARGB32.blue(sunColor) / 255f * 0.6f;
            rgba[3] = alpha;
            return rgba;
        } else {
            return null;
        }
    }

    public static void renderSunrise(BufferBuilder bufferBuilder, ClientLevel level, float partialTick, PoseStack poseStack, float[] color) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        poseStack.mulPose(Axis.ZP.rotationDegrees(0));

        float sunAngle = Mth.sin(level.getSunAngle(partialTick)) < 0 ? 180 : 0;
        poseStack.mulPose(Axis.ZP.rotationDegrees(sunAngle));
        poseStack.mulPose(Axis.ZP.rotationDegrees(90));

        float r = color[0];
        float g = color[1];
        float b = color[2];

        Matrix4f matrix = poseStack.last().pose();
        bufferBuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.vertex(matrix, 0, 100, 0).color(r, g, b, color[3]).endVertex();

        for (int i = 0; i <= 16; i++) {
            float angle = (float) i * (float) (Math.PI * 2) / 16;
            float x = Mth.sin(angle);
            float y = Mth.cos(angle);
            bufferBuilder.vertex(matrix, x * 120, y * 120, -y * 40 * color[3]).color(r, g, b, 0).endVertex();
        }

        BufferUploader.drawWithShader(bufferBuilder.end());
        poseStack.popPose();
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
        return null;
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return false;
    }

    public static boolean inFog(Camera camera) {
        var levelRenderer = (LevelRendererAccessor) Minecraft.getInstance().levelRenderer;
        var fogType = camera.getFluidInCamera();
        return fogType == FogType.POWDER_SNOW
                || fogType == FogType.LAVA
                || levelRenderer.invokeDoesMobEffectBlockSky(camera);
    }
}
