package com.st0x0ef.stellaris.fabric.client;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.StellarisClient;
import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public class StellarisFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        StellarisClient.initClient();

        SkyRenderer skyRenderer = new SkyRenderer();
        WorldRenderEvents.LAST.register(context -> {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (level != null) {
                skyRenderer.render(level, context.tickDelta(), context.matrixStack(), context.camera(), context.projectionMatrix());
            }
        });
    }
}
