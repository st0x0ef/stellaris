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
        return dimensionManagers.computeIfAbsent(dimension, id -> new DimensionOxygenManager());
    }

    public DimensionOxygenManager getDimensionManager(ResourceKey<Level> dimensionKey) {
        return dimensionManagers.computeIfAbsent(dimensionKey, level -> new DimensionOxygenManager());
    }

    public Map<ResourceKey<Level>, DimensionOxygenManager> getDimensionManagers() {
        return dimensionManagers;
    }
}
