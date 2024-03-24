package com.st0x0ef.stellaris.fabric;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.fabric.client.ConfigScreen;
import dev.architectury.event.events.client.ClientTickEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import static com.st0x0ef.stellaris.Stellaris.TEST_KEYMAPPING;

public class StellarisFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Stellaris.init();
    }

    public class StellarisFabricClient implements ClientModInitializer {



        @Override
        public void onInitializeClient() {
            Stellaris.clientInit();
            ClientTickEvent.CLIENT_POST.register(minecraft -> {
                while (TEST_KEYMAPPING.consumeClick()) {
                    minecraft.setScreen(new ConfigScreen(minecraft.screen));
                }
            });
        }
    }
}