package com.st0x0ef.stellaris.fabric.client;

import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class StellarisFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Stellaris.clientInit();



    }
}