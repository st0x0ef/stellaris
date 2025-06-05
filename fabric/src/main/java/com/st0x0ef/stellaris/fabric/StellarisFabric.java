package com.st0x0ef.stellaris.fabric;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.registry.*;
import com.st0x0ef.stellaris.platform.fabric.EffectRegisterImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class StellarisFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Stellaris.init();
        BiomeModificationsRegistry.register();
        onAddReloadListener();
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(Stellaris::onDatapackSyncEvent);
        EntityRegistry.registerAttributes((type, builder) -> FabricDefaultAttributeRegistry.register(type.get(), builder.get()));
        EffectRegisterImpl.MOB_EFFECTS.register();
        ItemGroupEvents.modifyEntriesEvent(CreativeTabsRegistry.STELLARIS_TAB.getKey()).register(itemGroup -> {
            for (ItemStack stack : ItemsRegistry.fullItemsToAdd()) {
                itemGroup.accept(stack);
            }
        });

        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.SOLAR_SAND.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.MARS_ICE.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.SULFUR_LEAVES.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.VELVET_LEAVES.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.LUNAR_FOREST_LEAVES.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.MOON_VINES.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.MOON_VINES_PLANT.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.MOON_CROPS.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.VELVET_SPORE_FLOWER.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.VELVET_THORN_GRASS.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.SULFURIC_THORN_PLANT.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.SULFUR_HIGH_PLANT.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.SULFURIC_VEIN_PLANT.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.BLACK_SULFUR_ROOTS.get(), RenderType.cutout());
    }

    public static void onAddReloadListener() {
        Stellaris.onAddReloadListenerEvent((id, listener) -> ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return id;
            }

            @Override
            public @NotNull CompletableFuture<Void> reload(@NotNull PreparationBarrier synchronizer, @NotNull ResourceManager manager, @NotNull ProfilerFiller prepareProfiler, @NotNull ProfilerFiller applyProfiler, @NotNull Executor prepareExecutor, @NotNull Executor applyExecutor) {
                return listener.reload(synchronizer, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor);
            }
        }));
    }
}