package com.st0x0ef.stellaris.common.entities.mobs.cheese_boss;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Dynamic;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;

public class CheeseBossAi {
    private static final List<SensorType<? extends Sensor<? super CheeseBoss>>> SENSOR_TYPES;
    private static final List<MemoryModuleType<?>> MEMORY_TYPES;

    public CheeseBossAi() {
    }

    protected static Brain<?> makeBrain(CheeseBoss cheeseBoss, Dynamic<?> ops) {
        Brain.Provider<CheeseBoss> provider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<CheeseBoss> brain = provider.makeBrain(ops);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    static {
        SENSOR_TYPES = List.of(SensorType.NEAREST_PLAYERS, EntityRegistry.CHEESE_BOSS_SENSOR.get());
        MEMORY_TYPES = List.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.ROAR_TARGET, MemoryModuleType.DISTURBANCE_LOCATION, MemoryModuleType.RECENT_PROJECTILE, MemoryModuleType.IS_SNIFFING, MemoryModuleType.IS_EMERGING, MemoryModuleType.ROAR_SOUND_DELAY, MemoryModuleType.DIG_COOLDOWN, MemoryModuleType.ROAR_SOUND_COOLDOWN, MemoryModuleType.SNIFF_COOLDOWN, MemoryModuleType.TOUCH_COOLDOWN, MemoryModuleType.VIBRATION_COOLDOWN, MemoryModuleType.SONIC_BOOM_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_DELAY);
    }
}
