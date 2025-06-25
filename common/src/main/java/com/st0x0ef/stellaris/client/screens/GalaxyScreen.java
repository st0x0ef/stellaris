package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.TexturedButton;
import com.st0x0ef.stellaris.client.screens.helper.ScreenHelper;
import com.st0x0ef.stellaris.client.screens.info.GalaxyInfo;
import com.st0x0ef.stellaris.common.menus.GalaxyMenu;
import com.st0x0ef.stellaris.common.network.packets.OpenPlanetScreenPacket;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static com.st0x0ef.stellaris.common.utils.Utils.isHoveredOnSprite;

@Environment(EnvType.CLIENT)
public class GalaxyScreen extends AbstractContainerScreen<GalaxyMenu> {
    public static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID,
            "textures/gui/galaxy_selection.png");
    public static final List<GalaxyInfo> GALAXY = new ArrayList<>();

    public static float rotationAngle = 0;
    public static boolean isPausePressed = false;

    private int selectedGalaxyIndex = 0;

    int tbtWidth;
    int tbtHeight;
    int tbtX;
    int tbtY;

    public GalaxyScreen(GalaxyMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 1200;
        this.imageHeight = 1600;
    }

    @Override
    protected void init() {
        super.init();
        tbtWidth = 32;
        tbtHeight = 32;
        tbtX = this.width - tbtWidth - 10;
        tbtY = this.height - tbtHeight - 10;
        isPausePressed = false;
        TexturedButton button = new TexturedButton(tbtX, tbtY, tbtWidth, tbtHeight, Component.translatable("stellaris.gui.travel_to_planet"),
                (btn) -> {
                    Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
                    NetworkManager.sendToServer(new OpenPlanetScreenPacket(GALAXY.get(selectedGalaxyIndex).id()));
                }).tex(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/arrow_button.png"),
                ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/arrow_button_hover.png"));
        this.addRenderableWidget(button);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderSelectedGalaxy(graphics, partialTicks);
    }

    @Override
    public void onClose() {
        long windowHandle = Minecraft.getInstance().getWindow().getWindow();
        GLFW.glfwSetScrollCallback(windowHandle, Minecraft.getInstance().mouseHandler::onScroll);
        super.onClose();
    }

    public static GalaxyInfo findByNameGalaxy(String id) {
        for (GalaxyInfo body : GalaxyScreen.GALAXY) {
            if (body.id().equals(id)) {
                return body;
            }
        }
        Stellaris.LOG.warn("Galaxy not found : {}", id);
        return null;
    }


    private void renderSelectedGalaxy(GuiGraphics guiGraphics, float partialTicks) {
        Font font = Minecraft.getInstance().font;
        int galaxyWidth = 192;
        int galaxyHeight = 192;
        int galaxyX = (width - galaxyWidth) / 2;
        int galaxyY = (height - galaxyHeight) / 2;

        if (!isPausePressed) {
            rotationAngle -= partialTicks * 0.005f;
        }

        if (!GALAXY.isEmpty()) {
            GalaxyInfo selectedGalaxy = GALAXY.get(selectedGalaxyIndex);
            ScreenHelper.drawTexturewithRotation(guiGraphics, selectedGalaxy.texture(), galaxyX, galaxyY, 0, 0, galaxyWidth, galaxyHeight, galaxyWidth, galaxyHeight, rotationAngle);
            int nameWidth = font.width(selectedGalaxy.getTranslatable());
            guiGraphics.drawString(font, selectedGalaxy.getTranslatable(), (width - nameWidth) / 2, 10, 0xFFFFFF);

            String infoText = "You can change the galaxies with ← →";
            int infoWidth = font.width(infoText);
            guiGraphics.drawString(font, infoText, (width - infoWidth) / 2, height - 30, 0xAAAAAA);


        } else {
            String noGalaxy = "No Galaxies";
            int nameWidth = font.width(noGalaxy);
            guiGraphics.drawString(font, noGalaxy, (width - nameWidth) / 2, 10, 0xFFFFFF);
        }

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT) {
            if (!GALAXY.isEmpty()) {
                selectedGalaxyIndex = (selectedGalaxyIndex - 1 + GALAXY.size()) % GALAXY.size();
            }
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            if (!GALAXY.isEmpty()) {
                selectedGalaxyIndex = (selectedGalaxyIndex + 1) % GALAXY.size();
            }
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_X) {
            isPausePressed = !isPausePressed;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
