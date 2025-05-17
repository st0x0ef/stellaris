package com.st0x0ef.stellaris.common.config;


public class CommonConfig {

    public boolean debug = false;

    @ConfigManager.InnerConfig
    public Oil oilConfig = new Oil();

    @ConfigManager.InnerConfig
    public Gravity gravityConfig = new Gravity();


    public float oxygenDamage = 2f;

    public static class Oil {
        public int chunkOilChance = 16;

        public int minOil = 10;
        public int maxOil = 50;
    }

    public static class Gravity {
        public boolean customEntityGravity = true;
        public boolean customItemGravity = true;

    }


}
