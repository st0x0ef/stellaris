package com.st0x0ef.stellaris.common.data.screen;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.tablet.TabletEntry;
import com.st0x0ef.stellaris.client.screens.tablet.TabletMainScreen;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class TabletPack extends SimpleJsonResourceReloadListener<TabletEntry> {

    public TabletPack() {
        super(TabletEntry.CODEC, FileToIdConverter.json("renderer/tablet"));
    }

    @Override
    protected void apply(Map<ResourceLocation, TabletEntry> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profiler) {
        Stellaris.LOG.info("Loading Assets for Tablet Pack");
        resourceLocationJsonElementMap.forEach((key, entry) -> {
            Stellaris.LOG.info("Loading tablet entry: {}", key);

            if(!TabletMainScreen.ENTRIES.containsKey(entry.id())) {
                TabletMainScreen.ENTRIES.put(entry.id(), entry);
            }
            entry.infos().forEach(info -> TabletMainScreen.INFOS.put(ResourceLocation.fromNamespaceAndPath(entry.id(), info.id()), info));
        });
    }
}
