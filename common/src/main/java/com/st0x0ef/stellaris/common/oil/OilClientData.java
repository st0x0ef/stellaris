package com.st0x0ef.stellaris.common.oil;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class OilClientData {
    public static Map<Level, Map<ChunkPos, Integer>> oilData = new HashMap<>();

    public static int getOilLevel(Level level, ChunkPos pos) {
        return oilData.get(level).getOrDefault(pos, 0);
    }

    public static void setOilLevel(Level level, ChunkPos pos, int amount) {
        if (!oilData.containsKey(level)) {
            oilData.put(level, new HashMap<>());
        }

        if (!oilData.get(level).containsKey(pos)) {
            oilData.get(level).put(pos, amount);
        } else {
            oilData.get(level).replace(pos, amount);
        }
    }
}
