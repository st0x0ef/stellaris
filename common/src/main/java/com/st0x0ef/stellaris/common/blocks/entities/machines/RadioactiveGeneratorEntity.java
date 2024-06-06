package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.google.common.collect.Maps;
import com.st0x0ef.stellaris.common.config.CustomConfig;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;
import com.st0x0ef.stellaris.common.items.RadioactiveItem;
import com.st0x0ef.stellaris.common.menus.RadioactiveGeneratorMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class RadioactiveGeneratorEntity extends GeneratorBlockEntityTemplate {

    int litTime;
    int litDuration;
    int cookingProgress;
    int cookingTotalTime;
    private static volatile Map<Item, Integer> fuelCache;


    public RadioactiveGeneratorEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.RADIOACTIVE_GENERATOR.get(), blockPos, blockState,1,2000,"stellaris.energy.radioactive_generator");

        super.items = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new RadioactiveGeneratorMenu(i, inventory,this,this);
    }

    @Override
    public void tick() {}

    public static void serverTick(Level level, BlockPos pos, BlockState state, RadioactiveGeneratorEntity blockEntity) {
        WrappedBlockEnergyContainer energyContainer = blockEntity.getWrappedEnergyContainer();
        boolean bl = blockEntity.isLit();
        boolean bl2 = false;
        if (blockEntity.isLit()) {
            --blockEntity.litTime;
        }

        ItemStack itemStack = blockEntity.items.get(1);
        boolean bl3 = !(blockEntity.items.get(0)).isEmpty();
        boolean bl4 = !itemStack.isEmpty();
        if (blockEntity.isLit() || bl4 && bl3) {
            int i = blockEntity.getMaxStackSize();
            if (!blockEntity.isLit() && canBurn(blockEntity.items)) {
                blockEntity.litTime = blockEntity.getBurnDuration(itemStack);
                blockEntity.litDuration = blockEntity.litTime;
                if (blockEntity.isLit()) {
                    bl2 = true;
                    if (bl4) {
                        Item item = itemStack.getItem();
                        itemStack.shrink(1);
                        if (itemStack.isEmpty()) {
                            Item item2 = item.getCraftingRemainingItem();
                            blockEntity.items.set(1, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
                        }
                    }
                }
            }

            if (blockEntity.isLit() && canBurn(blockEntity.items)) {
                ++blockEntity.cookingProgress;
                if (blockEntity.cookingProgress == blockEntity.cookingTotalTime) {
                    blockEntity.cookingProgress = 0;
                    blockEntity.cookingTotalTime = blockEntity.getBurnDuration(blockEntity.getItem(0));
                    bl2 = true;
                }
            } else {
                blockEntity.cookingProgress = 0;
            }
        } else if (!blockEntity.isLit() && blockEntity.cookingProgress > 0) {
            blockEntity.cookingProgress = Mth.clamp(blockEntity.cookingProgress - 2, 0, blockEntity.cookingTotalTime);
        }

        if (bl != blockEntity.isLit()) {
            bl2 = true;
            state = state.setValue(AbstractFurnaceBlock.LIT, blockEntity.isLit());
            level.setBlock(pos, state, 3);
        }

        if (bl2) {
            setChanged(level, pos, state);
        }

        if (blockEntity.isLit()){
            if (energyContainer.getStoredEnergy() < energyContainer.getMaxCapacity()) {
                energyContainer.setEnergy(energyContainer.getStoredEnergy() + blockEntity.getEnergyGeneratedPT());
            } else if (energyContainer.getStoredEnergy() > energyContainer.getMaxCapacity()) {
                energyContainer.setEnergy(energyContainer.getMaxCapacity());
            }
        }

        //EnergyApi.distributeEnergyNearby(blockEntity,100);
    }

    private static boolean canBurn(NonNullList<ItemStack> inventory) {
        return !(inventory.get(0)).isEmpty();
    }

    //TODO check if this is useful
    private static boolean burn(NonNullList<ItemStack> inventory) {
        if (canBurn(inventory)) {
            ItemStack itemStack = inventory.get(0);
            itemStack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    public static Map<Item, Integer> getFuel() {
        Map<Item, Integer> map = fuelCache;
        if (map != null) {
            return map;
        } else {
            Map<Item, Integer> map2 = Maps.newLinkedHashMap();
            add(map2, ItemsRegistry.URANIUM_INGOT.get(), (int) CustomConfig.getValue("uraniumBurnTime"));
            add(map2, ItemsRegistry.PLUTONIUM_INGOT.get(), (int) CustomConfig.getValue("plutoniumBurnTime"));
            add(map2, ItemsRegistry.NEPTUNIUM_INGOT.get(), (int) CustomConfig.getValue("neptuniumBurnTime"));
            fuelCache = map2;
            return map2;
        }
    }

    private static void add(Map<Item, Integer> map, ItemLike itemLike, int i) {
        Item item = itemLike.asItem();
        map.put(item, i);
    }

    protected int getBurnDuration(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            Item item = fuel.getItem();
            if (item instanceof RadioactiveItem radioactiveItem) {
                switch (radioactiveItem.getRadiationLevel()) {
                    case 0: return 200;
                    case 1: return 500;
                    case 2: return 1000;
                    default: return 0;
                }
            }
        }

        return 0;
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    @Override
    public boolean canGenerate() {
        return canBurn(this.items);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return super.getItems();
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items, provider);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putShort("BurnTime", (short)this.litTime);
        compoundTag.putShort("CookTime", (short)this.cookingProgress);
        compoundTag.putShort("CookTimeTotal", (short)this.cookingTotalTime);
    }
}
