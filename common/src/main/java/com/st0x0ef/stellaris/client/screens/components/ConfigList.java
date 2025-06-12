package com.st0x0ef.stellaris.client.screens.components;

import com.google.common.collect.ImmutableList;
import com.st0x0ef.stellaris.client.screens.ConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ConfigList extends ContainerObjectSelectionList<ConfigList.Entry> {
    private final ConfigScreen screen;

    public ConfigList(Minecraft minecraft, int width, ConfigScreen screen) {
        super(minecraft, width, screen.layout.getContentHeight(), screen.layout.getHeaderHeight(), 25);
        this.centerListVertically = false;
        this.screen = screen;
    }

    public void addBig(AbstractWidget option) {
        this.addEntry(Entry.big(option, this.screen));
    }



    public void addSmall(List<AbstractWidget> options) {
        for(int i = 0; i < options.size(); i += 2) {
            this.addSmall((AbstractWidget)options.get(i), i < options.size() - 1 ? (AbstractWidget)options.get(i + 1) : null);
        }

    }

    public void addSmall(AbstractWidget leftOption, @Nullable AbstractWidget rightOption) {
        this.addEntry(Entry.small(leftOption, rightOption, this.screen));
    }

    public int getRowWidth() {
        return 310;
    }


    public Optional<GuiEventListener> getMouseOver(double mouseX, double mouseY) {
        for(Entry entry : this.children()) {
            for(GuiEventListener guiEventListener : entry.children()) {
                if (guiEventListener.isMouseOver(mouseX, mouseY)) {
                    return Optional.of(guiEventListener);
                }
            }
        }

        return Optional.empty();
    }

    @Environment(EnvType.CLIENT)
    protected static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
        private final List<AbstractWidget> children;
        private final Screen screen;
        private static final int X_OFFSET = 160;

        Entry(List<AbstractWidget> children, Screen screen) {
            this.children = ImmutableList.copyOf(children);
            this.screen = screen;
        }

        public static Entry big(List<AbstractWidget> options, Screen screen) {
            return new Entry(options, screen);
        }

        public static Entry big(AbstractWidget option, Screen screen) {
            return new Entry(ImmutableList.of(option), screen);
        }

        public static Entry small(AbstractWidget leftOption, @Nullable AbstractWidget rightOption, Screen screen) {
            return rightOption == null ? new Entry(ImmutableList.of(leftOption), screen) : new Entry(ImmutableList.of(leftOption, rightOption), screen);
        }

        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            int i = 0;
            int j = this.screen.width / 2 - 155;

            for(AbstractWidget abstractWidget : this.children) {
                abstractWidget.setPosition(j + i, top);
                abstractWidget.render(guiGraphics, mouseX, mouseY, partialTick);
                i += 160;
            }

        }

        public List<? extends GuiEventListener> children() {
            return this.children;
        }

        public List<? extends NarratableEntry> narratables() {
            return this.children;
        }
    }

}
