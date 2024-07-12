package com.st0x0ef.stellaris.common.world.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.common.registry.ProcessorsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class VoidProcessor extends StructureProcessor {

    public static final MapCodec<VoidProcessor> CODEC = MapCodec.unit(VoidProcessor::new);


    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        if (structureBlockInfoWorld.state().is(Blocks.STRUCTURE_VOID)) {
            return null;
        }

        if (worldView.getBlockState(structureBlockInfoWorld.pos()).isAir()) {
            return null;
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<VoidProcessor> getType() {
        return ProcessorsRegistry.STRUCTURE_VOID_PROCESSOR.get();
    }
}
