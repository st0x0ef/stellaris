package com.st0x0ef.stellaris.common.rocket_upgrade;

import com.st0x0ef.stellaris.common.entities.vehicles.base.AbstractRoverBase;
import com.st0x0ef.stellaris.common.entities.vehicles.base.IBaseRover;

public class SpeedUpgrade extends RocketUpgrade
{
    public static int speedModifier;

    public SpeedUpgrade(int speedModifier)
    {
        SpeedUpgrade.speedModifier = speedModifier;
    }

    public static int getSpeedModifier() {
        return speedModifier;
    }

    public static SpeedUpgrade getBasicModifier()
    {
        return new SpeedUpgrade(1);
    }
}
