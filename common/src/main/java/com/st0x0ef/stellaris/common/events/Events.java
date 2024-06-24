package com.st0x0ef.stellaris.common.events;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.items.RadiationItem;
import com.st0x0ef.stellaris.common.registry.EffectsRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Events {
    private static int tickBeforeNextRadioactiveCheck = 100;

    public static void registerEvents() {
        TickEvent.PLAYER_POST.register(player -> {
            if ((tickBeforeNextRadioactiveCheck <= 0) && !Utils.isLivingInJetSuit(player)) {
                AtomicBoolean addEffect = new AtomicBoolean();
                AtomicInteger level = new AtomicInteger();
                List<ItemStack> items = new ArrayList<>(player.getInventory().items);

                items.forEach(itemStack -> {
                    if (itemStack.getItem() instanceof RadiationItem radioactiveItem) {
                        addEffect.set(true);

                        if (level.get() < radioactiveItem.getRadiationLevel()) {
                            level.set(radioactiveItem.getRadiationLevel());
                        }
                    }
                });

                if (addEffect.get()) {
                    if(player.level().isClientSide()) return;
                    player.removeEffect(EffectsRegistry.RADIOACTIVE);
                    player.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE, 100, 1));
                }

                tickBeforeNextRadioactiveCheck = 100;
            }

            tickBeforeNextRadioactiveCheck--;
        });

        BlockEvent.PLACE.register((level, pos, blockState, placer) -> {
            if(level.isClientSide) return EventResult.pass();

            ResourceLocation levelLocation = level.dimension().location();
            Block block = blockState.getBlock();
            if (PlanetUtil.isPlanet(levelLocation)) {
                if (!PlanetUtil.hasOxygen(levelLocation)) {
                    if(block instanceof CampfireBlock) {
                        Stellaris.LOG.error("sssss");
                        BlockState newCamp = Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, false).setValue(CampfireBlock.SIGNAL_FIRE, false).setValue(CampfireBlock.WATERLOGGED, true);
                        level.setBlock(pos, blockState, 1);
                        return EventResult.pass();

                    }

                }
            }

            return EventResult.pass();
        });
    }
}