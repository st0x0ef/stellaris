package com.st0x0ef.stellaris.common.vehicle_upgrade;

import net.minecraft.resources.ResourceLocation;

public class MotorUpgrade extends VehicleUpgrade {

    private final FuelType.Type type;

    public MotorUpgrade(FuelType.Type type) {
        this.type = type;
    }

    @Deprecated
    public MotorUpgrade(FuelType.Type type, ResourceLocation fluidTexture) {
        this(type);
    }

    public FuelType.Type getFuelType() {
        if (this.type == null) {
            return getBasic().getFuelType();
        }
        return this.type;
    }

    @Deprecated
    public ResourceLocation getFluidTexture() {
        return this.getFuelType().getFuelTexture();
    }

    public static MotorUpgrade getBasic() {
        return new MotorUpgrade(FuelType.Type.FUEL);
    }
}
