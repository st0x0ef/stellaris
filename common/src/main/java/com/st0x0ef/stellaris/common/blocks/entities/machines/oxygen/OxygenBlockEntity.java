package com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen;

import com.st0x0ef.stellaris.common.oxygen.OxygenContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class OxygenBlockEntity extends BaseContainerBlockEntity {

    private NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    protected OxygenContainer container;
    protected int range;

    protected OxygenBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, OxygenContainer container, int range) {
        super(type, pos, blockState);
        this.container = container;
        this.range = range;
    }

    public void tick() {}

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    public OxygenContainer getOxygenContainer() {
        return container;
    }

    public int getRange() {
        return range;
    }
}
