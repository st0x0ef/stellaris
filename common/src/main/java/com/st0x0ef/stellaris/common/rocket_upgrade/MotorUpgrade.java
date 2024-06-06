package com.st0x0ef.stellaris.common.rocket_upgrade;

public class MotorUpgrade extends RocketUpgrade {
    private final FuelType.Type type;

    public MotorUpgrade(FuelType.Type type) {
        this.type = type;
    }

    public FuelType.Type getFuelType() {
        return type;
    }

    public static MotorUpgrade getBasic() {
        return new MotorUpgrade(FuelType.Type.FUEL);
    }
}
