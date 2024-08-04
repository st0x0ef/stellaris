package com.st0x0ef.stellaris.client.screens.helper;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.GaugeValueFluidStack;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.GaugeValueSerializer;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.GaugeValueSimple;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.IGaugeValue;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class GaugeValueRenderer extends AbstractGaugeDataRenderer {

    private static final ResourceLocation DEFAULT_TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID,
            "textures/simplegaugevalue.png");

    public GaugeValueRenderer(IGaugeValue value) {
        super(value);
    }

    public GaugeValueRenderer(FriendlyByteBuf buffer, HolderLookup.Provider provider) {
        super(GaugeValueSerializer.Serializer.read(buffer, provider));
    }

    @Override
    public TextureAtlasSprite getBackgroundTileTexture() {
        IGaugeValue value = super.getValue();

        if (value instanceof GaugeValueFluidStack) {
            return /*GuiHelper.getStillFluidSprite(((GaugeValueFluidStack) value).getStack())*/ null;
        } else {
            return null;
        }
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        IGaugeValue value = super.getValue();
        return value instanceof GaugeValueSimple ? DEFAULT_TEXTURE : null;
    }

}
