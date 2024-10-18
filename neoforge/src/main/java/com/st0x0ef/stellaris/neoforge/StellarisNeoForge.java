package com.st0x0ef.stellaris.neoforge;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.StellarisClient;
import com.st0x0ef.stellaris.common.registry.CreativeTabsRegistry;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import com.st0x0ef.stellaris.common.world.ModConfiguredFeature;
import com.st0x0ef.stellaris.common.world.ModPlacedFeatures;
import com.st0x0ef.stellaris.neoforge.systems.SystemsNeoForge;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import java.util.Set;

@Mod(Stellaris.MODID)
public class StellarisNeoForge {
    public StellarisNeoForge(IEventBus bus) {
        Stellaris.init();
        NeoForge.EVENT_BUS.addListener(StellarisNeoForge::onAddReloadListenerEvent);
        NeoForge.EVENT_BUS.addListener(StellarisNeoForge::onDatapackSync);
        //NeoForge.EVENT_BUS.addListener(StellarisNeoForge::addItemToTab);

        bus.addListener(StellarisNeoForge::onAttributes);
        SystemsNeoForge.init(bus);

        if (FMLEnvironment.dist.isClient()) {
            StellarisClient.registerPacks();
        }
    }

    public static void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            Stellaris.onDatapackSyncEvent(event.getPlayer(), true);
        } else {
            event.getPlayerList().getPlayers().forEach((player) -> Stellaris.onDatapackSyncEvent(player, true));
        }
    }

    public static void addItemToTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeTabsRegistry.STELLARIS_TAB) {
            for (ItemStack stack : ItemsRegistry.fullItemsToAdd()) {
                event.accept(stack);
            }
        }
    }





    public static void onAddReloadListenerEvent(AddReloadListenerEvent event) {
        Stellaris.onAddReloadListenerEvent((id, listener) -> event.addListener(listener));
    }

    public static void onAttributes(EntityAttributeCreationEvent event) {
        EntityRegistry.registerAttributes((entityType, attribute) -> event.put(entityType.get(), attribute.get().build()));
    }
}