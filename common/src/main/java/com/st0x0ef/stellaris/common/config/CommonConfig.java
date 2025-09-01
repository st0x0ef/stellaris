package com.st0x0ef.stellaris.common.config;


public class CommonConfig {
    public int dieselGeneratorFuelTime = 20;
    public double orbitTeleportationYCoord = -10;

    public int radiationCheckInterval = 100;

    public int rocketTpHeight = 600;

    @ConfigManager.InnerConfig
    public Oil oilConfig = new Oil();

    @ConfigManager.InnerConfig
    public Gravity gravityConfig = new Gravity();

    @ConfigManager.InnerConfig
    public Oxygen oxygenConfig = new Oxygen();

    public static class Oil {
        public int chunkOilChance = 16;

        public int minOil = 10;
        public int maxOil = 50;
    }

    public static class Gravity {
        public boolean customEntityGravity = true;
        public boolean customItemGravity = true;
    }

    public static class Oxygen {
        public boolean enableOxygenSystem = true;
        public int oxygenCheckInterval = 20;
        public int maxOxygenRoomSize = 32;
        public float oxygenDamage = 2f;
    }
}
