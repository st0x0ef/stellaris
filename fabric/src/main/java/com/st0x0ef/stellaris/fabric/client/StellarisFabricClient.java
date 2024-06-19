package com.st0x0ef.stellaris.fabric.client;

import com.st0x0ef.stellaris.client.StellarisClient;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import net.fabricmc.api.ClientModInitializer;

public class StellarisFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        StellarisClient.initClient();
        StellarisClient.registerEntityRenderer();
        StellarisClient.registerEntityModelLayer();

        //WorldRenderEvents.AFTER_SETUP.register(SkyRendererFabric::RegisterSkyRenderer);
    }
}
