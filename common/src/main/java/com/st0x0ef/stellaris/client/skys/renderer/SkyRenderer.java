package com.st0x0ef.stellaris.client.skys.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.st0x0ef.stellaris.client.skys.type.RenderInfo;
import com.st0x0ef.stellaris.client.skys.type.Star;
import com.st0x0ef.stellaris.client.skys.type.StarType;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SkyRenderer {
    private static final Random RANDOM = new Random();
    public static final List<RenderInfo> RenderList = new ArrayList<>();
    public static final List<StarType> starType = new ArrayList<>();
    public static List<Star> stars = new ArrayList<>();

    public SkyRenderer() {
        createStars();
    }

    public void render(ClientLevel level, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix) {
        float celestialAngle = level.getTimeOfDay(partialTick);
        updateStars(celestialAngle);

        renderStars(poseStack, camera, projectionMatrix);
    }

    private void createStars() {
        System.out.println("Creating stars...");
        for (int i = 0; i < 1000; i++) {
            double x = RANDOM.nextDouble() * 2.0 - 1.0;
            double y = RANDOM.nextDouble() * 2.0 - 1.0;
            double z = RANDOM.nextDouble() * 2.0 - 1.0;
            double distance = Math.sqrt(x * x + y * y + z * z);
            x /= distance;
            y /= distance;
            z /= distance;
            x *= 100.0;
            y *= 100.0;
            z *= 100.0;

            float size = 1.0F + RANDOM.nextFloat() * 5.0F;
            float r = RANDOM.nextFloat();
            float g = RANDOM.nextFloat();
            float b = RANDOM.nextFloat();

            stars.add(new Star(x, y, z, size, r, g, b));
        }
        System.out.println("Stars created: " + stars.size());
    }

    private void updateStars(float celestialAngle) {
        float rotationAngle = celestialAngle * 360.0f; // Convert celestial angle to degrees
        for (Star star : stars) {
            star.update(rotationAngle);
        }
    }

    private void renderStars(PoseStack poseStack, Camera camera, Matrix4f projectionMatrix) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.depthMask(false);

        poseStack.pushPose();

        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        int renderedStarsCount = 0;
        for (Star star : stars) {
            float size = star.size * 10; // Increase the size of the stars by 10x
            buffer.vertex(star.x - size, star.y - size, star.z).color(star.r, star.g, star.b, 1.0f).endVertex();
            buffer.vertex(star.x + size, star.y - size, star.z).color(star.r, star.g, star.b, 1.0f).endVertex();
            buffer.vertex(star.x + size, star.y + size, star.z).color(star.r, star.g, star.b, 1.0f).endVertex();
            buffer.vertex(star.x - size, star.y + size, star.z).color(star.r, star.g, star.b, 1.0f).endVertex();
            renderedStarsCount++;
        }

        BufferUploader.drawWithShader(buffer.end());

        poseStack.popPose();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();

        System.out.println(renderedStarsCount + " stars rendered");
    }

    public static RenderInfo findByDimensionId(ResourceKey<Level> id) {
        for (RenderInfo renderable : SkyRenderer.RenderList) {
            if (renderable.getDimensionId().equals(id)) {
                return renderable;
            }
        }
        return null;
    }

    public static StarType findByStarDimensionId(ResourceKey<Level> id) {
        for (StarType starType : starType) {
            if (starType.dimension().equals(id)) {
                return starType;
            }
        }
        return null;
    }
}
