package com.st0x0ef.stellaris.fabric.client;

import com.st0x0ef.stellaris.common.config.ConfigEntry;
import com.st0x0ef.stellaris.common.config.CustomConfig;
import dev.architectury.event.events.client.ClientGuiEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.telemetry.TelemetryInfoScreen;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {

    private final Screen parent;
    private final Map<String, ConfigEntry<?>> config;
    public ConfigScreen(Screen parent) {
        super(Component.literal("Stellaris Option"));
        this.parent = parent;
        config = CustomConfig.CONFIG;

    }

    @Override
    protected void init() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter();
        GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(2);

        config.forEach( (string, configEntry) -> {
            Button button1 = Button.builder(Component.literal(configEntry.getValue().toString()), button -> {System.out.println("You clicked button1!");})
                    .size(50,25)
                    .tooltip(new Tooltip(Component.literal(configEntry.getDescription()),null))
                    .build();
            StringWidget widget = new StringWidget(Component.literal(string), this.font);
            rowHelper.addChild(widget);
            rowHelper.addChild(button1);

        });

        //addWidget(button1);

        gridLayout.arrangeElements();
        FrameLayout.alignInRectangle(gridLayout, -120, this.height / 6 , this.width, this.height, 0.5F, 0.0F);
        gridLayout.visitWidgets(this::addRenderableWidget);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        super.keyPressed(i, j, k);

        if (i == 256) {
            this.minecraft.setScreen(new TitleScreen());
            return true;
        }
        return false;
    }

    @Override
    public void onClose() {
        CustomConfig.writeConfigFile("stellaris.json");
        CustomConfig.loadConfigFile();
        //((ClientGuiEvent.SetScreen) screen -> null).modifyScreen();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215);
    }
}
