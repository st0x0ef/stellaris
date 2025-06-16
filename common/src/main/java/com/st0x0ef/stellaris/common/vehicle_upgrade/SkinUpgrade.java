package com.st0x0ef.stellaris.common.vehicle_upgrade;

import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import net.minecraft.resources.ResourceLocation;

public class SkinUpgrade extends VehicleUpgrade {

    public static final ResourceLocation TEXTURE = ResourceLocationUtils.texture("vehicle/rocket_skin/normal/standard");
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

    public String getNameSpace() {
        return rocketSkinLocation.getNamespace();
    }

    public static SkinUpgrade getBasic() {
        return new SkinUpgrade(TEXTURE);
    }
}
