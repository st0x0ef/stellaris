package com.st0x0ef.stellaris.client.screens.windows;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class MoveableWindow extends AbstractWidget implements Renderable, GuiEventListener {

    private boolean dragging = false;
    private int windowX;
    private int windowY;
    public double dragOffsetX = 0;
    public double dragOffsetY = 0;
    public int moveLimit;

    public final Map<AbstractWidget, int[]> initialWidgetOffsets = new HashMap<>();

    public Render render;
    public Screen parent;

    public MoveableWindow(int width, int height, Component message, Screen parent) {
        this(width, height, message, parent, null);
    }

    public MoveableWindow(int width, int height, Component message, Screen parent, Render render) {
        super(parent.width / 2 - (width / 2), parent.height / 2 - (height / 2), width, height, message);
        this.parent = parent;
        this.render = render;

        this.windowX = this.getX();
        this.windowY = this.getY();
        this.moveLimit = height;
    }

    public abstract void renderWindow(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);

    public void init() {};

    public void close() {
        this.changeVisibility(false);
    };

    public void renderWidgets(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (AbstractWidget widget : initialWidgetOffsets.keySet()) {

            widget.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    public Consumer<MoveableWindow> resize(Minecraft minecraft, int width, int height) {

        return (window) -> {};
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (render != null) render.render(this);

        this.renderWindow(guiGraphics, mouseX, mouseY, partialTick);

        updateWidgetPositions();

        renderWidgets(guiGraphics, mouseX, mouseY, partialTick);

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && mouseInside(mouseX, mouseY)) {
            dragging = true;
            dragOffsetX = mouseX - this.windowX;
            dragOffsetY = mouseY - this.windowY;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            dragging = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (dragging ) {
            this.windowX = (int) (mouseX - dragOffsetX);
            this.windowY = (int) (mouseY - dragOffsetY);

            this.setX(windowX);
            this.setY(windowY);
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    private void updateWidgetPositions() {
        for (AbstractWidget widget : this.initialWidgetOffsets.keySet()) {
            int[] offset = initialWidgetOffsets.get(widget);
            widget.setX(windowX + offset[0]);
            widget.setY(windowY + offset[1]);

        }
    }

    public void changeVisibility(boolean visible) {
        this.visible = visible;
        for (AbstractWidget widget : this.initialWidgetOffsets.keySet()) {
            widget.visible = visible;
        }
    }

    public <T extends AbstractWidget> void addWidget(T widget) {
        int relativeX = widget.getX() - this.windowX;
        int relativeY = widget.getY() - this.windowY;
        initialWidgetOffsets.put(widget, new int[]{relativeX, relativeY});

    }

    public boolean mouseInside(double mouseX, double mouseY) {
        return mouseX >= this.windowX && mouseX <= this.windowX + this.width && mouseY >= this.windowY && mouseY <= this.windowY + this.getMoveLimit();
    }

    public int getWindowX() {
        return windowX;
    }

    public int getWindowY() {
        return windowY;
    }

    public interface Render {
        void render(MoveableWindow window);
    }

    public int getMoveLimit() {
        return this.height;
    }
}
