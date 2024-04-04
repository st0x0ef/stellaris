package com.st0x0ef.stellaris.neoforge.client;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.StellarisClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Stellaris.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StellarisNeoforgeClient {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        StellarisClient.registerEntityModelLayer();
        event.enqueueWork(StellarisClient::registerParticle);
        event.enqueueWork(StellarisClient::registerEntityRenderer);
        event.enqueueWork(StellarisClient::registerScreen);
    }
}
