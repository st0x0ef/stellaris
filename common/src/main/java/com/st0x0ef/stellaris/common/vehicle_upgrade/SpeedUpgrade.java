package com.st0x0ef.stellaris.common.vehicle_upgrade;

public class SpeedUpgrade extends VehicleUpgrade
{
    public float speedModifier;

    public SpeedUpgrade(float speedModifier)
    {
        this.speedModifier = speedModifier;
    }

    public float getSpeedModifier() {
        return speedModifier;
    }

    public static SpeedUpgrade getBasicModifier()
    {
        return new SpeedUpgrade(1);
    }
}
