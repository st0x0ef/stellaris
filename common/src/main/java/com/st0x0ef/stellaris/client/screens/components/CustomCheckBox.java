package com.st0x0ef.stellaris.client.screens.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class CustomCheckBox extends AbstractButton {

    private ResourceLocation texture = ResourceLocation.withDefaultNamespace("widget/checkbox_selected");;
    private ResourceLocation checkTexture = ResourceLocation.withDefaultNamespace("widget/checkbox");;

    public boolean selected;
    private final CustomCheckBox.OnValueChange onValueChange;
    private final MultiLineTextWidget textWidget;


    public CustomCheckBox(int x, int y, int maxWidth, Component message, Font font, boolean selected) {
        this(x, y, maxWidth, message, font, selected, CustomCheckBox.OnValueChange.NOP);
    }


    public CustomCheckBox(int x, int y, int maxWidth, Component message, Font font, boolean selected, CustomCheckBox.OnValueChange onValueChange) {
        super(x, y, 0, 0, message);
        this.width = this.getAdjustedWidth(maxWidth, message, font);
        this.textWidget = (new MultiLineTextWidget(message, font)).setMaxWidth(this.width).setColor(14737632);
        this.height = this.getAdjustedHeight(font);
        this.selected = selected;
        this.onValueChange = onValueChange;
    }

    public CustomCheckBox setTexture(ResourceLocation texture, ResourceLocation checkTexture) {
        this.texture = texture;
        this.checkTexture = checkTexture;
        return this;
    }

    private int getAdjustedWidth(int maxWidth, Component message, Font font) {
        return Math.min(getDefaultWidth(message, font), maxWidth);
    }

    private int getAdjustedHeight(Font font) {
        return Math.max(getBoxSize(font), this.textWidget.getHeight());
    }

    static int getDefaultWidth(Component message, Font font) {
        return getBoxSize(font) + 4 + font.width(message);
    }

    public static int getBoxSize(Font font) {
        Objects.requireNonNull(font);
        return 9 + 8;
    }

    public void onPress() {
        this.selected = !this.selected;
        this.onValueChange.onValueChange(this, this.selected);
    }

    public CustomCheckBox setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.active) {
            if (this.isFocused()) {
                narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.checkbox.usage.focused"));
            } else {
                narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.checkbox.usage.hovered"));
            }
        }

    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.enableDepthTest();
        Font font = minecraft.font;
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        ResourceLocation resourceLocation = this.selected ? this.texture : this.checkTexture;

        int i = getBoxSize(font);
        guiGraphics.blitSprite(resourceLocation, this.getX(), this.getY(), i, i);
        int j = this.getX() + i + 4;
        int k = this.getY() + i / 2 - this.textWidget.getHeight() / 2;
        this.textWidget.setPosition(j, k);
        this.textWidget.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Environment(EnvType.CLIENT)
    public interface OnValueChange {
        CustomCheckBox.OnValueChange NOP = (checkbox, bl) -> {
        };

        void onValueChange(CustomCheckBox checkbox, boolean bl);
    }


}
