package com.st0x0ef.stellaris.common.oil;

import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTankHelper;

import java.util.Random;

public class OilUtils {
    public static int getRandomOilLevel() {
        Random random = new Random();
        if(random.nextInt(0, 11) == 10) {
            return FluidTankHelper.convertFromNeoMb(random.nextInt(0, 20) * 1000);
        }

        return 0;
    }
}
