package com.st0x0ef.stellaris.neoforge;

import com.st0x0ef.stellaris.Stellaris;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

@Mod(Stellaris.MODID)
public class StellarisNeoForge {
    public StellarisNeoForge(IEventBus bus) {
        Stellaris.init();

        NeoForge.EVENT_BUS.addListener(this::onDatapackSync);

    }

    public void onDatapackSync(OnDatapackSyncEvent event) {

        if (event.getPlayer() != null) {
            Stellaris.onDatapackSyncEvent(event.getPlayer());
        } else {
            event.getPlayerList().getPlayers().forEach((Stellaris::onDatapackSyncEvent));

        }
    }

}