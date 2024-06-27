package com.st0x0ef.stellaris.client.events;

import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import com.st0x0ef.stellaris.client.skys.type.RenderableType;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class ClientEvents {

    public static boolean isCustomClouds;


    public static void registerClientEvents() {
        ClientTickEvent.CLIENT_LEVEL_POST.register(clientLevel -> {
            ResourceLocation dimension = clientLevel.dimension().location();

            RenderableType renderableType = SkyRenderer.getRenderableType(dimension);
            if (renderableType != null) {
                if (!Objects.equals(renderableType.getCloudType(), "vanilla") && PlanetUtil.getPlanet(dimension) != null) {
                    if (Objects.equals(SkyRenderer.getRenderableType(dimension).getCloudType(), "none")) {
                        isCustomClouds = false;
                    } else {
                        SkyRenderer.clouds_texture = new ResourceLocation(renderableType.getCloudType());
                        isCustomClouds = true;
                    }
                } else {
                    isCustomClouds = false;
                }
            }
        });
    }
}
