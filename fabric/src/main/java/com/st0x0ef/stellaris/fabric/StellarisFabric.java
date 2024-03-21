package com.st0x0ef.stellaris.fabric;

import com.st0x0ef.stellaris.Stellaris;
import net.fabricmc.api.ModInitializer;

public class StellarisFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Stellaris.init();
    }
}