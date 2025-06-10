package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.CustomCheckBox;
import com.st0x0ef.stellaris.client.screens.components.TexturedButton;
import com.st0x0ef.stellaris.common.blocks.entities.machines.AntennaBlockEntity;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadLauncher;
import com.st0x0ef.stellaris.common.menus.AntennaMenu;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class AntennaScreen extends AbstractContainerScreen<AntennaMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/antenna.png");

    private final AntennaBlockEntity blockEntity = getMenu().getBlockEntity();

    private EditBox nameBox;
    private EditBox whitelistBox;
    private CustomCheckBox publicCheckbox;
    private TexturedButton saveButton;

    public LaunchPad pad;

    public AntennaScreen(AntennaMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 180;
        imageHeight = 188;
        inventoryLabelY = imageHeight - 94;
        if(menu.launchPadId != -1) {
            this.pad = LaunchPadLauncher.LAUNCH_PADS.launchPads().get(menu.launchPadId);
        }
    }

    @Override
    protected void init() {
        super.init();

        addWidgets(pad);

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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if ((keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_ENTER) && this.nameBox.isFocused()) {
            this.nameBox.setFocused(false);
            return true;

        }

        if ((this.nameBox.isHovered() || this.nameBox.isFocused()) && keyCode == GLFW.GLFW_KEY_E) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
    }

    private void addWidgets(@Nullable LaunchPad pad) {
        this.nameBox = new EditBox(this.font, this.leftPos + 50, this.topPos + 40, 59, 14, Component.translatable("gui.stellaris.launchpad_creator.name"));
        this.whitelistBox = new EditBox(this.font, this.leftPos + 120, this.topPos + 38, 80, 20, Component.translatable("gui.stellaris.launchpad_creator.name"));

        nameBox.setBordered(false);

        this.publicCheckbox = new CustomCheckBox(this.leftPos + 120, this.topPos + 38, 17, Component.literal(""), this.font, false)
                .setTexture(GUISprites.INDUSTRIAL_CHECKBOX, GUISprites.INDUSTRIAL_CHECKBOX_SELECTED);

        this.saveButton = new TexturedButton(this.leftPos + (this.imageWidth / 2 - 30),  this.inventoryLabelY, 60, 20, Component.literal("Create"), (b) -> onClose())
                .tex(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/antenna_button.png"), ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/antenna_button_hovered.png"))
                .showText(true);


        if(pad != null) {
            this.nameBox.setValue(pad.name());
            this.whitelistBox.setValue(String.join(",", pad.whitelist()));
            this.publicCheckbox.setSelected(pad.isPublic());
            this.saveButton.setMessage(Component.literal("Save"));
        }

        this.addRenderableWidget(this.nameBox);
        this.addRenderableWidget(this.saveButton);
        this.addRenderableWidget(this.publicCheckbox);
    }

    @Override
    public void onClose() {
        this.saveLaunchPad();
        super.onClose();
    }

    private void saveLaunchPad() {

        if (this.nameBox.getValue().isEmpty() || this.nameBox.getValue().equals(" ")) return;


        var create = false;
        if (this.pad == null) {
            this.pad = new LaunchPad(
                    LaunchPadLauncher.LAUNCH_PADS.launchPads().size(),
                    Utils.blockPosToVec3(blockEntity.getBlockPos()),
                    blockEntity.getLevel().dimension(),
                    this.nameBox.getValue(),
                    this.publicCheckbox.selected,
                    menu.getPlayer().getDisplayName().getString(),
                    List.of(this.whitelistBox.getValue().split(","))
            );
            create = true;
        } else {
            this.pad = new LaunchPad(pad.id(), pad.position(), pad.dimension(), this.nameBox.getValue(), this.publicCheckbox.selected, pad.owner(), List.of(this.whitelistBox.getValue().split(",")));
        }

        blockEntity.setLaunchPad(this.pad, create);
    }
}