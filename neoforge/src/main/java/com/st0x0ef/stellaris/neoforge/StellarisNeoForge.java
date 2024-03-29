package com.st0x0ef.stellaris.neoforge;

import com.st0x0ef.stellaris.Stellaris;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Stellaris.MODID)
public class StellarisNeoForge {
    public StellarisNeoForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        NeoForge.EVENT_BUS.register(this);
        Stellaris.init();
    }
}