package com.st0x0ef.stellaris.client.screens.helper;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.GaugeTextHelper;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.GaugeValueSerializer;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.IGaugeValue;
import com.st0x0ef.stellaris.common.util.GuiHelper;
import com.st0x0ef.stellaris.common.util.Rectangle2d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractGaugeDataRenderer
{
    private final IGaugeValue value;

    public AbstractGaugeDataRenderer(IGaugeValue value) {
        this.value = value;
    }

    public void toBytes(FriendlyByteBuf buffer, HolderLookup.Provider provider) {
        GaugeValueSerializer.Serializer.write(this.getValue(), buffer, provider);
    }

    public void render(GuiGraphics graphics, int left, int top, int width, int height) {
        this.drawBorder(graphics, left, top, width, height);
        int padding = this.getBorderWidth();
        Rectangle2d innerBounds = new Rectangle2d(left + padding, top + padding, width - padding * 2,
                height - padding * 2);
        this.drawBackground(graphics, innerBounds);
        this.drawContents(graphics, innerBounds);
        this.drawGaugeText(graphics, innerBounds);
    }

    public void render(GuiGraphics graphics, int left, int top) {
        this.render(graphics, left, top, this.getWidth(), this.getHeight());
    }

    protected void drawContents(GuiGraphics graphics, Rectangle2d innerBounds) {

    }

    @Nullable
    public Component getGaugeText() {
        return GaugeTextHelper.getValueText(this.getValue()).build();
    }

    protected void drawGaugeText(GuiGraphics graphics, Rectangle2d innerBounds) {
        Component text = this.getGaugeText();

        if (text != null) {
            int color = this.getTextColor();
            int textPadding = 2;
            Rectangle2d textBounds = new Rectangle2d(innerBounds.getX() + textPadding, innerBounds.getY(),
                    innerBounds.getWidth() - textPadding, innerBounds.getHeight());

            this.drawText(graphics, textBounds, text, color);
        }
    }

    protected void drawText(GuiGraphics graphics, Rectangle2d bounds, Component text, int color) {
        this.drawText(Minecraft.getInstance(), graphics, bounds, text, color);
    }

    protected void drawText(Minecraft minecraft, GuiGraphics graphics, Rectangle2d bounds, Component text, int color) {
        Font fontRenderer = minecraft.font;
        int textWidth = fontRenderer.width(text);

        float scale = Math.min(1.0F, (float) bounds.getWidth() / (float) textWidth);
        float offsetX = 0.0F;
        float offsetY = (float) ((bounds.getHeight() - ((fontRenderer.lineHeight - 1) * scale)) / 2.0F);
        float scaledX = (float) ((bounds.getX() + offsetX) / scale);
        float scaledY = (float) ((bounds.getY() + offsetY) / scale);

        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, scale);
        new GuiGraphics(minecraft, minecraft.renderBuffers().bufferSource()).drawCenteredString(fontRenderer, text, (int) scaledX, (int) scaledY, color);
        graphics.pose().popPose();
    }

    protected void drawBackground(GuiGraphics graphics, Rectangle2d innerBounds) {
        IGaugeValue value = this.getValue();
        int tileColor = value.getColor();
        double displayRatio = value.getDisplayRatio();

        try {

            RenderSystem.enableBlend();
            GuiHelper.setGLColorFromInt(tileColor);

            TextureAtlasSprite tileTexture = this.getBackgroundTileTexture();

            if (tileTexture != null) {
                int tileWidth = this.getBackgroundTileWidth();
                int tileHeight = this.getBackgroundTileHeight();
                int ratioWidth = (int) Math.ceil(innerBounds.getWidth() * displayRatio);
                GuiHelper.drawTiledSprite(graphics, innerBounds.getX(), innerBounds.getY(), ratioWidth,
                        innerBounds.getHeight(), tileTexture, tileWidth, tileHeight);
            }

            ResourceLocation texture = this.getBackgroundTexture();

            if (texture != null) {
                GuiHelper.drawHorizontal(graphics, innerBounds.getX(), innerBounds.getY(), innerBounds.getWidth(),
                        innerBounds.getHeight(), texture, displayRatio);
            }

        } finally {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
        }

    }

    protected void drawBorder(GuiGraphics graphics, int left, int top, int width, int height) {
        int borderColor = this.getBorderColor();
        int padding = this.getBorderWidth();

        graphics.fill(left, top, left + width - padding, top + padding, borderColor);
        graphics.fill(left, top, left + padding, top + height - padding, borderColor);
        graphics.fill(left + width - padding, top, left + width, top + height - padding, borderColor);
        graphics.fill(left, top + height - padding, left + width, top + height, borderColor);
    }

    public int getTextColor() {
        return 0xFFFFFFFF;
    }

    @Nullable
    public TextureAtlasSprite getBackgroundTileTexture() {
        return null;
    }

    @Nullable
    public ResourceLocation getBackgroundTexture() {
        return null;
    }

    public int getBackgroundTileWidth() {
        return 16;
    }

    public int getBackgroundTileHeight() {
        return 16;
    }

    public int getBorderWidth() {
        return 1;
    }

    public int getBorderColor() {
        return 0xFF808080;
    }

    public int getWidth() {
        return 100;
    }

    public int getHeight() {
        return 13;
    }

    public Vec2 getSize() {
        return new Vec2(this.getWidth(), this.getHeight());
    }

    public IGaugeValue getValue() {
        return this.value;
    }
}
