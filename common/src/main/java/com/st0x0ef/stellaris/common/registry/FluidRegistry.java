package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

public class FluidRegistry {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Stellaris.MODID, Registries.FLUID);

    /** FUEL FLUIDS */
    private static final ArchitecturyFluidAttributes FUEL_ATTRIBUTES = SimpleArchitecturyFluidAttributes.ofSupplier(() -> FluidRegistry.FLOWING_FUEL, () -> FluidRegistry.FUEL_STILL)
            .blockSupplier(() -> BlocksRegistry.FUEL_BLOCK)
            .bucketItemSupplier(() -> ItemsRegistry.FUEL_BUCKET)
            .slopeFindDistance(4)
            .dropOff(1)
            .tickDelay(8)
            .explosionResistance(100.0F)
            .convertToSource(true)
            .sourceTexture(new ResourceLocation(Stellaris.MODID, "block/fluids/fuel_still"))
            .flowingTexture(new ResourceLocation(Stellaris.MODID, "block/fluids/fuel_flow"));



    public static final RegistrySupplier<FlowingFluid> FLOWING_FUEL = FLUIDS.register("flowing_fuel", () -> new ArchitecturyFlowingFluid.Flowing(FUEL_ATTRIBUTES));
    public static final RegistrySupplier<FlowingFluid> FUEL_STILL = FLUIDS.register("fuel", () -> new ArchitecturyFlowingFluid.Source(FUEL_ATTRIBUTES));


    /** OIL FLUIDS */
    private static final ArchitecturyFluidAttributes OIL_ATTRIBUTES = SimpleArchitecturyFluidAttributes.ofSupplier(() -> FluidRegistry.FLOWING_OIL, () -> FluidRegistry.OIL_STILL)
            .blockSupplier(() -> BlocksRegistry.OIL_BLOCK)
            .bucketItemSupplier(() -> ItemsRegistry.OIL_BUCKET)
            .slopeFindDistance(4)
            .dropOff(1)
            .tickDelay(8)
            .explosionResistance(100.0F)
            .convertToSource(true)
            .sourceTexture(new ResourceLocation(Stellaris.MODID, "block/fluids/oil_still"))
            .flowingTexture(new ResourceLocation(Stellaris.MODID, "block/fluids/oil_flow"));

    public static final RegistrySupplier<FlowingFluid> FLOWING_OIL = FLUIDS.register("flowing_oil", () -> new ArchitecturyFlowingFluid.Flowing(OIL_ATTRIBUTES));
    public static final RegistrySupplier<FlowingFluid> OIL_STILL = FLUIDS.register("oil", () -> new ArchitecturyFlowingFluid.Source(OIL_ATTRIBUTES));

    /** HYDROGEN FLUIDS */
    private static final ArchitecturyFluidAttributes HYDROGEN_ATTRIBUTES = SimpleArchitecturyFluidAttributes.ofSupplier(() -> FluidRegistry.FLOWING_HYDROGEN, () -> FluidRegistry.HYDROGEN_STILL)
            .blockSupplier(() -> BlocksRegistry.HYDROGEN_BLOCK)
            .bucketItemSupplier(() -> ItemsRegistry.HYDROGEN_BUCKET)
            .slopeFindDistance(4)
            .dropOff(1)
            .tickDelay(8)
            .explosionResistance(100.0F)
            .convertToSource(true)
            .sourceTexture(new ResourceLocation(Stellaris.MODID, "block/fluids/hydrogen_still"))
            .flowingTexture(new ResourceLocation(Stellaris.MODID, "block/fluids/hydrogen_flow"));

    public static final RegistrySupplier<FlowingFluid> FLOWING_HYDROGEN = FLUIDS.register("flowing_hydrogen", () -> new ArchitecturyFlowingFluid.Flowing(HYDROGEN_ATTRIBUTES));
    public static final RegistrySupplier<FlowingFluid> HYDROGEN_STILL = FLUIDS.register("hydrogen", () -> new ArchitecturyFlowingFluid.Source(HYDROGEN_ATTRIBUTES));

    /** OXYGEN FLUIDS **/
    private static final ArchitecturyFluidAttributes OXYGEN_ATTRIBUTES = SimpleArchitecturyFluidAttributes.ofSupplier(() -> FluidRegistry.FLOWING_OXYGEN, () -> FluidRegistry.OXYGEN_STILL)
            .blockSupplier(() -> BlocksRegistry.OXYGEN_BLOCK)
            .slopeFindDistance(4)
            .dropOff(1)
            .tickDelay(8)
            .explosionResistance(100)
            .sourceTexture(new ResourceLocation(Stellaris.MODID, "block/fluids/oxygen_still"))
            .flowingTexture(new ResourceLocation(Stellaris.MODID, "block/fluids/oxygen_flow"));

    public static final RegistrySupplier<FlowingFluid> FLOWING_OXYGEN = FLUIDS.register("flowing_oxygen", () -> new ArchitecturyFlowingFluid.Flowing(OXYGEN_ATTRIBUTES));
    public static final RegistrySupplier<FlowingFluid> OXYGEN_STILL = FLUIDS.register("oxygen", () -> new ArchitecturyFlowingFluid.Source(OXYGEN_ATTRIBUTES));
}
