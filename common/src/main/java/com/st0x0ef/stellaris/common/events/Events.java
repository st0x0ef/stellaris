package com.st0x0ef.stellaris.common.events;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.config.CustomConfig;
import com.st0x0ef.stellaris.common.items.RadioactiveBlockItem;
import com.st0x0ef.stellaris.common.items.RadioactiveItem;
import com.st0x0ef.stellaris.common.registry.EffectsRegistry;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.core.NonNullList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.atomic.AtomicLong;

public class Events {

    // Check every 10 seconds
    private static final long RADIOACTIVE_CHECK = 1000 * (long) CustomConfig.CONFIG.get("radioactivityCheckInterval").getValue()  ;
    private static long lastRadioactiveCheck;

    public static void registerEvents() {

       PlayerEvent.OPEN_MENU.register((Player player, AbstractContainerMenu menu) -> {
          NonNullList<ItemStack> items =  menu.getItems();
          items.forEach(item -> {
              if(item.getItem() instanceof RadioactiveItem radioactiveItem) {
                    player.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE.get(), 100, radioactiveItem.getRadiationLevel()));
              } else if (item.getItem() instanceof RadioactiveBlockItem radioactiveBlockItem){
                  player.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE.get(), 100, radioactiveBlockItem.getRadiationLevel()));
              }
          });
       });

       TickEvent.PLAYER_POST.register(player -> {
           long now = System.currentTimeMillis();

           if((now - lastRadioactiveCheck) > RADIOACTIVE_CHECK){
               Stellaris.LOG.error("Checking every " + CustomConfig.CONFIG.get("radioactivityCheckInterval").getValue() + " seconds");
               player.getInventory().items.forEach(itemStack -> {
                   if(itemStack.getItem() instanceof RadioactiveItem radioactiveItem) {
                       player.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE.get(), 100, radioactiveItem.getRadiationLevel()));
                   } else if (itemStack.getItem() instanceof RadioactiveBlockItem radioactiveBlockItem){
                       player.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE.get(), 100, radioactiveBlockItem.getRadiationLevel()));
                   }
               });
               lastRadioactiveCheck = now;
           }

       });
   }
}
