package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.machines.CoalGenerator;
import com.st0x0ef.stellaris.common.menus.CoalGeneratorMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity.getFuel;

public class CoalGeneratorEntity extends GeneratorBlockEntityTemplate {

    private int litTime;
    private int litDuration;
    public final ContainerData dataAccess = new ContainerData() {

        public int get(int index) {
            switch (index) {
                case 0 -> {
                    return litTime;
                }
                case 1 -> {
                    return litDuration;
                }
                default -> {
                    return 0;
                }
            }
        }

        public void set(int index, int value) {
            switch (index) {
                case 0 -> litTime = value;
                case 1 -> litDuration = value;
            }
        }

        public int getCount() {
            return 2;
        }
    };

    public CoalGeneratorEntity(BlockPos blockPos, BlockState blockState) {
        this(BlockEntityRegistry.COAL_GENERATOR.get(), blockPos, blockState, 1, 2000);
    }

    public CoalGeneratorEntity(BlockEntityType<?> entityType, BlockPos blockPos, BlockState blockState, int energyGeneratedPT, int maxCapacity) {
        super(entityType, blockPos, blockState, energyGeneratedPT, maxCapacity);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new CoalGeneratorMenu(containerId, inventory, this, this, dataAccess);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CoalGeneratorEntity blockEntity) {
        WrappedBlockEnergyContainer energyContainer = blockEntity.getWrappedEnergyContainer();
        boolean wasLit = blockEntity.isLit();
        boolean shouldUpdate = false;

        if (blockEntity.isLit()) {
            --blockEntity.litTime;
        }

        ItemStack stack = blockEntity.getItems().get(0);
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
                        blockEntity.getItems().set(0, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
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

        if (blockEntity.isLit()) {
            if (energyContainer.getStoredEnergy() < energyContainer.getMaxCapacity()) {
                energyContainer.setEnergy(energyContainer.getStoredEnergy() + blockEntity.getEnergyGeneratedPT());
            }
            else if (energyContainer.getStoredEnergy() > energyContainer.getMaxCapacity()) {
                energyContainer.setEnergy(energyContainer.getMaxCapacity());
            }
        }

        //EnergyApi.distributeEnergyNearby(blockEntity,100);
//        Stellaris.LOG.warn(Long.toString(energyContainer.getStoredEnergy()));
    }

    protected int getBurnDuration(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        }
        else {
            Item item = fuel.getItem();
            return getFuel().getOrDefault(item, 0);
        }
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    @Override
    public boolean canGenerate() {
        return isLit();
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        litTime = tag.getShort("BurnTime");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putShort("BurnTime", (short) this.litTime);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.stellaris.coal_generator");
    }

    @Override
    public int getContainerSize() {
        return 1;
    }
}
