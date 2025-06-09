package com.st0x0ef.stellaris.common.events;

import com.st0x0ef.stellaris.common.blocks.CoalLanternBlock;
import com.st0x0ef.stellaris.common.blocks.RocketLaunchPad;
import com.st0x0ef.stellaris.common.blocks.WallCoalTorchBlock;
import com.st0x0ef.stellaris.common.blocks.entities.machines.AntennaBlockEntity;
import com.st0x0ef.stellaris.common.blocks.machines.AntennaBlock;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadLauncher;
import com.st0x0ef.stellaris.common.network.packets.LaunchPadsOperations;
import com.st0x0ef.stellaris.common.network.packets.SyncLaunchPads;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.registry.EffectsRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Events {
    private static final int RADIATION_CHECK_INTERVAL = 100;
    private static int tickBeforeNextRadioactiveCheck = RADIATION_CHECK_INTERVAL;

    public static void registerEvents() {
        TickEvent.PLAYER_POST.register(player -> {
            if (tickBeforeNextRadioactiveCheck <= 0 && !Utils.isLivingInJetSuit(player)) {
                if (!player.level().isClientSide()) {
                    int maxRadiationLevel = player.getInventory().items.stream()
                            .filter(itemStack -> itemStack.has(DataComponentsRegistry.RADIOACTIVE.get()))
                            .mapToInt(itemStack -> itemStack.get(DataComponentsRegistry.RADIOACTIVE.get()).level())
                            .max()
                            .orElse(0);

                    if (maxRadiationLevel > 0) {
                        player.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE, 100, maxRadiationLevel - 1));
                    }
                }
                tickBeforeNextRadioactiveCheck = RADIATION_CHECK_INTERVAL;
            }

            tickBeforeNextRadioactiveCheck--;
        });

        BlockEvent.BREAK.register((level, pos, state, player, value) -> {
            if (level instanceof ServerLevel serverLevel) {
                if (state.is(BlocksRegistry.OXYGEN_DISTRIBUTOR)) {
                    if (level.getBlockStates(new AABB(pos).inflate(32)).anyMatch(blockState -> blockState.is(BlocksRegistry.OXYGEN_DISTRIBUTOR))) {
                        removeOxygenRoom(serverLevel, pos);
                    }
                } else if(state.is(BlocksRegistry.ROCKET_LAUNCH_PAD)) {

                    if(checkIfAntennaIsNear(pos, level)) {
                        return EventResult.interruptFalse();
                    }

                }
            }
            return EventResult.pass();
        });

        BlockEvent.BREAK.register((level, pos, state, player, xp) -> {
            if(!level.isClientSide() && state.is(BlocksRegistry.ANTENNA)) {

                if(level.getBlockEntity(pos) instanceof AntennaBlockEntity blockEntity) {

                    var launchPos = Utils.blockPosToVec3(pos);

                    NetworkManager.sendToServer(new LaunchPadsOperations(new LaunchPad(blockEntity.launchPadId,
                            launchPos, level.dimension(), "remove", false, "Notch", List.of()), "remove"));

                }
            }
            return EventResult.pass();
        });

        BlockEvent.PLACE.register((level, pos, state, player) -> {
            if (level instanceof ServerLevel serverLevel && !PlanetUtil.hasOxygen(level)) {
                if (state.is(Blocks.TORCH)) {
                    serverLevel.setBlock(pos, BlocksRegistry.COAL_TORCH_BLOCK.get().defaultBlockState(), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.WALL_TORCH)) {
                    serverLevel.setBlock(pos, BlocksRegistry.WALL_COAL_TORCH_BLOCK.get().defaultBlockState().setValue(WallCoalTorchBlock.FACING, state.getValue(WallTorchBlock.FACING)), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.LANTERN)) {
                    serverLevel.setBlock(pos, BlocksRegistry.COAL_LANTERN_BLOCK.get().defaultBlockState().setValue(CoalLanternBlock.HANGING, state.getValue(LanternBlock.HANGING)), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.CAMPFIRE)) {
                    serverLevel.setBlock(pos, state.setValue(CampfireBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.CYAN_CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.BLACK_CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.BLUE_CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.BROWN_CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.GREEN_CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.LIGHT_BLUE_CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.YELLOW_CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.WHITE_CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.RED_CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.PINK_CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.MAGENTA_CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.PURPLE_CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.LIME_CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.ORANGE_CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                } else if (state.is(Blocks.LIGHT_GRAY_CANDLE)) {
                    serverLevel.setBlock(pos, state.setValue(CandleBlock.LIT, false), 3);
                    return EventResult.interruptFalse();
                }
            }

            if(state.is(BlocksRegistry.ANTENNA)) {
                if (level.getBlockState(pos.above()).is(BlocksRegistry.ROCKET_LAUNCH_PAD) && level.getBlockState(pos.above()).getValue(RocketLaunchPad.STAGE)) {
                    return EventResult.pass();
                }
                return EventResult.interruptFalse();
            }

            return EventResult.pass();
        });

        LifecycleEvent.SERVER_STARTED.register((server) -> {
            LevelStorageSource.LevelStorageAccess levelStorageSource = server.storageSource;

            LaunchPadLauncher.loadOrGenerateDefaults(levelStorageSource.getLevelDirectory().path());
        });

        PlayerEvent.PLAYER_JOIN.register((player) -> {
            LevelStorageSource.LevelStorageAccess levelStorageSource = player.server.storageSource;

            LaunchPadLauncher.loadOrGenerateDefaults(levelStorageSource.getLevelDirectory().path());
            NetworkManager.sendToPlayer(player, new SyncLaunchPads(LaunchPadLauncher.LAUNCH_PADS));

        });
    }

    private static void removeOxygenRoom(ServerLevel level, BlockPos pos) {
        GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).removeOxygenRoom(pos);
    }

    private static boolean checkIfAntennaIsNear(BlockPos pos, Level level) {
        return level.getBlockStates(new AABB(pos).inflate(2)).anyMatch(blockState -> blockState.is(BlocksRegistry.ANTENNA));
    }
}
