package com.st0x0ef.stellaris.client.screens;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.windows.MoveableWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BaseWindowScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    public ArrayList<AbstractWidget> guiEventListeners = new ArrayList<>();
    public ArrayList<MoveableWindow> moveableWindows = new ArrayList<>();

    public BaseWindowScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    }

    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
        T widget1 = super.addRenderableWidget(widget);

        if (widget1 instanceof MoveableWindow window) {
            Stellaris.LOG.info("Adding moveable window " + window);
            window.init();
            moveableWindows.add(window);
            guiEventListeners.add(window);
            guiEventListeners.addAll(window.initialWidgetOffsets.keySet());

        }
        return super.addRenderableWidget(widget);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        for(GuiEventListener listener : this.guiEventListeners) {
            listener.mouseMoved(mouseX, mouseY);
        }

        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {

        for(GuiEventListener listener : this.moveableWindows) {
            listener.mouseReleased(mouseX, mouseY, button);
        }
        for(GuiEventListener listener : this.guiEventListeners) {
            listener.mouseReleased(mouseX, mouseY, button);
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {

        for(GuiEventListener listener : this.guiEventListeners) {
            listener.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        for(GuiEventListener listener : this.guiEventListeners) {
            listener.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }


    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        for(GuiEventListener listener : this.guiEventListeners) {
            listener.charTyped(codePoint, modifiers);
        }

        return super.charTyped(codePoint, modifiers);
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for(GuiEventListener listener : this.guiEventListeners) {
            listener.keyPressed(keyCode, scanCode, modifiers);
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(GuiEventListener listener : this.guiEventListeners) {
            listener.setFocused(true);
            listener.mouseClicked(mouseX, mouseY, button);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        Map<MoveableWindow, Consumer<MoveableWindow>> resizeConsumers = new HashMap<>();

        for(MoveableWindow window : moveableWindows) {
            resizeConsumers.putIfAbsent(window, window.resize(minecraft, width, height));
        }
        super.resize(minecraft, width, height);

        resizeConsumers.forEach((window, consumer) -> consumer.accept(window));

    }
}
