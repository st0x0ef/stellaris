package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.energy.EnergyApi;
import com.st0x0ef.stellaris.common.energy.impl.WrappedBlockEnergyContainer;
import com.st0x0ef.stellaris.common.menus.CoalGeneratorMenu;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity.getFuel;

public class CoalGeneratorEntity extends GeneratorBlockEntityTemplate {

    private List<Integer> inputSlots = List.of(0);

    public CoalGeneratorEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.COAL_GENERATOR.get(), blockPos, blockState,1,2000);

        super.items = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new CoalGeneratorMenu(i, inventory,this,this);
    }

    //TODO implement coal and charcoal blocks(maybe all blocks)
    int litTime;
    int litDuration;
    int cookingProgress;
    int cookingTotalTime;

    @Override
    public void tick() {}

    public static void serverTick(Level level, BlockPos pos, BlockState state, CoalGeneratorEntity blockEntity) {
        WrappedBlockEnergyContainer energyContainer = blockEntity.getEnergyContainer();
        boolean bl = blockEntity.isLit();
        boolean bl2 = false;
        if (blockEntity.isLit()) {
            --blockEntity.litTime;
        }

        ItemStack itemStack = (ItemStack)blockEntity.items.get(1);
        boolean bl3 = !((ItemStack)blockEntity.items.get(0)).isEmpty();
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
                    blockEntity.cookingTotalTime = getTotalCookTime(level, blockEntity);
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
            //TODO add this to coal generator(its 2am i need some sleep i cant make this now)
            state = (BlockState)state.setValue(AbstractFurnaceBlock.LIT, blockEntity.isLit());
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

        EnergyApi.distributeEnergyNearby(blockEntity,100);
    }

    private static boolean canBurn(NonNullList<ItemStack> inventory) {
        return !((ItemStack) inventory.get(0)).isEmpty();
    }

    //TODO check if this is useful
    private static boolean burn(RegistryAccess registryAccess, @Nullable RecipeHolder<?> recipe, NonNullList<ItemStack> inventory) {
        if (recipe != null && canBurn(inventory)) {
            ItemStack itemStack = (ItemStack)inventory.get(0);
            ItemStack itemStack2 = recipe.value().getResultItem(registryAccess);
            ItemStack itemStack3 = (ItemStack)inventory.get(2);
            if (itemStack3.isEmpty()) {
                inventory.set(2, itemStack2.copy());
            } else if (itemStack3.is(itemStack2.getItem())) {
                itemStack3.grow(1);
            }

            if (itemStack.is(Blocks.WET_SPONGE.asItem()) && !((ItemStack)inventory.get(1)).isEmpty() && ((ItemStack)inventory.get(1)).is(Items.BUCKET)) {
                inventory.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemStack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    protected int getBurnDuration(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            Item item = fuel.getItem();
            return (Integer)getFuel().getOrDefault(item, 0);
        }
    }

    private static int getTotalCookTime(Level level, CoalGeneratorEntity blockEntity) {
        //i have no idea what this is for TODO change this
        return 200;
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
        return this.items;
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