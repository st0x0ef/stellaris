package com.st0x0ef.stellaris.client.screens.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class GaugeChunkWidget extends GaugeWidget {

    protected boolean spriteChanged;

    protected int imageWidth;
    protected int imageHeight;

    public GaugeChunkWidget(int x, int y, int width, int height, Component message, ResourceLocation sprite, @Nullable ResourceLocation overlay_sprite, long capacity, Direction4 direction) {
        super(x, y, width, height, message, sprite, overlay_sprite, capacity, direction);

        spriteChanged = true;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (spriteChanged) {
            SpriteContents contents = guiGraphics.sprites.getSprite(sprite).contents();
            this.imageHeight = contents.height();
            this.imageWidth = contents.width();

            spriteChanged = false;
        }

        switch (DIRECTION) {
            case DOWN_UP -> {
                int i = Mth.ceil(getProgress(amount, capacity) * (getHeight() - 1));
                for (int j = 0; j < width / imageWidth; j++) {
                    guiGraphics.blitSprite(sprite, imageWidth, getHeight(), 0, getHeight() - i, getX() + imageWidth * j, getY() + getHeight() - i, imageWidth, i);
                }
                int x = width % imageWidth;
                if (x > 0) {
                    guiGraphics.blitSprite(sprite, x, getHeight(), 0, getHeight() - i, getX() + width - x, getY() + getHeight() - i, x, i);
                }
            }
            case UP_DOWN -> {
                int i = Mth.ceil(getProgress(amount, capacity) * (getHeight() - 1));
                for (int j = 0; j < width / imageWidth; j++) {
                    guiGraphics.blitSprite(sprite, imageWidth, getHeight(), 0, 0, getX() + imageWidth * j, getY(), imageWidth, i);
                }
                int x = width % imageWidth;
                if (x > 0) {
                    guiGraphics.blitSprite(sprite, x, getHeight(), 0, 0, getX() + width - x, getY(), x, i);
                }
            }
            case LEFT_RIGHT -> {
                int i = Mth.ceil(getProgress(amount, capacity) * (getWidth() - 1));
                for (int j = 0; j < height / imageHeight; j++) {
                    guiGraphics.blitSprite(sprite, getWidth(), imageHeight, 0, 0, getX(), getY() + imageHeight * j, i, imageHeight);
                }
                int y = height % imageHeight;
                if (y > 0) {
                    guiGraphics.blitSprite(sprite, getWidth(), y, 0, 0, getX(), getY() + height - y, i, y);
                }
            }
            case RIGHT_LEFT -> {
                int i = Mth.ceil(getProgress(amount, capacity) * (getWidth() - 1));
                for (int j = 0; j < height / imageHeight; j++) {
                    guiGraphics.blitSprite(sprite, getWidth(), imageHeight, getWidth() - i, 0, getX() + getWidth() - i, getY() + imageHeight * j, i, imageHeight);
                }
                int y = height % imageHeight;
                if (y > 0) {
                    guiGraphics.blitSprite(sprite, getWidth(), y, getWidth() - i, 0, getX() + getWidth() - i, getY() + height - y, i, y);
                }
            }
        }
        if (this.overlay_sprite != null) {
            guiGraphics.blitSprite(overlay_sprite, getX(), getY(), width, height);
        }
    }

    @Override
    public void updateSprite(ResourceLocation sprite) {
        super.updateSprite(sprite);
        spriteChanged = true;
    }
}
