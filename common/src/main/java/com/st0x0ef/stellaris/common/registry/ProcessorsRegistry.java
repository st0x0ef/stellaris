package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.world.processor.VoidProcessor;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class ProcessorsRegistry {
    public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSORS = DeferredRegister.create(Stellaris.MODID, Registries.STRUCTURE_PROCESSOR);

    public static final RegistrySupplier<StructureProcessorType<VoidProcessor>> STRUCTURE_VOID_PROCESSOR = STRUCTURE_PROCESSORS.register("structure_void_processor", () -> VoidProcessor.CODEC::codec);

}