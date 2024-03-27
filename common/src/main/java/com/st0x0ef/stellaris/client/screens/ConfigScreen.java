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
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {

    private final Map<String, PlainTextButton> TEXT_FIELD = new HashMap<>();

    private final Screen parent;
    public ConfigScreen(Screen parent) {
        super(Component.literal("Stellaris Option"));
        this.parent = parent;
    }



    @Override
    protected void init() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter();
        GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(2);

        CustomConfig.CONFIG.forEach( (string, configEntry) -> {
            if(configEntry.getType() == Boolean.class) {
                Stellaris.LOG.error("Boolean");
            }

            StringWidget widget = new StringWidget(Component.literal(string), this.font);
            rowHelper.addChild(widget);
            addTypeWidgets(configEntry, rowHelper, string);
        });

        rowHelper.addChild(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.saveConfig();
        }).width(200).build(), 2, rowHelper.newCellSettings().paddingTop(6));

        gridLayout.arrangeElements();
        FrameLayout.alignInRectangle(gridLayout, 0, this.height / 6 + 10, this.width, this.height , 0.5F, 0.0F);
        gridLayout.visitWidgets(this::addRenderableWidget);
    }


    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);

        CustomConfig.writeConfigFile("stellaris.json");
        CustomConfig.loadConfigFile();
        //((ClientGuiEvent.SetScreen) screen -> null).modifyScreen();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215);
    }

    public void addTypeWidgets(ConfigEntry entry, GridLayout.RowHelper rowHelper, String entryName) {
        if(entry.getType() == Boolean.class) {
            Checkbox checkbox = Checkbox.builder(Component.literal(entry.getValue().toString()), this.font)
                    .selected((Boolean) entry.getValue())
                    .tooltip(new Tooltip(Component.literal(entry.getDescription()),null))
                    .onValueChange((checkbox1, aBoolean) -> {
                        CustomConfig.CONFIG.replace(entryName, new ConfigEntry<Boolean>(aBoolean,  entry.getDescription()));
                    })
                    .build();
            rowHelper.addChild(checkbox);
        } else if (entry.getType() == String.class || entry.getType() == Integer.class || entry.getType() == Float.class || entry.getType() == Double.class) {
            EditBox button = new EditBox(this.font, 50, 15, Component.literal(entry.getValue().toString()));
            button.setMaxLength(100);
            button.setValue(entry.getValue().toString());
            button.setResponder((string) -> {
                CustomConfig.CONFIG.replace(entryName, new ConfigEntry<String>(string, entry.getDescription()));
            });
            rowHelper.addChild(button);
        } else {
            StringWidget widget = new StringWidget(Component.literal("This config type is not supported. Use the manual config"), this.font);
            rowHelper.addChild(widget);
        }
    }

    public void saveConfig() {
        CustomConfig.writeConfigFile("stellaris.json");
        CustomConfig.loadConfigFile();
        this.onClose();
    }

    @Override
    public void removed() {
        this.minecraft.options.save();
    }


}
