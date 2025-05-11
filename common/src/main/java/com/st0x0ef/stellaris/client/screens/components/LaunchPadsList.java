package com.st0x0ef.stellaris.client.screens.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.GUISprites;
import com.st0x0ef.stellaris.client.screens.windows.LaunchWindow;
import com.st0x0ef.stellaris.client.screens.windows.MoveableWindow;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector4i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LaunchPadsList extends AbstractScrollWidget {

    private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "icon/scroller");

    public Map<Vector4i, LaunchPad> launchPadMap = new HashMap<>();
    private final AtomicInteger finalHeight = new AtomicInteger(0);
    private final LaunchWindow window;
    public ArrayList<LaunchPad> launchPads;

    public LaunchPadsList(int x, int y, int width, int height, Component message, LaunchWindow window, ArrayList<LaunchPad> launchPads) {
        super(x, y, width, height, message);
        this.window = window;
        this.launchPads = launchPads;
    }

    @Override
    protected int getInnerHeight() {
        return finalHeight.get() ;
    }

    @Override
    protected void renderBorder(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        //We don't want to render the border
    }


    @Override
    protected double scrollRate() {
        return 9;
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        finalHeight.set(0);
        for(int i = 0; i < this.launchPads.size(); i++) {
            int x = this.window.getWindowX() + 40;
            int y = (i * 35);

            LaunchPadWidget launchPadWidget = new LaunchPadWidget(launchPads.get(i), x, getY() + y, this.window);
            launchPadWidget.render(guiGraphics, mouseX, (int) (mouseY + this.scrollAmount()), partialTick);
            launchPadMap.putIfAbsent(launchPadWidget.buttonPositions, launchPads.get(i));

            finalHeight.addAndGet(y);
        }

    }


    @Override
    public boolean isHovered() {
        return super.isHovered();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    @Override
    public void renderScrollBar(GuiGraphics guiGraphics) {
        int i = this.getScrollBarHeight();
        int j = this.getX() + this.width;

        int k = Math.max(this.getY(), (int) this.scrollAmount() * (this.height - i) / this.getMaxScrollAmount() + this.getY());
        RenderSystem.enableBlend();
        guiGraphics.blitSprite(SCROLLER_SPRITE, j, k, 8, i);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if(launchPadMap != null) {
            for(Map.Entry<Vector4i, LaunchPad> entry : launchPadMap.entrySet()) {
                Vector4i pos = entry.getKey();
                LaunchPad launchPad = entry.getValue();

                if (Utils.isHoveredOnSprite(pos.x, (int) (pos.y - this.scrollAmount()), pos.z, pos.w, (int) mouseX, (int) mouseY)) {
                    Stellaris.LOG.error("Clicked on launch pad {}", launchPad.name());
                    this.window.parent.tpToFocusedPlanet(launchPad.position(), this.window.celestialBody);
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    public Font getFont() {
        return Minecraft.getInstance().font;
    }

    public static class LaunchPadWidget {

        public final LaunchPad launchPad;
        public final int x;
        public final int y;
        public final MoveableWindow window;

        public Vector4i buttonPositions = new Vector4i();

        public LaunchPadWidget(LaunchPad launchPad, int x, int y, MoveableWindow window) {
            this.x = x;
            this.y = y;
            this.launchPad = launchPad;
            this.window = window;
        }


        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.blitSprite(GUISprites.SIDEWAYS_ENERGY_FULL, this.x, this.y, window.getWidth() - 80, 30);
            guiGraphics.drawString(getFont(), launchPad.name(), this.x + 5, this.y + 5, Utils.getColorHexCode("white"));

            LaunchButton launchButton = new LaunchButton(this.x + window.getWidth() - 135, this.y + 7, 50, 15, Component.literal("Launch"), (btn) -> {
            });

            buttonPositions = new Vector4i(launchButton.getX(), launchButton.getY(), 50, 15);

            launchButton.setButtonTexture(
                    ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button.png"),
                    ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button_hovered.png")
            );
            launchButton.render(guiGraphics, mouseX, mouseY, partialTick);

        }


        public Font getFont() {
            return Minecraft.getInstance().font;
        }


    }
}
