package com.st0x0ef.stellaris.platform.systems.fluid;

import com.st0x0ef.stellaris.common.systems.fluid.base.FluidHolder;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.NotImplementedException;

public class ClientFluidHooks {
    @ExpectPlatform
    public static TextureAtlasSprite getFluidSprite(FluidHolder fluid) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static int getFluidColor(FluidHolder fluid) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static int getFluidLightLevel(FluidHolder fluid) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static Component getDisplayName(FluidHolder fluid) {
        throw new NotImplementedException();
    }
}
