package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.client.renderers.globe.GlobeItemRenderer;
import net.minecraft.client.Minecraft;

public class ItemRendererRegistry {

    public static final GlobeItemRenderer<?> GLOBE_ITEM_RENDERER = new GlobeItemRenderer<>(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

}