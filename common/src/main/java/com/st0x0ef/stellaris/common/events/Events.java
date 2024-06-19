package com.st0x0ef.stellaris.common.events;

import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import com.st0x0ef.stellaris.client.skys.type.RenderableType;
import com.st0x0ef.stellaris.common.items.RadiationItem;
import com.st0x0ef.stellaris.common.registry.EffectsRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Objects;

public class Events {
    private static int tickSinceLastRadioactiveCheck = 100;
    public static boolean isCustomClouds;

    public static void registerEvents() {
       TickEvent.PLAYER_POST.register(player -> {
           if (tickSinceLastRadioactiveCheck == 0) {
               player.getInventory().items.forEach(itemStack -> {
                   if (itemStack.getItem() instanceof RadiationItem radioactiveItem) {
                       player.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE, 100, radioactiveItem.getRadiationLevel()));
                   }
               });

               if (player.hasContainerOpen()) {
                   player.containerMenu.getItems().forEach(itemStack -> {
                       if (itemStack.getItem() instanceof RadiationItem radioactiveItem) {
                           player.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE, 100, radioactiveItem.getRadiationLevel()));
                       }
                   });
               }
               tickSinceLastRadioactiveCheck = 100;
           }

           tickSinceLastRadioactiveCheck--;

           RenderableType renderableType = SkyRenderer.getRenderableType(player.level().dimension());

           if (renderableType != null) {
               if (!Objects.equals(renderableType.getCloudType(), "vanilla") && PlanetUtil.getPlanet(player.level().dimension()) != null) {
                   if (Objects.equals(SkyRenderer.getRenderableType(player.level().dimension()).getCloudType(), "none")) {
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
