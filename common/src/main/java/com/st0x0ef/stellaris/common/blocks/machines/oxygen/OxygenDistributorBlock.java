package com.st0x0ef.stellaris.common.blocks.machines.oxygen;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.common.blocks.machines.BaseLitMachineBlock;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class OxygenDistributorBlock extends BaseLitMachineBlock {

    public OxygenDistributorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntityRegistry.OXYGEN_DISTRIBUTOR.get();
    }

    @Override
    public boolean hasTicker(Level level) {
        return !level.isClientSide;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(OxygenDistributorBlock::new);
    }
}
