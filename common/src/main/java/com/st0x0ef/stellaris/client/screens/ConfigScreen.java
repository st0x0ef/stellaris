package com.st0x0ef.stellaris.client.screens;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.config.ConfigEntry;
import com.st0x0ef.stellaris.common.config.CustomConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.network.chat.Component;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {

    private final Screen parent;
    private final Map<String, ConfigEntry<?>> config;
    public ConfigScreen(Screen parent) {
        super(Component.literal("Stellaris Config"));
        this.parent = parent;
        config = CustomConfig.CONFIG;

    }

    @Override
    protected void init() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter();
        GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(2);

        config.forEach( (string, configEntry) -> {
            if(configEntry.getType() == Boolean.class) {
                Stellaris.LOG.error("Boolean");
            }

            StringWidget widget = new StringWidget(Component.literal(string), this.font);


            rowHelper.addChild(widget);
            addTypeWidgets(configEntry, rowHelper);

        });

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

    public void addTypeWidgets(ConfigEntry entry, GridLayout.RowHelper rowHelper) {
        if(entry.getType() == Boolean.class) {
            Checkbox checkbox = Checkbox.builder(Component.literal(entry.getValue().toString()), this.font)
                    .tooltip(new Tooltip(Component.literal(entry.getDescription()),null))
                    .build();
            rowHelper.addChild(checkbox);
        } else if (entry.getType() == String.class || entry.getType() == Integer.class || entry.getType() == Float.class || entry.getType() == Double.class) {
            EditBox button = new EditBox(this.font, 50, 30, Component.literal(entry.getValue().toString()));
            rowHelper.addChild(button);
        } else {
            StringWidget widget = new StringWidget(Component.literal("This config type is not supported. Use the manual config"), this.font);
            rowHelper.addChild(widget);
        }
    }

}
