package com.st0x0ef.stellaris.common.events;

import com.st0x0ef.stellaris.common.blocks.CoalLanternBlock;
import com.st0x0ef.stellaris.common.blocks.WallCoalTorchBlock;
import com.st0x0ef.stellaris.common.items.RadiationItem;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.oxygen.OxygenRoom;
import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.*;
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
            if (level instanceof ServerLevel serverLevel && !PlanetUtil.hasOxygen(serverLevel)) {
                if (state.is(BlocksRegistry.OXYGEN_DISTRIBUTOR)) {
                    addOxygenRoom(serverLevel, pos);
                } else if (serverLevel.getBlockStates(new AABB(pos).inflate(32)).anyMatch(blockState -> blockState.is(BlocksRegistry.OXYGEN_DISTRIBUTOR))) {
                    for (int x = -16; x <= 16; x++) {
                        for (int y = -16; y <= 16; y++) {
                            for (int z = -16; z <= 16; z++) {
                                BlockPos blockPos = pos.offset(x, y, z);
                                if (serverLevel.getBlockState(blockPos).is(BlocksRegistry.OXYGEN_DISTRIBUTOR)) {
                                    checkIfNeedToAddOxygenRoom(serverLevel, blockPos);
                                }
                            }
                        }
                    }
                }

                if (state.is(Blocks.TORCH)) {
                    serverLevel.setBlock(pos, BlocksRegistry.COAL_TORCH_BLOCK.get().defaultBlockState(), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.WALL_TORCH)) {
                    serverLevel.setBlock(pos, BlocksRegistry.WALL_COAL_TORCH_BLOCK.get().defaultBlockState().setValue(WallCoalTorchBlock.FACING, state.getValue(WallTorchBlock.FACING)), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.LANTERN)) {
                    serverLevel.setBlock(pos, BlocksRegistry.COAL_LANTERN_BLOCK.get().defaultBlockState().setValue(CoalLanternBlock.HANGING, state.getValue(LanternBlock.HANGING)), 3);
                    return EventResult.interruptFalse();
                }
            }

            return EventResult.pass();
        });

    }

    private static void addOxygenRoom(ServerLevel level, BlockPos pos) {
        GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).addOxygenRoom(new OxygenRoom(level, pos));
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
}
