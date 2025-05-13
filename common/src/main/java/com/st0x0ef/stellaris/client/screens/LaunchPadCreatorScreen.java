package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.GaugeWidget;
import com.st0x0ef.stellaris.common.blocks.entities.machines.LaunchPadCreatorBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.PumpjackBlockEntity;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadLauncher;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadUtils;
import com.st0x0ef.stellaris.common.menus.LaunchPadCreatorMenu;
import com.st0x0ef.stellaris.common.menus.PumpjackMenu;
import com.st0x0ef.stellaris.common.oil.OilUtils;
import com.st0x0ef.stellaris.common.utils.Utils;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.SingleFluidStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class LaunchPadCreatorScreen extends AbstractContainerScreen<LaunchPadCreatorMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/window/window_large.png");

    private final LaunchPadCreatorBlockEntity blockEntity = getMenu().getBlockEntity();

    private EditBox nameBox;
    private EditBox whitelistBox;

    private Checkbox publicCheckbox;

    public LaunchPad pad;

    public LaunchPadCreatorScreen(LaunchPadCreatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 180;
        imageHeight = 260;
        inventoryLabelY = imageHeight - 92;
        this.pad = menu.getBlockEntity().launchPad;
    }

    @Override
    protected void init() {
        super.init();
        addWidgets(pad);
        if (blockEntity == null) {
            return;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

    }


    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
    }

    private void addWidgets(@Nullable LaunchPad pad) {
        this.nameBox = new EditBox(this.font, this.leftPos + 10, this.topPos + 10, 160, 20, Component.translatable("gui.stellaris.launchpad_creator.name"));
        this.whitelistBox = new EditBox(this.font, this.leftPos + 10, this.topPos + 40, 160, 20, Component.translatable("gui.stellaris.launchpad_creator.name"));

        var builder = Checkbox.builder(Component.literal("public"), this.font).pos(this.leftPos + 10, this.topPos + 70);
        if(pad != null) {
            this.nameBox.setValue(pad.name());
            this.whitelistBox.setValue(String.join(",", pad.whitelist()));
            builder.selected(pad.isPublic());
        }

        this.publicCheckbox = builder.build();


        this.addWidget(this.nameBox);
        this.addWidget(this.whitelistBox);
        this.addWidget(this.publicCheckbox);
    }

    @Override
    public void onClose() {
        this.saveLaunchPad();
        super.onClose();
    }

    private void saveLaunchPad() {
        var create = false;
        if (this.pad == null) {
            this.pad = new LaunchPad(
                    blockEntity.getBlockPos().getBottomCenter(),
                    blockEntity.getLevel().dimension(),
                    this.nameBox.getValue(),
                    this.publicCheckbox.selected(),
                    menu.getPlayer().getDisplayName().getString(),
                    List.of(this.whitelistBox.getValue().split(","))
            );
            create = true;
        } else {
            this.pad = new LaunchPad(pad.position(), pad.dimension(), this.nameBox.getValue(), this.publicCheckbox.selected(), pad.owner(), List.of(this.whitelistBox.getValue().split(",")));
        }

        if (this.nameBox.getValue() == "" || this.nameBox.getValue() == " ") return;

        blockEntity.setLaunchPad(this.pad, create);
    }
}