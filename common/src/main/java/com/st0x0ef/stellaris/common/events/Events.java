package com.st0x0ef.stellaris.common.events;

import com.st0x0ef.stellaris.common.items.RadiationItem;
import com.st0x0ef.stellaris.common.oxygen.DimensionOxygenManager;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.oxygen.OxygenRoom;
import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.ChunkEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

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
                manageOxygenRoom(serverLevel, pos, false);
            }
            return EventResult.pass();
        });

        BlockEvent.PLACE.register((level, pos, state, player) -> {
            if (level instanceof ServerLevel serverLevel && state.is(BlocksRegistry.OXYGEN_DISTRIBUTOR)) {
                manageOxygenRoom(serverLevel, pos, true);
            }
            return EventResult.pass();
        });

        ChunkEvent.LOAD_DATA.register((chunk, level, tag) -> {
            if (level instanceof ServerLevel serverLevel) {
                chunk.getBlockEntitiesPos().stream()
                        .filter(pos -> chunk.getBlockState(pos).is(BlocksRegistry.OXYGEN_DISTRIBUTOR))
                        .forEach(pos -> manageOxygenRoom(serverLevel, pos, true));
            }
        });

        TickEvent.SERVER_LEVEL_PRE.register(Events::updateOxygen);
    }

    private static void manageOxygenRoom(ServerLevel level, BlockPos pos, boolean add) {
        DimensionOxygenManager dimensionManager = GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level.dimension());
        if (add) {
            dimensionManager.addOxygenRoom(new OxygenRoom(pos));
        } else {
            dimensionManager.removeOxygenRoom(pos);
        }
    }

    private static void updateOxygen(ServerLevel level) {
        GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level.dimension()).updateOxygen(level);
    }
}
