package com.st0x0ef.stellaris.common.events;

import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import com.st0x0ef.stellaris.client.skys.type.RenderableType;
import com.st0x0ef.stellaris.common.config.CustomConfig;
import com.st0x0ef.stellaris.common.items.RadiationItem;
import com.st0x0ef.stellaris.common.items.RadioactiveBlockItem;
import com.st0x0ef.stellaris.common.items.RadioactiveItem;
import com.st0x0ef.stellaris.common.registry.EffectsRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class Events {
    private static int tickSinceLastRadioactiveCheck;
    public static boolean isCustomClouds;

    public static void registerEvents() {

       PlayerEvent.OPEN_MENU.register((Player player, AbstractContainerMenu menu) -> {
          NonNullList<ItemStack> items =  menu.getItems();
          items.forEach(item -> {
              if(item.getItem() instanceof RadioactiveItem radioactiveItem) {
                    player.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE, 100, radioactiveItem.getRadiationLevel()));
              } else if (item.getItem() instanceof RadioactiveBlockItem radioactiveBlockItem){
                  player.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE, 100, radioactiveBlockItem.getRadiationLevel()));
              }
          });
       });

       TickEvent.PLAYER_POST.register(player -> {
           if(tickSinceLastRadioactiveCheck == 100) {
               player.getInventory().items.forEach(itemStack -> {
                   if(itemStack.getItem() instanceof RadiationItem radioactiveItem) {
                       player.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE, 100, radioactiveItem.getRadiationLevel()));
                   }
               });
               tickSinceLastRadioactiveCheck = 0;
           }

           tickSinceLastRadioactiveCheck++;

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
