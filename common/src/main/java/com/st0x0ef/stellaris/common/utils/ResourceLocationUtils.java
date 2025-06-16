package com.st0x0ef.stellaris.common.utils;

import com.st0x0ef.stellaris.Stellaris;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocationUtils {
    public static ResourceLocation texture(String path) {
        return id("textures/" + path + ".png");
    }

    public static ResourceLocation guiTexture(String path) {
        return texture("gui/" + path);
    }


    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, path);
    }
}
