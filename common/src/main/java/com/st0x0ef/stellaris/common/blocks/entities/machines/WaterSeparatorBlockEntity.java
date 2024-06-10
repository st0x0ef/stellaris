package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.data.recipes.WaterSeparatorRecipe;
import com.st0x0ef.stellaris.common.items.oxygen.OxygenContainerItem;
import com.st0x0ef.stellaris.common.menus.WaterSeparatorMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.RecipesRegistry;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidBucketHooks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.Optional;

public class WaterSeparatorBlockEntity extends BaseEnergyContainerBlockEntity {

    private static final int TANK_CAPACITY = 3000;
    public static final long BUCKET_AMOUNT = 1000;

    private final FluidTank ingredientTank = new FluidTank("ingredientTank", TANK_CAPACITY);
    private final NonNullList<FluidTank> resultTanks = Util.make(NonNullList.createWithCapacity(2), list -> {
        list.add(0, new FluidTank("resultTank1", TANK_CAPACITY));
        list.add(1, new FluidTank("resultTank2", TANK_CAPACITY));
    });
    private final RecipeManager.CachedCheck<WaterSeparatorBlockEntity, WaterSeparatorRecipe> cachedCheck = RecipeManager.createCheck(RecipesRegistry.WATER_SEPERATOR_TYPE.get());

    public WaterSeparatorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.WATER_SEPARATOR_ENTITY.get(), pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.stellaris.water_separator");
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new WaterSeparatorMenu(containerId, inventory, this, this);
    }

    @Override
    public void tick() {
        for (int i = 0; i < 2; i++) {
            int slot = i + 2;
            tryRemoveFluid(slot, slot, resultTanks.get(i), false);
        }

        if (!addFluidFromBucket()) {
            tryRemoveFluid(1, 0, ingredientTank, true);
        }

        Optional<RecipeHolder<WaterSeparatorRecipe>> recipeHolder = cachedCheck.getRecipeFor(this, level);
        if (recipeHolder.isPresent()) {
            WaterSeparatorRecipe recipe = recipeHolder.get().value();
            WrappedBlockEnergyContainer energyContainer = getWrappedEnergyContainer();

            if (energyContainer.getStoredEnergy() >= recipe.energy()) {
                List<FluidStack> stacks = recipe.resultStacks();
                long firstAmount = stacks.getFirst().getAmount();
                long secondAmount = stacks.get(1).getAmount();
                FluidTank firstTank = resultTanks.getFirst();
                FluidTank secondTank = resultTanks.get(1);

                if (firstTank.getAmount() + firstAmount <= firstTank.getMaxCapacity() && secondTank.getAmount() + secondAmount <= secondTank.getMaxCapacity()) {
                    setChanged();
                    energyContainer.extractEnergy(recipe.energy(), false);
                    ingredientTank.grow(-recipe.ingredientStack().getAmount());
                    firstTank.grow(firstAmount);
                    secondTank.grow(secondAmount);
                }
            }
        }
    }

    private boolean addFluidFromBucket() {
        if (ingredientTank.getAmount() + BUCKET_AMOUNT < ingredientTank.getMaxCapacity()) {
            ItemStack inputStack = getItem(1);

            if (!inputStack.isEmpty() && getItem(0).isEmpty()) {
                if (inputStack.getItem() instanceof BucketItem item) {
                    Fluid fluid = FluidBucketHooks.getFluid(item);

                    if (!fluid.isSame(Fluids.EMPTY)) {
                        setItem(0, new ItemStack(Items.BUCKET));
                        setItem(1, ItemStack.EMPTY);

                        if (ingredientTank.getStack().isEmpty()) {
                            ingredientTank.setFluid(fluid, BUCKET_AMOUNT);
                            setChanged();
                            return true;
                        }

                        ingredientTank.grow(BUCKET_AMOUNT);
                        setChanged();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void tryRemoveFluid(int inputSlot, int outputSlot, FluidTank tank, boolean checkOutput) {
        ItemStack inputStack = getItem(inputSlot);

        ItemStack outputStack = getItem(outputSlot);
        if (!inputStack.isEmpty() && (!checkOutput || outputStack.isEmpty() || outputStack.getCount() < outputStack.getMaxStackSize())) {
            if (!tank.getStack().isEmpty() && tank.getAmount() >= BUCKET_AMOUNT) {
                ItemStack outputStack1;
                if (inputStack.getItem() instanceof OxygenContainerItem) {
                    outputStack1 = inputStack.copy(); // TODO modify oxygen amount
                }
                else {
                    outputStack1 = new ItemStack(tank.getStack().getFluid().getBucket());
                }

                if (outputStack.isEmpty()) {
                    setItem(outputSlot, outputStack1);
                }
                else if (ItemStack.isSameItem(outputStack, outputStack1) && outputStack.getCount() < outputStack.getMaxStackSize()) {
                    getItem(outputSlot).grow(1);
                }
                else {
                    return;
                }

                inputStack.shrink(1);
                tank.grow(-BUCKET_AMOUNT);
                setChanged();
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ingredientTank.save(provider, tag);
        resultTanks.forEach(tank -> tank.save(provider, tag));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ingredientTank.load(provider, tag);
        resultTanks.forEach(tank -> tank.load(provider, tag));
    }

    @Override
    protected int getMaxCapacity() {
        return 12000;
    }

    public FluidTank getIngredientTank() {
        return ingredientTank;
    }

    public static class FluidTank {

        private final String name;
        private final int maxCapacity;
        private FluidStack stack = FluidStack.empty();

        public FluidTank(String name, int maxCapacity) {
            this.name = name;
            this.maxCapacity = maxCapacity;
        }

        public int getMaxCapacity() {
            return maxCapacity;
        }

        public void setFluid(Fluid fluid, long amount) {
            stack = FluidStack.create(fluid, amount);
        }

        public long getAmount() {
            return stack.getAmount();
        }

        public void setAmount(long amount) {
            stack.setAmount(Mth.clamp(amount, 0, maxCapacity));
        }

        public void grow(long amount) {
            setAmount(getAmount() + amount);
        }

        public FluidStack getStack() {
            return stack;
        }

        public void load(HolderLookup.Provider provider, CompoundTag tag) {
            CompoundTag containerTag = tag.getCompound(name);
            stack = FluidStack.read(provider, containerTag).orElse(FluidStack.empty());
        }

        public void save(HolderLookup.Provider provider, CompoundTag tag) {
            tag.put(name, stack.write(provider, new CompoundTag()));
        }
    }
}
