package com.st0x0ef.stellaris.common.events;

import com.st0x0ef.stellaris.common.items.RadiationItem;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.oxygen.OxygenRoom;
import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class Events {
    private static final int RADIATION_CHECK_INTERVAL = 100;
    private static int tickBeforeNextRadioactiveCheck = RADIATION_CHECK_INTERVAL;

    public static void registerEvents() {
        TickEvent.PLAYER_POST.register(player -> {
            if (tickBeforeNextRadioactiveCheck-- <= 0 && !Utils.isLivingInJetSuit(player)) {
                if (!player.level().isClientSide()) {
                    int maxRadiationLevel = player.getInventory().items.stream()
                            .filter(itemStack -> itemStack.getItem() instanceof RadiationItem)
                            .mapToInt(itemStack -> ((RadiationItem) itemStack.getItem()).getRadiationLevel())
                            .max()
                            .orElse(0);

                    if (maxRadiationLevel > 0) {
                        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80));
                    }
                }
                tickBeforeNextRadioactiveCheck = RADIATION_CHECK_INTERVAL;
            }
        });

        BlockEvent.BREAK.register((level, pos, state, player, value) -> {
            if (level instanceof ServerLevel serverLevel && state.is(BlocksRegistry.OXYGEN_DISTRIBUTOR)) {
                if (state.is(BlocksRegistry.OXYGEN_DISTRIBUTOR)) {
                    removeOxygenRoom(serverLevel, pos);
                } else if (level.getBlockStates(new AABB(pos).inflate(32)).anyMatch(blockState -> blockState.is(BlocksRegistry.OXYGEN_DISTRIBUTOR))) {
                    checkIfNeedToRemoveOxygenRoom(serverLevel, pos);
                }
            }
            return EventResult.pass();
        });

        BlockEvent.PLACE.register((level, pos, state, player) -> {
            if (level instanceof ServerLevel serverLevel) {
                if (state.is(BlocksRegistry.OXYGEN_DISTRIBUTOR)) {
                    addOxygenRoom(serverLevel, pos);
                } else if (level.getBlockStates(new AABB(pos).inflate(32)).anyMatch(blockState -> blockState.is(BlocksRegistry.OXYGEN_DISTRIBUTOR))) {
                    checkIfNeedToAddOxygenRoom(serverLevel, pos);
                }
            }
            return EventResult.pass();
        });

        TickEvent.SERVER_LEVEL_PRE.register(Events::updateOxygen);
    }

    private static void addOxygenRoom(ServerLevel level, BlockPos pos) {
        GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).addOxygenRoom(new OxygenRoom(pos));
    }

    private static void checkIfNeedToAddOxygenRoom(ServerLevel level, BlockPos pos) {
        if (GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).getOxygenRoom(pos) == null) {
            addOxygenRoom(level, pos);
        }
    }

    private static void removeOxygenRoom(ServerLevel level, BlockPos pos) {
        GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).removeOxygenRoom(pos);
    }

    private static void checkIfNeedToRemoveOxygenRoom(ServerLevel level, BlockPos pos) {
        OxygenRoom room = GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).getOxygenRoom(pos);
        if (room != null) {
            boolean shouldRemove = false;

            for (Direction direction : Direction.values()) {
                BlockPos adjacentPos = pos.relative(direction);
                BlockState adjacentState = level.getBlockState(adjacentPos);

                if (adjacentState.isAir() && !room.hasOxygenAt(adjacentPos)) {
                    shouldRemove = true;
                    break;
                }
            }

            if (shouldRemove) {
                removeOxygenRoom(level, pos);
            }
        }
    }


    private static void updateOxygen(ServerLevel level) {
        GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).updateOxygen(level);
    }
}
