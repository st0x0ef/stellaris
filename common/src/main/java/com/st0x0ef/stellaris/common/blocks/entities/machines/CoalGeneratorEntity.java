package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.menus.CoalGeneratorMenu;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CoalGeneratorEntity extends GeneratorBlockEntityTemplate {

    private NonNullList<ItemStack> items;
    private List<Integer> inputSlots = List.of(0);

    public CoalGeneratorEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.COAL_GENERATOR.get(), blockPos, blockState,1,2000);

        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    //TODO create menu and screen
    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new CoalGeneratorMenu(i, inventory,this,this);
    }

    private final int burnTimeMax = AbstractFurnaceBlockEntity.getFuel().get(Items.COAL);
    private int burnTime = 0;
    @Override
    public void tick() {
        super.tick();
        if (canGenerate()){burnTime++;}
        if (burnTime>=burnTimeMax) {items.get(0).shrink(1);}
    }

    @Override
    public boolean canGenerate() {
        return !items.get(0).isEmpty() && burnTime<burnTimeMax;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }
}
