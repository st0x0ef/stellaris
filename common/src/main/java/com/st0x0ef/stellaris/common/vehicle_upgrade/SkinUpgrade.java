package com.st0x0ef.stellaris.common.vehicle_upgrade;

import net.minecraft.resources.ResourceLocation;

import static com.st0x0ef.stellaris.Stellaris.id;

public class SkinUpgrade extends VehicleUpgrade {

    public static final ResourceLocation TEXTURE = id("textures/vehicle/rocket_skin/normal/standard.png");
    final private ResourceLocation rocketSkinLocation;

    public SkinUpgrade(ResourceLocation rocketSkinLocation) {
        this.rocketSkinLocation = rocketSkinLocation;
    }

    public ResourceLocation getRocketSkinLocation() {
        return rocketSkinLocation;
    }

    public String getRocketSkinName() {
        String[] string = rocketSkinLocation.toString().split("/");
        return string[string.length - 1].replace(".png", "");
    }

    public String getNameSpace()  {
        return rocketSkinLocation.getNamespace();
    }

    public static SkinUpgrade getBasic() {
        return new SkinUpgrade(TEXTURE);
    }
}
