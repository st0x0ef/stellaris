package com.st0x0ef.stellaris.fabric.client;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.StellarisClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public class StellarisFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        StellarisClient.initClient();
    }
}
