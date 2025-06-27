package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class SoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Stellaris.MODID, Registries.SOUND_EVENT);

    /** SOUNDS */
    public static final RegistrySupplier<SoundEvent> ROCKET_SOUND = SOUNDS.register("rocket_fly", () -> SoundEvent.createVariableRangeEvent(ResourceLocationUtils.id("rocket_fly")));
    public static final RegistrySupplier<SoundEvent> BOOST_SOUND = SOUNDS.register("boost", () -> SoundEvent.createVariableRangeEvent(ResourceLocationUtils.id("boost")));
    public static final RegistrySupplier<SoundEvent> BEEP_SOUND = SOUNDS.register("beep", () -> SoundEvent.createVariableRangeEvent(ResourceLocationUtils.id("beep")));
    public static final RegistrySupplier<SoundEvent> WIND_SOUND = SOUNDS.register("wind", () -> SoundEvent.createVariableRangeEvent(ResourceLocationUtils.id("wind")));
    public static final RegistrySupplier<SoundEvent> RADIOACTIVE = SOUNDS.register("radioactive", () -> SoundEvent.createVariableRangeEvent(ResourceLocationUtils.id("radioactive")));
    public static final RegistrySupplier<SoundEvent> SONIC_BOOM_SOUND = SOUNDS.register("sonic_boom", () -> SoundEvent.createVariableRangeEvent(ResourceLocationUtils.id("sonic_boom")));

    private SoundRegistry() {}

}
