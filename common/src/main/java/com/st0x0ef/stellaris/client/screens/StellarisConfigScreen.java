package com.st0x0ef.stellaris.client.screens;

import com.st0x0ef.stellaris.Stellaris;
import fr.tathan.tathanconfig.screen.ConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class StellarisConfigScreen extends ConfigScreen {

    public StellarisConfigScreen(Screen parent) {
        super(parent, Component.literal("Stellaris Config"), Stellaris.CONFIG);
    }

}

