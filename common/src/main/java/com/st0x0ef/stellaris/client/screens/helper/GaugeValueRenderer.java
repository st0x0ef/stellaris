package com.st0x0ef.stellaris.client.screens.helper;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.GaugeValueFluidStack;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.GaugeValueSerializer;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.IGaugeValue;
import com.st0x0ef.stellaris.common.util.GuiHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class GaugeValueRenderer extends AbstractGaugeDataRenderer {

    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(Stellaris.MODID,
            "textures/simplegaugevalue.png");

    public GaugeValueRenderer(IGaugeValue value) {
        super(value);
    }

    public GaugeValueRenderer(FriendlyByteBuf buffer) {
        super(GaugeValueSerializer.Serializer.read(buffer));
    }

    @Override
    public TextureAtlasSprite getBackgroundTileTexture() {
        IGaugeValue value = this.getValue();

        if (value instanceof GaugeValueFluidStack) {
            return GuiHelper.getStillFluidSprite(((GaugeValueFluidStack) value).getStack());
        } else {
            return null;
        }
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        IGaugeValue value = this.getValue();
        return value instanceof GaugeValueSimple ? DEFAULT_TEXTURE : null;
    }

}
