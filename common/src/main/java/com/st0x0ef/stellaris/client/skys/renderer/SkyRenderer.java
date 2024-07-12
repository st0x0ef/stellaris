package com.st0x0ef.stellaris.client.skys.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.st0x0ef.stellaris.client.skys.record.SkyProperties;
import com.st0x0ef.stellaris.client.skys.record.SkyPropertiesData;
import com.st0x0ef.stellaris.client.skys.utils.StarHelper;
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
    private SkyProperties skyProperties;
    private VertexBuffer starBuffer;

    public SkyRenderer(SkyProperties skyProperties) {
        super(Float.NaN, true, SkyType.NONE, false, false);
        this.skyProperties = skyProperties;
    }

    public void render(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        setupFog.run();
        if (isFoggy || inFog(camera)) return;
        if (level.isRaining()) return;

        if (skyProperties == null) skyProperties = SkyPropertiesData.SKY_PROPERTIES.get(Minecraft.getInstance().player.level().dimension()).skyProperties();

        if (skyProperties.stars().colored() && starBuffer == null) {
            starBuffer = StarHelper.createStars(0.1F, skyProperties.stars().count(), 190, 160, -1);
        } else {
            starBuffer = StarHelper.createStars(0.1F, skyProperties.stars().count(), 255, 255, 255);
        }

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();

        setSkyColor(level, camera, partialTick);

        RenderSystem.depthMask(false);
        VertexBuffer.unbind();
        RenderSystem.enableBlend();

        //renderSky(bufferBuilder, level, partialTick, poseStack, projectionMatrix);

        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        poseStack.pushPose();

        renderStars(level, partialTick, poseStack, projectionMatrix, setupFog);

        RenderSystem.disableBlend();

        RenderSystem.setShaderColor(1, 1, 1, 1);
//        renderer.skyRenderables().forEach(renderable -> {
//            var globalRotation = switch (renderable.movementType()) {
//                case STATIC -> renderable.globalRotation();
//                case TIME_OF_DAY -> renderable.globalRotation().add(0, 0, level.getTimeOfDay(partialTick) * 360);
//                case TIME_OF_DAY_REVERSED ->
//                        renderable.globalRotation().add(0, 0, -level.getTimeOfDay(partialTick) * 360);
//            };
//
//            renderSkyRenderable(bufferBuilder, poseStack, renderable.localRotation(), globalRotation, renderable.scale(), renderable.texture(), renderable.blend());
//            if (renderable.backLightScale() > 0) {
//                setSkyRenderableColor(level, partialTick, renderable.backLightColor());
//                renderSkyRenderable(bufferBuilder, poseStack, renderable.localRotation(), globalRotation, renderable.backLightScale(), DimensionRenderingUtils.BACKLIGHT, true);
//                RenderSystem.setShaderColor(1, 1, 1, 1);
//            }
//        });

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        poseStack.popPose();
        RenderSystem.depthMask(true);

    }


    public void renderSky(BufferBuilder bufferBuilder, ClientLevel level, float partialTick, PoseStack poseStack, Matrix4f projectionMatrix) {
        FogRenderer.levelFogColor();
        ShaderInstance shader = RenderSystem.getShader();
        if (shader == null) return;

        var skyBuffer = ((LevelRendererAccessor) Minecraft.getInstance().levelRenderer).stellaris$getSkyBuffer();
        skyBuffer.bind();
        skyBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shader);
        VertexBuffer.unbind();
        RenderSystem.enableBlend();

        float[] color = this.getSunriseColor(level.getTimeOfDay(partialTick), partialTick, skyProperties.sunriseColor());
        if (color != null) {
            //renderSunrise(bufferBuilder, level, partialTick, poseStack, color);
        }
    }


    public void setSkyColor(ClientLevel level, Camera camera, float partialTick) {
        Vec3 skyColor = level.getSkyColor(camera.getPosition(), partialTick);
        float r = (float) skyColor.x;
        float g = (float) skyColor.y;
        float b = (float) skyColor.z;
        RenderSystem.setShaderColor(r, g, b, 1);
    }

    public void renderStars(ClientLevel level, float partialTick, PoseStack poseStack, Matrix4f projectionMatrix, Runnable setupFog) {
        if (poseStack == null) return;

        float starLight = level.getStarBrightness(partialTick) * (1 - level.getRainLevel(partialTick));
        if (starLight <= 0) return;
        if (starBuffer == null) return;

        RenderSystem.setShaderColor(starLight, starLight, starLight, starLight);
        FogRenderer.setupNoFog();
        starBuffer.bind();
        var shader = GameRenderer.getPositionColorShader();
        if (shader == null || poseStack == null) return;
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPosition = camera.getPosition();
        Vec3 starsPosition = new Vec3(cameraPosition.x, cameraPosition.y, cameraPosition.z);
        poseStack.pushPose();
        poseStack.translate(starsPosition.x, starsPosition.y, starsPosition.z);
        starBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shader);
        VertexBuffer.unbind();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        poseStack.popPose();
        RenderSystem.depthMask(true);
        setupFog.run();
    }



    @Nullable
    @Override
    public float[] getSunriseColor(float timeOfDay, float partialTicks) {
        return getSunriseColor(timeOfDay, partialTicks, skyProperties().sunriseColor());
    }

    public float[] getSunriseColor(float timeOfDay, float partialTicks, int sunColor) {
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

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
        if (skyProperties.fog()) {
            return fogColor.multiply(
                    brightness * 0.94 + 0.06,
                    brightness * 0.94 + 0.06,
                    brightness * 0.91 + 0.09);
        }
        return Vec3.ZERO;
    }


    @Override
    public boolean isFoggyAt(int x, int y) {
        return false;
    }


    public boolean renderClouds() {
        return skyProperties.cloud();
    }



    public static boolean inFog(Camera camera) {
        LevelRendererAccessor levelRenderer = (LevelRendererAccessor) Minecraft.getInstance().levelRenderer;
        FogType fogType = camera.getFluidInCamera();
        return fogType == FogType.POWDER_SNOW || fogType == FogType.LAVA || levelRenderer.invokeDoesMobEffectBlockSky(camera);
    }

    public SkyProperties skyProperties() {
        return skyProperties;
    }
}
