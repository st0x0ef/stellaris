package com.st0x0ef.stellaris.client.events;

public class ClientEvents {

    public static boolean isCustomClouds;


    public static void registerClientEvents() {
//        ClientTickEvent.CLIENT_LEVEL_POST.register(clientLevel -> {
//            ResourceLocation dimension = clientLevel.dimension().location();
//
//            RenderableType renderableType = SkyRenderer.getRenderableType(dimension);
//            if (renderableType != null) {
//                if (!Objects.equals(renderableType.getCloudType(), "vanilla") && PlanetUtil.getPlanet(dimension) != null) {
//                    if (Objects.equals(SkyRenderer.getRenderableType(dimension).getCloudType(), "none")) {
//                        isCustomClouds = false;
//                    } else {
//                        SkyRenderer.clouds_texture = new ResourceLocation(renderableType.getCloudType());
//                        isCustomClouds = true;
//                    }
//                } else {
//                    isCustomClouds = false;
//                }
//            }
//        });
    }
}
