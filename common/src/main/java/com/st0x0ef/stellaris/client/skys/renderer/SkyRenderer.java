package com.st0x0ef.stellaris.client.skys.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.skys.record.CustomVanillaObject;
import com.st0x0ef.stellaris.client.skys.record.SkyObject;
import com.st0x0ef.stellaris.client.skys.record.SkyProperties;
import com.st0x0ef.stellaris.client.skys.utils.StarHelper;
import io.github.amerebagatelle.mods.nuit.mixin.LevelRendererAccessor;
import io.github.amerebagatelle.mods.nuit.skybox.AbstractSkybox;
import io.github.amerebagatelle.mods.nuit.skybox.decorations.DecorationBox;
import io.github.amerebagatelle.mods.nuit.skybox.vanilla.OverworldSkybox;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.LevelStem;
import org.joml.Matrix4f;

/**
 * Our own implementation for custom Sky
 */
public class SkyRenderer extends AbstractSkybox {

    private final SkyProperties properties;
    private VertexBuffer starBuffer;

    public SkyRenderer(SkyProperties properties) {
       this.properties = properties;
    }

    @Override
    public void render(LevelRendererAccessor levelRendererAccessor, PoseStack poseStack, Matrix4f matrix4f, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback) {
        RenderSystem.enableBlend();
        var world = Minecraft.getInstance().level;
        assert world != null;

        if(world.dimension() != properties.id()) return;

        if (properties.stars().colored() && starBuffer == null) {
            starBuffer = StarHelper.createStars(0.1F, properties.stars().count(), 190, 160, -1);
        } else if (starBuffer == null) {
            starBuffer = StarHelper.createStars(0.1F, properties.stars().count(), 255, 255, 255);
        }


        poseStack.pushPose();

        Matrix4f matrix4f2 = poseStack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();

        CustomVanillaObject customVanillaObject = this.properties.customVanillaObject();

        // Sun
        if (customVanillaObject.sun()) {
            this.renderSun(bufferBuilder, matrix4f2, customVanillaObject);
        }
        // Moon
        if (customVanillaObject.moon()) {
            this.renderMoon(bufferBuilder, matrix4f2, customVanillaObject);
        }

        for (SkyObject skyObject : this.properties.skyObjects()) {
            renderSkyObject(bufferBuilder, poseStack, skyObject);
        }

        // Stars
        this.renderStars(levelRendererAccessor, tickDelta, poseStack, matrix4f);

        poseStack.popPose();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    }


    public void renderSun(BufferBuilder bufferBuilder, Matrix4f matrix4f, CustomVanillaObject customVanillaObject) {
        RenderSystem.setShaderTexture(0, customVanillaObject.sunTexture());
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix4f, -30.0F, 100.0F, -30.0F).uv(0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, 30.0F, 100.0F, -30.0F).uv(1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, 30.0F, 100.0F, 30.0F).uv(1.0F, 1.0F).endVertex();
        bufferBuilder.vertex(matrix4f, -30.0F, 100.0F, 30.0F).uv(0.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
    }

    public void renderSkyObject(BufferBuilder bufferBuilder, PoseStack poseStack, SkyObject skyObject) {

        poseStack.pushPose();
        if (skyObject.blend()) RenderSystem.enableBlend();
        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees((float) skyObject.rotation().x));
        poseStack.mulPose(Axis.YP.rotationDegrees((float) skyObject.rotation().y));
        poseStack.mulPose(Axis.ZP.rotationDegrees((float) skyObject.rotation().z));


        var matrix = poseStack.last().pose();
        var size = skyObject.size();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, skyObject.texture());
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix, -size, 100, -size).uv(1, 0).endVertex();
        bufferBuilder.vertex(matrix, size, 100, -size).uv(0, 0).endVertex();
        bufferBuilder.vertex(matrix, size, 100, size).uv(0, 1).endVertex();
        bufferBuilder.vertex(matrix, -size, 100, size).uv(1, 1).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
        poseStack.popPose();
        RenderSystem.disableBlend();
    }



    public void renderMoon(BufferBuilder bufferBuilder, Matrix4f matrix4f, CustomVanillaObject customVanillaObject) {

        float startX = 0;
        float startY = 1;
        float endX = 1;
        float endY = 0;

        if (customVanillaObject.moonTexture().equals(new ResourceLocation("textures/environment/moon_phases.png"))) {
            int moonPhase = Minecraft.getInstance().level.getMoonPhase();
            int xCoord = moonPhase % 4;
            int yCoord = moonPhase / 4 % 2;
            startX = xCoord / 4.0F;
            startY = yCoord / 2.0F;
            endX = (xCoord + 1) / 4.0F;
            endY = (yCoord + 1) / 2.0F;
        }

        RenderSystem.setShaderTexture(0, customVanillaObject.moonTexture());

        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix4f, -20.0F, -100.0F, 20.0F).uv(endX, endY).endVertex();
        bufferBuilder.vertex(matrix4f, 20.0F, -100.0F, 20.0F).uv(startX, endY).endVertex();
        bufferBuilder.vertex(matrix4f, 20.0F, -100.0F, -20.0F).uv(startX, startY).endVertex();
        bufferBuilder.vertex(matrix4f, -20.0F, -100.0F, -20.0F).uv(endX, startY).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
    }

    public void renderStars(LevelRendererAccessor levelRendererAccessor, float tickDelta, PoseStack poseStack, Matrix4f matrix4f) {
        ClientLevel world = Minecraft.getInstance().level;
        float i = 1.0F - world.getRainLevel(tickDelta);
        float brightness = world.getStarBrightness(tickDelta) * i;

        if (this.properties.stars().allDaysVisible()) {
            RenderSystem.setShaderColor(0.5F, 0.5F, 0.5F, 0.5F);
            FogRenderer.setupNoFog();
            starBuffer.bind();
            starBuffer.drawWithShader(poseStack.last().pose(), matrix4f, GameRenderer.getPositionShader());
            VertexBuffer.unbind();

        } else if (brightness == 0.0F) {
            RenderSystem.setShaderColor(brightness, brightness, brightness, brightness);
            FogRenderer.setupNoFog();
            starBuffer.bind();
            starBuffer.drawWithShader(poseStack.last().pose(), matrix4f, GameRenderer.getPositionShader());
            VertexBuffer.unbind();
        }
    }

    public boolean hasCloud() {
        return this.properties.cloud();
    }

    @Override
    public boolean isActive() {
        Minecraft client = Minecraft.getInstance();
        if(client != null) {
            if(client.level.dimension().location() == LevelStem.OVERWORLD.location()) {
                return false;
            }
        }
        return true;
    }

}
