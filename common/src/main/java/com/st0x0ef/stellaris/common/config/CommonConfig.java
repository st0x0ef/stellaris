package com.st0x0ef.stellaris.common.config;


public class CommonConfig {

    public boolean debug = false;

    @ConfigManager.ConfigBlock
    public Oil oilConfig = new Oil();

    public static class Oil {
        public int chunkOilChance = 16;


        public int minOil = 10;
        public int maxOil = 50;
    }

}
