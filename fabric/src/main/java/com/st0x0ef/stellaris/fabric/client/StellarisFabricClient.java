package com.st0x0ef.stellaris.fabric.client;

import com.st0x0ef.stellaris.client.StellarisClient;
import com.st0x0ef.stellaris.fabric.client.renderer.SkyRendererFabric;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class StellarisFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        StellarisClient.initClient();

        //WorldRenderEvents.AFTER_SETUP.register(SkyRendererFabric::RegisterSkyRenderer);
    }
}
