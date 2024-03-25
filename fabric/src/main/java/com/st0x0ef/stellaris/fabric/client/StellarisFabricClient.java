package com.st0x0ef.stellaris.fabric.client;

import com.st0x0ef.stellaris.Stellaris;
import net.fabricmc.api.ClientModInitializer;

public class StellarisFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Stellaris.clientInit();



    }
}