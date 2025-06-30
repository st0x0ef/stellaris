package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.CustomCheckBox;
import com.st0x0ef.stellaris.common.blocks.entities.FlagBlockEntity;
import com.st0x0ef.stellaris.common.menus.DieselGeneratorMenu;
import com.st0x0ef.stellaris.common.menus.FlagUploadMenu;
import com.st0x0ef.stellaris.common.network.packets.SendImagePacket;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.st0x0ef.stellaris.Stellaris.LOG;

@Environment(EnvType.CLIENT)
public class FlagUploadScreen extends AbstractContainerScreen<FlagUploadMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/antenna.png");


    private CustomCheckBox usePlayerHead;
    private CustomCheckBox useCustomPNG;

    private FlagBlockEntity flagBlockEntity;

    public FlagUploadScreen(FlagUploadMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 180;
        this.imageHeight = 188;
        this.inventoryLabelY = imageHeight - 94;
        this.flagBlockEntity =  menu.getBlockEntity();
    }


    @Override
    protected void init() {
        this.usePlayerHead = new CustomCheckBox(10, 10, 20,  Component.literal("Enable Flag Upload"), this.font, true)
                .setSelected(flagBlockEntity.flagState.playerHead)
                .setOnValueChange((checkbox, bl) -> this.switchState());

        this.addRenderableWidget(usePlayerHead);

        this.useCustomPNG = new CustomCheckBox(10, 30, 20,  Component.literal("Use Custom Image"), this.font, false)
                .setSelected(flagBlockEntity.flagState.customPng)
                .setOnValueChange((checkbox, bl) -> this.switchState());
        this.addRenderableWidget(useCustomPNG);

    }

    public void switchState() {
        if(this.usePlayerHead.selected) {
            this.usePlayerHead.setSelected(false);
            this.useCustomPNG.setSelected(true);
        } else {
            this.usePlayerHead.setSelected(true);
            this.useCustomPNG.setSelected(false);
        }

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    public void onFilesDrop(List<Path> packs) {
        if (packs.size() > 1) LOG.warn("Can only upload one image at a time");
        Path texture = packs.getFirst();
        File file = new File(texture.toUri());
        if (!file.getName().toLowerCase().endsWith(".png")) {
            LOG.error("Uploaded file is not a PNG");
            return;
        }

        try {
            ImageIO.read(file).toString();
        } catch (Exception e) {
            LOG.error("Uploaded file is not an image");
            return;
        }

        try {
            byte[] bytes = Files.readAllBytes(texture);
            if (bytes.length > 16 * 1024 * 1024) {
                LOG.error("Uploaded file was too large");
                return;
            }

            this.usePlayerHead.setSelected(false);
            this.useCustomPNG.setSelected(true);
            NetworkManager.sendToServer(new SendImagePacket(bytes));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClose() {
        super.onClose();

        flagBlockEntity.setFlagState(FlagBlockEntity.STATE.fromValues(this.usePlayerHead.selected, this.useCustomPNG.selected));
    }
}
