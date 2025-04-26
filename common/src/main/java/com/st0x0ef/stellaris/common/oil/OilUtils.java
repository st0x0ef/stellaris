package com.st0x0ef.stellaris.common.oil;

import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

import java.util.Random;

public class OilUtils {
    public static int getOilLevel(ServerLevel level, ChunkPos pos) {
        return OilSavedData.getData(level).getAmount(pos);
    }

    public static void setOilLevel(ServerLevel level, ChunkPos pos, int amount) {
        OilSavedData.getData(level).setAmount(pos, amount);
    }

    public static int getRandomOilLevel() {
        Random random = new Random();
        if (random.nextInt(0, 16) == 0) {
            return random.nextInt(10, 50) * 1000;
        }

        return 0;
    }

    public static int getOilLevelColor(int oilLevel) {
        if (oilLevel > 40000) {
            return Utils.getColorHexCode("green");
        }
        else if (oilLevel > 0) {
            return Utils.getColorHexCode("orange");
        }
        else {
            return Utils.getColorHexCode("red");
        }
    }
}
