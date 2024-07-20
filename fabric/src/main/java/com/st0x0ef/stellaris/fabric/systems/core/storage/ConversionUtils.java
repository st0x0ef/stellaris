package com.st0x0ef.stellaris.fabric.systems.core.storage;

import com.st0x0ef.stellaris.common.systems.resources.fluid.FluidResource;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;

public class ConversionUtils {
    public static ItemResource toResource(ItemVariant variant) {
        return ItemResource.of(variant.getItem(), variant.getComponents());
    }

    public static ItemVariant toVariant(ItemResource resource) {
        return ItemVariant.of(resource.getItem(), resource.getDataPatch());
    }

    public static FluidResource toResource(FluidVariant variant) {
        return FluidResource.of(variant.getFluid(), variant.getComponents());
    }

    public static FluidVariant toVariant(FluidResource resource) {
        return FluidVariant.of(resource.getType(), resource.getDataPatch());
    }
}
