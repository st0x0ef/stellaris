package com.st0x0ef.stellaris.common.entities.mobs.cheese_boss;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.registry.MemoryModuleTypeRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CheeseBossAi {
    private static final List<SensorType<? extends Sensor<? super CheeseBoss>>> SENSOR_TYPES;
    private static final List<MemoryModuleType<?>> MEMORY_TYPES;
    private static final int SWITCH_COOLDOWN = 20;
    private static final int SPIKES_COOLDOWN = 100;
    private static final int CHEESE_RAIN_COOLDOWN = 100;
    private static final int CHEESE_WHEEL_COOLDOWN = 400;
    private static final int MOLD_BOMB_COOLDOWN = 400;

    protected static Brain<?> makeBrain(CheeseBoss cheeseBoss, Dynamic<?> ops) {
        Brain.Provider<CheeseBoss> provider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<CheeseBoss> brain = provider.makeBrain(ops);
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        setupCooldowns(brain);
        return brain;
    }

    private static void initCoreActivity(Brain<CheeseBoss> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F),
                new MoveToTargetSink(500, 700), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)));
    }

    private static void initFightActivity(Brain<CheeseBoss> brain) {
        brain.addActivityWithConditions(Activity.FIGHT, ImmutableList.of(Pair.of(0, new Switch())),
                Set.of(Pair.of(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryStatus.VALUE_PRESENT)));
    }

    private static void initIdleActivity(Brain<CheeseBoss> brain) {
        brain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(Pair.of(2, new LookAtTargetSink(45, 90)),
                Pair.of(4, new RunOne<>(ImmutableList.of(
                        Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 2),
                        Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 6.0F), 1),
                        Pair.of(RandomStroll.stroll(1.0F), 1),
                        Pair.of(new DoNothing(5, 20), 2))))),
                Set.of());
    }

    public static void setupCooldowns(Brain<CheeseBoss> brain) {
        brain.setMemory(MemoryModuleTypeRegistry.SWITCH_COOLDOWN.get(), SWITCH_COOLDOWN);
        brain.setMemory(MemoryModuleTypeRegistry.SPIKES_COOLDOWN.get(), SPIKES_COOLDOWN);
        brain.setMemory(MemoryModuleTypeRegistry.CHEESE_RAIN_COOLDOWN.get(), CHEESE_RAIN_COOLDOWN);
        brain.setMemory(MemoryModuleTypeRegistry.CHEESE_WHEEL_COOLDOWN.get(), CHEESE_WHEEL_COOLDOWN);
        brain.setMemory(MemoryModuleTypeRegistry.MOLD_BOMB_COOLDOWN.get(), MOLD_BOMB_COOLDOWN);
    }

    static {
        SENSOR_TYPES = List.of(SensorType.NEAREST_PLAYERS, EntityRegistry.CHEESE_BOSS_SENSOR.get());
        MEMORY_TYPES = List.of(MemoryModuleType.NEAREST_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_VISIBLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.PATH,
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.ATTACK_COOLING_DOWN,
                MemoryModuleType.NEAREST_ATTACKABLE,
                MemoryModuleTypeRegistry.CURRENT_PHASE.get(),
                MemoryModuleTypeRegistry.SPIKES_COOLDOWN.get(),
                MemoryModuleTypeRegistry.CHEESE_RAIN_COOLDOWN.get(),
                MemoryModuleTypeRegistry.MOLD_BOMB_COOLDOWN.get(),
                MemoryModuleTypeRegistry.CHEESE_WHEEL_COOLDOWN.get(),
                MemoryModuleTypeRegistry.SWITCH_COOLDOWN.get());
    }

    // Goal for now:
    // Make the cheese boss be able to spawn cheese spikes correctly, with animations and good timing.

    // Memory Module Types:
    // - SWITCH_COOLDOWN
    // - SPIKES_COOLDOWN
    // - CHEESE_RAIN_COOLDOWN
    // - CHEESE_WHEEL_COOLDOWN
    // - MOLD_BOMB_COOLDOWN
    // - CURRENT_PHASE

    // Activities:
    // - Idle
    //   - RandomStroll
    // - Fight
    //   - Switch: Switch phases, cd: 15 seconds
    //   - Approach player for close attack
    //   - Behaviours when in cheddar phase:
    //     - SpikesAttack: Cheese spikes from the ground, cd: 5 seconds
    //     - CheeseRainAttack: Cheese spikes launched around him, cd: 5 seconds
    //   - Behaviours when in camembert phase:
    //     - CheeseWheelAttack: Roll cheese wheel at the player, cd: 5 seconds
    //     - MoldBombAttack: Gives mold effect, cd: 10 seconds
    //   - Behaviours when in babybel phase:
    //     - Run: Run from the player
    //     - When hit, spawn cheeselings

    static class Switch extends Behavior<CheeseBoss> {
        Switch() {
            super(Map.of(MemoryModuleTypeRegistry.SWITCH_COOLDOWN.get(), MemoryStatus.VALUE_PRESENT), 1);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel serverLevel, CheeseBoss cheeseBoss) {
            return cheeseBoss.getBrain().getMemory(MemoryModuleTypeRegistry.SWITCH_COOLDOWN.get()).get() <= 0;
        }

        @Override
        protected boolean canStillUse(ServerLevel serverLevel, CheeseBoss cheeseBoss, long l) {
            return false; // This behavior should only run once
        }

        @Override
        protected void start(ServerLevel serverLevel, CheeseBoss cheeseBoss, long l) {
            serverLevel.getServer().sendSystemMessage(Component.literal(cheeseBoss.getCurrentPhase()));
            switch (cheeseBoss.getBrain().getMemory(MemoryModuleTypeRegistry.CURRENT_PHASE.get()).orElse("cheddar")) {
                case "cheddar" -> cheeseBoss.switchPhase("camembert");
                case "camembert" -> cheeseBoss.switchPhase("babybel");
                default -> cheeseBoss.switchPhase("cheddar");
            }
        }

        @Override
        protected void stop(ServerLevel serverLevel, CheeseBoss cheeseBoss, long l) {
            cheeseBoss.getBrain().setMemory(MemoryModuleTypeRegistry.SWITCH_COOLDOWN.get(), SWITCH_COOLDOWN);
        }
    }
}
