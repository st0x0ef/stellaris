package com.st0x0ef.stellaris.client.screens.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.GUISprites;
import com.st0x0ef.stellaris.client.screens.windows.LaunchWindow;
import com.st0x0ef.stellaris.client.screens.windows.MoveableWindow;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.joml.Vector4i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class LaunchPadsList extends AbstractScrollWidget {

    private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "icon/scroller");

    public Map<Vector4i, LaunchPadWidget> launchPadMap = new HashMap<>();
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
        return finalHeight.get() - finalHeight.get() / 3;
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
            launchPadMap.putIfAbsent(launchPadWidget.buttonPositions, launchPadWidget);

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
            for(Map.Entry<Vector4i, LaunchPadWidget> entry : launchPadMap.entrySet()) {
                Vector4i pos = entry.getKey();
                LaunchPad launchPad = entry.getValue().launchPad;
                ResourceKey<Level> dimension = launchPad.dimension();

                if (Utils.isHoveredOnSprite(pos.x, (int) (pos.y - this.scrollAmount()), pos.z, pos.w, (int) mouseX, (int) mouseY) && window.parent.windowIndex != -1) {
                    if (PlanetUtil.isPlanet(dimension.location())) {
                        if (this.window.parent.canLaunch(PlanetUtil.getPlanet(dimension.location()))) {
                            this.window.parent.tpToFocusedPlanet(launchPad.position(), dimension.location());
                        }
                    } else if (PlanetUtil.isOrbit(dimension.location())) {
                        if (this.window.parent.canLaunch(PlanetUtil.getPlanetFromOrbit(dimension.location()))) {
                            this.window.parent.tpToFocusedPlanet(launchPad.position(), dimension.location());
                        }
                    }

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
        public boolean flames;
        public Vector4i buttonPositions = new Vector4i();

        public TexturedButton launchButton;

        public LaunchPadWidget(LaunchPad launchPad, int x, int y, MoveableWindow window) {
            this.x = x;
            this.y = y;
            this.launchPad = launchPad;
            this.window = window;
            this.flames = new Random().nextInt() == 1; // This can be set to true based on some condition if needed
            launchButton = new TexturedButton(this.x + window.getWidth() - 147, this.y + 5, 60, 20, Component.literal("Launch"), (btn) -> {
            });
        }


        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.blitSprite(GUISprites.WINDOW_BAR, this.x, this.y, window.getWidth() - 80, 30);

            String smallPadName = this.launchPad.name().length() > 20 ? this.launchPad.name().substring(0, 20) + "..." : this.launchPad.name();
            guiGraphics.drawString(getFont(), smallPadName, this.x + 10, this.y + 10, Utils.getColorHexCode("white"));

            this.buttonPositions = new Vector4i(launchButton.getX(), launchButton.getY(), 50, 15);

            this.launchButton.tex(
                    ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button.png"),
                    ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button_hovered.png")
            );
            this.launchButton.render(guiGraphics, mouseX, mouseY, partialTick);

            if (this.launchButton.isHovered()) {
            }

            if(this.launchButton.isHovered() && flames) {
                guiGraphics.blitSprite(GUISprites.FLAMES, this.launchButton.getX() - 9, this.launchButton.getY() - 17, this.launchButton.getWidth() + 13, this.launchButton.getHeight() + 5);

            }
        }


        public Font getFont() {
            return Minecraft.getInstance().font;
        }


    }
}
