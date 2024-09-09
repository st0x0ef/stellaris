package com.st0x0ef.stellaris.common.oxygen;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class GlobalOxygenManager {
    private static final GlobalOxygenManager INSTANCE = new GlobalOxygenManager();
    private final Map<ResourceKey<Level>, DimensionOxygenManager> dimensionManagers;

    private GlobalOxygenManager() {
        this.dimensionManagers = new HashMap<>();
    }

    public static GlobalOxygenManager getInstance() {
        return INSTANCE;
    }

    public DimensionOxygenManager getOrCreateDimensionManager(ResourceKey<Level> dimension) {
        return dimensionManagers.computeIfAbsent(dimension, DimensionOxygenManager::new);
    }
}
