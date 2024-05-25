package com.st0x0ef.stellaris.client.screens.temporary;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.helper.ScreenHelper;
import com.st0x0ef.stellaris.common.menus.PlanetSelectionMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MilkyWayScreen extends AbstractContainerScreen<PlanetSelectionMenu> {
    public static final ResourceLocation MILKY_WAY_TEXTURE = new ResourceLocation(Stellaris.MODID,
            "textures/environment/milky_way.png");
    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Stellaris.MODID,
            "textures/gui/planet_selection.png");

    public MilkyWayScreen(PlanetSelectionMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        int milkywayWidth = 256;
        int milkywayHeight = 256;
        int milkywayx = (width - imageWidth) / 2;
        int milkywayy = (height - imageHeight) / 2;

        ScreenHelper.drawTexture(milkywayx, milkywayy, milkywayWidth, milkywayHeight, MILKY_WAY_TEXTURE, true);
    }
}
