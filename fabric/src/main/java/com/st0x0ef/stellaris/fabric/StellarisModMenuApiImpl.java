package com.st0x0ef.stellaris.fabric;

import com.st0x0ef.stellaris.client.screens.ConfigScreen;
import com.terraformersmc.modmenu.ModMenuModMenuCompat;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;

public class StellarisModMenuApiImpl extends ModMenuModMenuCompat {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigScreen::new;
    }
}
