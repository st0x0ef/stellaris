package com.st0x0ef.stellaris.client.skys.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.StellarisClient;
import com.st0x0ef.stellaris.client.skys.helper.SkyHelper;
import com.st0x0ef.stellaris.client.skys.helper.StarHelper;
import com.st0x0ef.stellaris.client.skys.type.RenderableType;
import com.st0x0ef.stellaris.common.events.Events;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SkyRenderer {
    public static final List<RenderableType> renderableList = new ArrayList<>();
    private static final Random random = new Random();
    private static final Minecraft mc = Minecraft.getInstance();
    private static final boolean colorSystem = false;

    @Nullable
    public static ResourceLocation clouds_texture = null;

    @Nullable
    private static VertexBuffer starBuffer = null;

    @Nullable
    public static CloudStatus defaultCloudsLevel = null;

    public static void render(ResourceKey<Level> dimension, float partialTicks, Matrix4f projectionMatrix, Matrix4f viewMatrix, PoseStack poseStack) {
        LevelRenderer renderer = mc.levelRenderer;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        RenderableType renderable = getRenderableType(mc.player.level().dimension());

        if (renderable != null) {
            starBuffer = StarHelper.createStars(0.1F, 190, 160, -1);
        } else {
            starBuffer = StarHelper.createStars(0.1F, 255, 255, 255);
        }

        renderSky(buffer, tesselator, projectionMatrix, viewMatrix, renderer, poseStack, partialTicks);

        if (Events.isCustomClouds && clouds_texture != null && poseStack != null) {
            SkyHelper.renderCustomClouds(poseStack, projectionMatrix, viewMatrix, partialTicks, mc.player.getX(), mc.player.getY(), mc.player.getZ(), clouds_texture);
        }
    }

    private static void renderSky(BufferBuilder buffer, Tesselator tesselator, Matrix4f projectionMatrix, Matrix4f viewMatrix, LevelRenderer renderer, PoseStack poseStack, float partialTicks) {
        float dayTime = mc.level.getTimeOfDay(partialTicks);
        float worldTime = mc.level.getDayTime() + partialTicks;
        float dayAngle = dayTime * 360f % 360f;
        float skyLight = 1 - 2 * Math.abs(dayTime - 0.5f);
        float starLight;

        Matrix4f matrix4f;

        if (getRenderableType(mc.player.level().dimension()).isAllDaysVisible()) {
            starLight = 1.0F;
        } else {
            float rainLevel = 1.0F - mc.level.getRainLevel(partialTicks);
            starLight = mc.level.getStarBrightness(partialTicks) * rainLevel + 0.2F;
        }

        if (starLight > 0.0F) {
            matrix4f = new Matrix4f(viewMatrix).rotate(Axis.XP.rotationDegrees(mc.level.getTimeOfDay(partialTicks) * -360.0F));
            RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
            RenderSystem.setShaderColor(starLight, starLight, starLight, starLight);
            SkyHelper.drawStars(starBuffer, matrix4f, projectionMatrix, GameRenderer.getPositionColorTexShader(), true);
        }

        RenderSystem.setShaderColor(0.8f, 0.8f, 0.8f, 0.8f);
        RenderSystem.depthMask(true);
    }

    public static synchronized RenderableType getRenderableType(ResourceKey<Level> dimension) {
        for (RenderableType renderableType : renderableList) {
            if (renderableType.getDimension() == dimension) {
                return renderableType;
            }
        }
        return null;
    }
}
