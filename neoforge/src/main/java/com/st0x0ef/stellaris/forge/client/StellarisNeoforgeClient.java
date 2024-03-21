package com.st0x0ef.stellaris.forge.client;

import com.st0x0ef.stellaris.Stellaris;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Stellaris.MODID, value = Dist.CLIENT)
public class StellarisNeoforgeClient {
    @SubscribeEvent
    public static void initializeClient(FMLClientSetupEvent event) {
        Stellaris.clientInit();
    }
}
