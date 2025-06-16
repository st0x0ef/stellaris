package com.st0x0ef.stellaris.client.screens.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class StateButton extends AbstractButton {
    private static final WidgetSprites SPRITES = new WidgetSprites(ResourceLocation.withDefaultNamespace("widget/button"), ResourceLocation.withDefaultNamespace("widget/button_disabled"), ResourceLocation.withDefaultNamespace("widget/button_highlighted"));

    public boolean state;
    public int activeColor = 16777215;
    public int inactiveColor = 16777215;
    public Component[] label = new Component[]{
            Component.literal("False"),
            Component.translatable("True")
    };

    public StateButton(int x, int y, int width, int height, Component message, boolean defaultState) {
        super(x, y, width, height, message);

        this.state = defaultState;
    }


    public StateButton setColor(int active, int inactive) {
        this.activeColor = active;
        this.inactiveColor = inactive;
        return this;
    }

    public StateButton setLabel(Component active, Component inactive) {
        this.label[0] = active;
        this.label[1] = inactive;
        return this;
    }

    public Boolean switchState() {
        this.state = !this.state;
        return this.state;
    }

    @Override
    public Component getMessage() {
        return label[state ? 1 : 0];
    }

    @Override
    public void onPress() {
        this.switchState();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        guiGraphics.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        int i = this.state ? activeColor : inactiveColor;
        this.renderString(guiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public static StateButton fromButton(Button button, boolean defaultState) {
        return new StateButton(button.getX(), button.getY(), button.getWidth(), button.getHeight(), button.getMessage(), defaultState);
    }
}
