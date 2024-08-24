package com.st0x0ef.stellaris.common.events;

import com.st0x0ef.stellaris.common.blocks.entities.machines.OxygenGeneratorBlockEntity;
import com.st0x0ef.stellaris.common.items.RadiationItem;
import com.st0x0ef.stellaris.common.oxygen.DimensionOxygenManager;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.oxygen.OxygenRoom;
import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;

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
                    player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80));
                }

                tickBeforeNextRadioactiveCheck = 100;
            }

            tickBeforeNextRadioactiveCheck--;
        });

        BlockEvent.BREAK.register((level, pos, state, player, value) -> {
            if (level instanceof ServerLevel serverLevel) regenerateRoomIfNeeded(serverLevel, pos);
            return EventResult.pass();
        });

        BlockEvent.PLACE.register((level, pos, state, player) -> {
            if (level instanceof ServerLevel serverLevel) regenerateRoomIfNeeded(serverLevel, pos);
            return EventResult.pass();
        });

        TickEvent.SERVER_LEVEL_PRE.register((level) -> GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level.dimension()).updateOxygen(level));
    }

    private static void regenerateRoomIfNeeded(ServerLevel level, BlockPos pos) {
        if (level.getBlockState(pos).equals(BlocksRegistry.OXYGEN_DISTRIBUTOR)) {
            GlobalOxygenManager globalOxygenManager = GlobalOxygenManager.getInstance();
            DimensionOxygenManager dimensionManager = globalOxygenManager.getDimensionManager(level.dimension());

            OxygenRoom oxygenRoom = new OxygenRoom((OxygenGeneratorBlockEntity) level.getBlockEntity(pos));
            dimensionManager.addOxygenSystem(oxygenRoom);

            return;
        }

        for (DimensionOxygenManager manager : GlobalOxygenManager.getInstance().getDimensionManagers().values()) {
            for (OxygenRoom system : manager.getOxygenSystems()) {
                if (isWithinGeneratorArea(system.getGeneratorPosition(), pos)) {
                    system.updateOxygenRoom(level);
                    return;
                }
            }
        }
    }

    private static boolean isWithinGeneratorArea(BlockPos generatorPos, BlockPos blockPos) {
        int distanceX = Math.abs(blockPos.getX() - generatorPos.getX());
        int distanceY = Math.abs(blockPos.getY() - generatorPos.getY());
        int distanceZ = Math.abs(blockPos.getZ() - generatorPos.getZ());
        return distanceX <= 16 && distanceY <= 16 && distanceZ <= 16;
    }
}