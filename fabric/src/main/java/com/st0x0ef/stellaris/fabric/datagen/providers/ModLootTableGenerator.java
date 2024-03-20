package com.st0x0ef.stellaris.fabric.datagen.providers;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class ModLootTableGenerator extends FabricBlockLootTableProvider {
    public ModLootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        dropSelf(BlocksRegistry.RAW_STEEL_BLOCK.get());
        dropSelf(BlocksRegistry.STEEL_BLOCK.get());
        dropSelf(BlocksRegistry.STEEL_ORE.get());
        dropSelf(BlocksRegistry.DEEPSLATE_STEEL_ORE.get());

    }
}
