package com.st0x0ef.stellaris.client.skys.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.math.Axis;
import com.st0x0ef.stellaris.client.skys.helper.SkyHelper;
import com.st0x0ef.stellaris.client.skys.helper.StarHelper;
import com.st0x0ef.stellaris.client.skys.type.RenderableType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class SkyRenderer extends DimensionSpecialEffects {
    public static final List<RenderableType> renderableList = new ArrayList<>();
    private static final Minecraft mc = Minecraft.getInstance();

    @Nullable
    private static VertexBuffer starBuffer = null;

    public SkyRenderer() {
        super(Float.NaN, false, SkyType.NONE, false, false);
    }

    public static void render(ResourceKey<Level> dimension, float partialTicks, Matrix4f projectionMatrix, PoseStack poseStack, Matrix4f viewMatrix) {
        LevelRenderer renderer = mc.levelRenderer;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        starBuffer = StarHelper.createStars(0.15F, 190, 160, -1);

        renderSky(buffer, tesselator, partialTicks, projectionMatrix, renderer, poseStack, viewMatrix);
    }

    private static void renderSky(BufferBuilder buffer, Tesselator tesselator, float partialTicks, Matrix4f projectionMatrix, LevelRenderer renderer, PoseStack poseStack, Matrix4f viewMatrix) {
        float dayTime = mc.level.getTimeOfDay(partialTicks);
        float worldTime = mc.level.getDayTime() + partialTicks;
        float dayAngle = dayTime * 360f % 360f;
        float skyLight = 1 - 2 * Math.abs(dayTime - 0.5f);

        Matrix4f matrix4f;
        float rainLevel = 1.0F - mc.level.getRainLevel(partialTicks);
        float starLight = mc.level.getStarBrightness(partialTicks) * rainLevel;

        if (!buffer.building()) {
            try {
                if (starLight > 0.0F) {
                    matrix4f = new Matrix4f(viewMatrix).rotate(Axis.XP.rotationDegrees(mc.level.getTimeOfDay(partialTicks) * -360.0F));
                    RenderSystem.setShaderColor(starLight, starLight, starLight, starLight);
                    SkyHelper.drawStars(starBuffer, matrix4f, projectionMatrix, GameRenderer.getPositionColorShader(), true);
                }
            } catch (IllegalStateException e) {
                System.out.println("BufferBuilder is already building, skipping this render call.");
            }
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
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

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
        return fogColor.multiply(brightness * 0.94F + 0.06F, brightness * 0.94F + 0.06F, brightness * 0.91F + 0.09F);
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return false;
    }
}
