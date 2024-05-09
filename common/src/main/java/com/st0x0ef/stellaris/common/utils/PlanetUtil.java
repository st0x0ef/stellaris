package com.st0x0ef.stellaris.common.utils;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class PlanetUtil {
    public static Planet getPlanet(ResourceKey<Level> level) {
        return StellarisData.PLANETS.get(level);
    }

    public static boolean isPlanet(ResourceKey<Level> level) {
        return StellarisData.PLANETS.containsKey(level);
    }
    public static boolean hasOxygen(ResourceKey<Level> level) {
        if (isPlanet(level)) {
            return getPlanet(level).oxygen();
        }
        return true;
    }
}
