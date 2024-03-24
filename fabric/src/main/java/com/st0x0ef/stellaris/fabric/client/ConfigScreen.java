package com.st0x0ef.stellaris.fabric.client;

import com.st0x0ef.stellaris.common.config.CustomConfig;
import dev.architectury.event.events.client.ClientGuiEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {
    public ConfigScreen(Screen parent) {
        super(Component.literal("Stellaris Config"));
        this.parent = parent;
    }

    Button button1;

    @Override
    protected void init() {
        button1 = Button.builder(Component.literal("Button 1"), button -> {System.out.println("You clicked button1!");})
                .size(200,100)
                .tooltip(new Tooltip(Component.literal("Tooltip of button1"),null))
                .build();
        addWidget(button1);
    }

    private final Screen parent;

    @Override
    public void onClose() {
        CustomConfig.writeConfigFile("stellaris.json");
        CustomConfig.loadConfigFile();
        //((ClientGuiEvent.SetScreen) screen -> null).modifyScreen();
    }
}
