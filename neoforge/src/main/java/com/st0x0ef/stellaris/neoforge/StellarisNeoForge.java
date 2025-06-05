package com.st0x0ef.stellaris.neoforge;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.StellarisClient;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import com.st0x0ef.stellaris.platform.neoforge.EffectRegisterImpl;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;


@Mod(Stellaris.MODID)
public class StellarisNeoForge {
    public StellarisNeoForge(IEventBus bus) {
        Stellaris.init();
        NeoForge.EVENT_BUS.addListener(StellarisNeoForge::onAddReloadListenerEvent);
        NeoForge.EVENT_BUS.addListener(StellarisNeoForge::onDatapackSync);
        //NeoForge.EVENT_BUS.addListener(StellarisNeoForge::addItemToTab);
        EffectRegisterImpl.MOB_EFFECTS.register(bus);

        bus.addListener(StellarisNeoForge::onAttributes);
        bus.addListener(StellarisNeoForge::addItemToTab);

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

    @SubscribeEvent
    public static void addItemToTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            for (ItemStack stack : ItemsRegistry.fullItemsToAdd()) {
                event.accept(stack);
            }
        }
    }

    public static void onAddReloadListenerEvent(AddReloadListenerEvent event) {
        if(FMLEnvironment.dist.isClient()) {
            Stellaris.onAddReloadClientListenerEvent((id, listener) -> event.addListener(listener));
        }
        Stellaris.onAddReloadListenerEvent((id, listener) -> event.addListener(listener));
    }

    public static void onAttributes(EntityAttributeCreationEvent event) {
        EntityRegistry.registerAttributes((entityType, attribute) -> event.put(entityType.get(), attribute.get().build()));
    }

    public static void registerBlockRenders() {
        ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.SOLAR_SAND.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.MARS_ICE.get(), RenderType.translucent());

        ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.SULFUR_LEAVES.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.VELVET_LEAVES.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.LUNAR_FOREST_LEAVES.get(), RenderType.cutout());

        ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.MOON_VINES.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.MOON_VINES_PLANT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.MOON_CROPS.get(), RenderType.cutout());

        ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.VELVET_SPORE_FLOWER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.VELVET_THORN_GRASS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.SULFURIC_THORN_PLANT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.SULFUR_HIGH_PLANT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.SULFURIC_VEIN_PLANT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksRegistry.BLACK_SULFUR_ROOTS.get(), RenderType.cutout());
    }
}