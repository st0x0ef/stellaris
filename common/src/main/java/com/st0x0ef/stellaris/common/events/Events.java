package com.st0x0ef.stellaris.common.events;

import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import com.st0x0ef.stellaris.client.skys.type.RenderableType;
import com.st0x0ef.stellaris.common.items.RadiationItem;
import com.st0x0ef.stellaris.common.registry.EffectsRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Events {
    private static int tickBeforeNextRadioactiveCheck = 100;
    public static boolean isCustomClouds;

    public static void registerEvents() {
       TickEvent.PLAYER_POST.register(player -> {
           if (tickBeforeNextRadioactiveCheck == 0 && !Utils.isLivingInJetSuit(player)) {
               AtomicBoolean addEffect = new AtomicBoolean();
               AtomicInteger level = new AtomicInteger();
               List<ItemStack> items = player.getInventory().items;

               if (player.hasContainerOpen() && !player.containerMenu.getItems().isEmpty()) {
                   items.addAll(player.containerMenu.getItems());
               }

               items.forEach(itemStack -> {
                   if (itemStack.getItem() instanceof RadiationItem radioactiveItem) {
                       addEffect.set(true);
                       if (level.get() < radioactiveItem.getRadiationLevel()) {
                           level.set(radioactiveItem.getRadiationLevel());
                       }
                   }
               });

               if (addEffect.get()) {
                   player.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE, 100, level.get()));
               }

               tickBeforeNextRadioactiveCheck = 100;
           }

           tickBeforeNextRadioactiveCheck--;

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
