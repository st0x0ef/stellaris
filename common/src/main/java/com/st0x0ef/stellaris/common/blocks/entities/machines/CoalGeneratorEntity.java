package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.blocks.machines.CoalGenerator;
import com.st0x0ef.stellaris.common.energy.EnergyApi;
import com.st0x0ef.stellaris.common.energy.impl.WrappedBlockEnergyContainer;
import com.st0x0ef.stellaris.common.menus.CoalGeneratorMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity.getFuel;

public class CoalGeneratorEntity extends GeneratorBlockEntityTemplate {
    int litTime;
    int litDuration;
    int cookingProgress;
    int cookingTotalTime;

    public final ContainerData dataAccess;

    private List<Integer> inputSlots = List.of(0);

    public CoalGeneratorEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.COAL_GENERATOR.get(), blockPos, blockState,1,2000);

        super.items = NonNullList.withSize(1, ItemStack.EMPTY);

        this.dataAccess = new ContainerData() {

            public int get(int i) {
                switch (i) {
                    case 0 -> {
                        return CoalGeneratorEntity.this.litTime;
                    }
                    case 1 -> {
                        return CoalGeneratorEntity.this.litDuration;
                    }
                    case 2 -> {
                        return CoalGeneratorEntity.this.cookingProgress;
                    }
                    case 3 -> {
                        return CoalGeneratorEntity.this.cookingTotalTime;
                    }
                    default -> {
                        return 0;
                    }
                }
            }

            public void set(int i, int j) {
                switch (i) {
                    case 0 -> CoalGeneratorEntity.this.litTime = j;
                    case 1 -> CoalGeneratorEntity.this.litDuration = j;
                    case 2 -> CoalGeneratorEntity.this.cookingProgress = j;
                    case 3 -> CoalGeneratorEntity.this.cookingTotalTime = j;
                }
            }

            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new CoalGeneratorMenu(i, inventory, this, this, dataAccess);
    }

    @Override
    public void tick() {}

    public static void serverTick(Level level, BlockPos pos, BlockState state, CoalGeneratorEntity blockEntity) {
        WrappedBlockEnergyContainer energyContainer = blockEntity.getEnergyContainer();
        boolean wasLit = blockEntity.isLit();
        boolean shouldUpdate = false;

        if (blockEntity.isLit()) {
            --blockEntity.litTime;
        }

        ItemStack stack = blockEntity.items.get(0);
        if (blockEntity.isLit() || !stack.isEmpty()) {
            if (!blockEntity.isLit() && !stack.isEmpty()) {
                blockEntity.litTime = blockEntity.getBurnDuration(stack);
                blockEntity.litDuration = blockEntity.litTime;
                if (blockEntity.isLit()) {
                    shouldUpdate = true;
                    Item item = stack.getItem();
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        Item item2 = item.getCraftingRemainingItem();
                        blockEntity.items.set(0, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
                    }
                }
            }
        }

        if (wasLit != blockEntity.isLit()) {
            shouldUpdate = true;
            state = state.setValue(CoalGenerator.LIT, blockEntity.isLit());
            level.setBlock(pos, state, 3);
        }

        if (shouldUpdate) {
            setChanged(level, pos, state);
        }

        if (blockEntity.isLit()){
            if (energyContainer.getStoredEnergy() < energyContainer.getMaxCapacity()) {
                energyContainer.setEnergy(energyContainer.getStoredEnergy() + blockEntity.getEnergyGeneratedPT());
            } else if (energyContainer.getStoredEnergy() > energyContainer.getMaxCapacity()) {
                energyContainer.setEnergy(energyContainer.getMaxCapacity());
            }
        }

        EnergyApi.distributeEnergyNearby(blockEntity,100);
    }

    protected int getBurnDuration(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            Item item = fuel.getItem();
            return getFuel().getOrDefault(item, 0);
        }
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    @Override
    public boolean canGenerate() {
        return this.isLit();
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
    }

//    @Override
//    public NonNullList<ItemStack> getItems() {
//        return this.items;
//    }

    @Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items, provider);
        litTime = compoundTag.getShort("BurnTime");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        ContainerHelper.saveAllItems(compoundTag, items, provider);
        compoundTag.putShort("BurnTime", (short)this.litTime);
    }
}
