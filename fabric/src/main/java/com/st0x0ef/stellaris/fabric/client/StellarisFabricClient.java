package com.st0x0ef.stellaris.fabric.client;

import com.st0x0ef.stellaris.client.StellarisClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class StellarisFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        StellarisClient.initClient();
        StellarisClient.registerEntityRenderer();
        StellarisClient.registerEntityModelLayer();

        WorldRenderEvents.AFTER_ENTITIES.register(FabricSkyRenderer::renderSky);
    }
}
