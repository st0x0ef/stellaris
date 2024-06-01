package com.st0x0ef.stellaris.neoforge.systems.generic;

import com.st0x0ef.stellaris.common.systems.generic.base.BlockContainerLookup;
import com.st0x0ef.stellaris.common.systems.generic.base.EntityContainerLookup;
import com.st0x0ef.stellaris.common.systems.generic.base.ItemContainerLookup;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeCapsHandler {
    private static final List<NeoForgeBlockContainerLookup<?, ?>> BLOCK_LOOKUPS = new ArrayList<>();
    private static final List<NeoForgeItemContainerLookup<?, ?>> ITEM_LOOKUPS = new ArrayList<>();
    private static final List<NeoForgeEntityContainerLookup<?, ?>> ENTITY_LOOKUPS = new ArrayList<>();

    public static <T, C> NeoForgeBlockContainerLookup<T, C> registerBlockLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        NeoForgeBlockContainerLookup<T, C> lookup = new NeoForgeBlockContainerLookup<>(name, typeClass, contextClass);
        BLOCK_LOOKUPS.add(lookup);
        return lookup;
    }

    public static <T, C> NeoForgeItemContainerLookup<T, C> registerItemLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        NeoForgeItemContainerLookup<T, C> lookup = new NeoForgeItemContainerLookup<>(name, typeClass, contextClass);
        ITEM_LOOKUPS.add(lookup);
        return lookup;
    }

    public static <T, C> NeoForgeEntityContainerLookup<T, C> registerEntityLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        NeoForgeEntityContainerLookup<T, C> lookup = new NeoForgeEntityContainerLookup<>(name, typeClass, contextClass);
        ENTITY_LOOKUPS.add(lookup);
        return lookup;
    }

    public static <T, C> BlockCapability<T, C> getAsCapability(BlockContainerLookup<T, C> lookup) {
        if (lookup instanceof NeoForgeBlockContainerLookup) {
            return ((NeoForgeBlockContainerLookup<T, C>) lookup).getCapability();
        }
        return null;
    }

    public static <T, C> ItemCapability<T, C> getAsCapability(ItemContainerLookup<T, C> lookup) {
        if (lookup instanceof NeoForgeItemContainerLookup) {
            return ((NeoForgeItemContainerLookup<T, C>) lookup).getCapability();
        }
        return null;
    }

    public static <T, C> EntityCapability<T, C> getAsCapability(EntityContainerLookup<T, C> lookup) {
        if (lookup instanceof NeoForgeEntityContainerLookup) {
            return ((NeoForgeEntityContainerLookup<T, C>) lookup).getCapability();
        }
        return null;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (NeoForgeBlockContainerLookup<?, ?> lookup : BLOCK_LOOKUPS) {
            lookup.registerWithCaps(event);
        }

        for (NeoForgeItemContainerLookup<?, ?> lookup : ITEM_LOOKUPS) {
            lookup.registerWithCaps(event);
        }

        for (NeoForgeEntityContainerLookup<?, ?> lookup : ENTITY_LOOKUPS) {
            lookup.registerWithCaps(event);
        }
    }
}
