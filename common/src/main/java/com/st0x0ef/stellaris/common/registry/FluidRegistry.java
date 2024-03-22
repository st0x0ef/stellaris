package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.fluids.FuelFluid;
import com.st0x0ef.stellaris.common.fluids.HydrogenFluid;
import com.st0x0ef.stellaris.common.fluids.OilFluid;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

public class FluidRegistry {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Stellaris.MODID,Registries.FLUID);

    /** FUEL FLUIDS */
    public static final RegistrySupplier<FlowingFluid> FLOWING_FUEL = FLUIDS.register("flowing_fuel", FuelFluid.Flowing::new);
    public static final RegistrySupplier<FlowingFluid> FUEL_STILL = FLUIDS.register("fuel", FuelFluid.Source::new);

    /** OIL FLUIDS */
    public static final RegistrySupplier<FlowingFluid> FLOWING_OIL = FLUIDS.register("flowing_oil", OilFluid.Flowing::new);
    public static final RegistrySupplier<FlowingFluid> OIL_STILL = FLUIDS.register("oil", OilFluid.Source::new);

    /** HYDROGEN FLUIDS */
    public static final RegistrySupplier<FlowingFluid> FLOWING_HYDROGEN = FLUIDS.register("flowing_hydrogen", HydrogenFluid.Flowing::new);
    public static final RegistrySupplier<FlowingFluid> HYDROGEN_STILL = FLUIDS.register("hydrogen", HydrogenFluid.Source::new);

}
