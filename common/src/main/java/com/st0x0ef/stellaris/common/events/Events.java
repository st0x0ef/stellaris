package com.st0x0ef.stellaris.common.events;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.CoalLanternBlock;
import com.st0x0ef.stellaris.common.blocks.RocketLaunchPad;
import com.st0x0ef.stellaris.common.blocks.WallCoalTorchBlock;
import com.st0x0ef.stellaris.common.blocks.entities.machines.AntennaBlockEntity;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadLauncher;
import com.st0x0ef.stellaris.common.network.packets.SyncLaunchPads;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.registry.EffectsRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.*;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.phys.AABB;

public class Events {
    private static final int RADIATION_CHECK_INTERVAL = Stellaris.CONFIG.radiationCheckInterval;
    private static int tickBeforeNextRadioactiveCheck = RADIATION_CHECK_INTERVAL;

    public static void registerEvents() {
        TickEvent.PLAYER_POST.register(player -> {
            if (!player.level().isClientSide()) {
                Utils.handleGravityChange(player, player.level());

                if (tickBeforeNextRadioactiveCheck <= 0 && !Utils.isLivingInJetSuit(player)) {
                    int level = player.getInventory().items.stream()
                            .filter(stack -> stack.has(DataComponentsRegistry.RADIOACTIVE.get()))
                            .mapToInt(stack -> stack.get(DataComponentsRegistry.RADIOACTIVE.get()).level())
                            .max()
                            .orElse(-1);

                    if (level >= 0) {
                        player.addEffect(new MobEffectInstance(EffectsRegistry.getHolder(EffectsRegistry.RADIOACTIVE), 100, level));
                    }

                    tickBeforeNextRadioactiveCheck = RADIATION_CHECK_INTERVAL;
                }
                tickBeforeNextRadioactiveCheck--;
            }
        });

        BlockEvent.BREAK.register((level, pos, state, player, value) -> {
            if (level instanceof ServerLevel serverLevel) {
                if (state.is(BlocksRegistry.OXYGEN_DISTRIBUTOR)) {
                    GlobalOxygenManager.getInstance().getOrCreateDimensionManager(serverLevel).removeOxygenRoom(pos);

                } else if(state.is(BlocksRegistry.ROCKET_LAUNCH_PAD)) {

                    if(checkIfAntennaIsNear(pos, level)) {
                        return EventResult.interruptFalse();
                    }
                } else if(state.is(BlocksRegistry.ANTENNA)) {
                    AntennaBlockEntity antennaBlockEntity = (AntennaBlockEntity) level.getBlockEntity(pos);
                    if (antennaBlockEntity != null && antennaBlockEntity.launchPadId != -1) {
                        LaunchPadLauncher.removeLaunchpad(antennaBlockEntity.launchPadId, serverLevel.getServer());
                    }
                }
            }
            return EventResult.pass();
        });

        BlockEvent.PLACE.register((level, pos, state, player) -> {
            if (level instanceof ServerLevel serverLevel && !PlanetUtil.hasOxygen(level)) {
                if (state.is(Blocks.TORCH)) {
                    serverLevel.setBlockAndUpdate(pos, BlocksRegistry.COAL_TORCH_BLOCK.get().defaultBlockState());
                    return EventResult.interruptFalse();
                }
                else if (state.is(Blocks.WALL_TORCH)) {
                    serverLevel.setBlockAndUpdate(pos, BlocksRegistry.WALL_COAL_TORCH_BLOCK.get().defaultBlockState().setValue(WallCoalTorchBlock.FACING, state.getValue(WallTorchBlock.FACING)));
                    return EventResult.interruptFalse();
                }
                else if (state.is(Blocks.LANTERN)) {
                    serverLevel.setBlockAndUpdate(pos, BlocksRegistry.COAL_LANTERN_BLOCK.get().defaultBlockState().setValue(CoalLanternBlock.HANGING, state.getValue(LanternBlock.HANGING)));
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

            Utils.handleGravityChange(player, player.level());
        });

        TickEvent.SERVER_LEVEL_POST.register((level) -> GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).updateOxygenTick());

        PlayerEvent.PLAYER_RESPAWN.register((player, conqueredEnd, removalReason) -> Utils.handleGravityChange(player, player.level()));
    }

    private static boolean checkIfAntennaIsNear(BlockPos pos, Level level) {
        return level.getBlockStates(new AABB(pos).inflate(1)).anyMatch(blockState -> blockState.is(BlocksRegistry.ANTENNA));
    }

}
