package com.st0x0ef.stellaris.neoforge;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod(Stellaris.MODID)
public class StellarisNeoForge {

    public StellarisNeoForge(IEventBus bus) {
        Stellaris.init();
        NeoForge.EVENT_BUS.addListener(StellarisNeoForge::onAddServerReloadListenersEvent);
        NeoForge.EVENT_BUS.addListener(StellarisNeoForge::onDatapackSync);

        bus.addListener(StellarisNeoForge::onAttributes);
        bus.addListener(StellarisNeoForge::addItemToTab);
    }

    public static void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            Stellaris.onDatapackSyncEvent(event.getPlayer(), true);
        } else {
            event.getPlayerList().getPlayers().forEach((player) -> Stellaris.onDatapackSyncEvent(player, true));
        }
    }

    @SubscribeEvent
    public static void addItemToTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            for (ItemStack stack : ItemsRegistry.fullItemsToAdd()) {
                event.accept(stack);
            }
        }
    }



    public static void onAddServerReloadListenersEvent(AddServerReloadListenersEvent event) {
        Stellaris.onAddReloadListenerEvent(event::addListener);
    }

    public static void onAttributes(EntityAttributeCreationEvent event) {
        EntityRegistry.registerAttributes((entityType, attribute) -> event.put(entityType.get(), attribute.get().build()));
    }
}