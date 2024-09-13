package com.st0x0ef.stellaris.common.oil;

import java.util.Random;

public class OilUtils {

    public static int getRandomOilLevel() {
        Random random = new Random();
        if(random.nextInt(0, 11) == 10) {
            return random.nextInt(0, 10000);

        }
        return 0;


    }

}
