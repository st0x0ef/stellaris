package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.data.recipes.WaterSeparatorRecipe;
import com.st0x0ef.stellaris.common.items.oxygen.OxygenContainerItem;
import com.st0x0ef.stellaris.common.menus.WaterSeparatorMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
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

    //Water Tank (I guess)
    public final FluidTank ingredientTank = new FluidTank("ingredientTank", TANK_CAPACITY);

    public final NonNullList<FluidTank> resultTanks = Util.make(NonNullList.createWithCapacity(2), list -> {
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
            ItemStack inputStack = getItem(slot);
            FluidTank tank = resultTanks.get(i);

            if (!inputStack.isEmpty()) {
                if (!tank.isEmpty() && tank.getAmount() >= BUCKET_AMOUNT) {
                    ItemStack resultStack;
                    if (tank.getStack().getFluid().isSame(FluidRegistry.OXYGEN_STILL.get()) && inputStack.getItem() instanceof OxygenContainerItem) {
                        resultStack = inputStack.copy(); // TODO modify oxygen amount
                    }
                    else {
                        resultStack = new ItemStack(tank.getStack().getFluid().getBucket());
                    }

                    if (!resultStack.isEmpty()) {
                        setItem(slot, resultStack);
                        tank.grow(-BUCKET_AMOUNT);
                        setChanged();
                    }
                }
            }
        }

        if (!addFluidFromBucket()) {
            ItemStack inputStack = getItem(1);
            ItemStack outputStack = getItem(0);
            boolean hasSpace = outputStack.getCount() < outputStack.getMaxStackSize();

            if (!inputStack.isEmpty() && (outputStack.isEmpty() || hasSpace)) {
                if (!ingredientTank.isEmpty() && ingredientTank.getAmount() >= BUCKET_AMOUNT) {
                    if (inputStack.getItem() instanceof BucketItem item && FluidBucketHooks.getFluid(item).isSame(Fluids.EMPTY)) {
                        ItemStack resultStack = new ItemStack(ingredientTank.getStack().getFluid().getBucket());
                        boolean success = false;

                        if (outputStack.isEmpty()) {
                            setItem(0, resultStack);
                            success = true;
                        }
                        else if (ItemStack.isSameItem(outputStack, resultStack) && hasSpace) {
                            getItem(0).grow(1);
                            success = true;
                        }

                        if (success) {
                            inputStack.shrink(1);
                            ingredientTank.grow(-BUCKET_AMOUNT);
                            setChanged();
                        }
                    }
                }
            }
        }

        Optional<RecipeHolder<WaterSeparatorRecipe>> recipeHolder = cachedCheck.getRecipeFor(this, level);
        if (recipeHolder.isPresent()) {
            WaterSeparatorRecipe recipe = recipeHolder.get().value();
            WrappedBlockEnergyContainer energyContainer = getWrappedEnergyContainer();

            if (energyContainer.getStoredEnergy() >= recipe.energy()) {
                List<FluidStack> stacks = recipe.resultStacks();
                FluidStack stack1 = recipe.resultStacks().getFirst();
                FluidStack stack2 = recipe.resultStacks().get(1);
                FluidTank tank1 = resultTanks.getFirst();
                FluidTank tank2 = resultTanks.get(1);

                if ((tank1.isEmpty() || tank1.getStack().isFluidEqual(stack1)) && (tank2.isEmpty() || tank2.getStack().isFluidEqual(stack2))) {
                    if (tank1.getAmount() + stacks.getFirst().getAmount() <= tank1.getMaxCapacity() && tank2.getAmount() + stacks.get(1).getAmount() <= tank2.getMaxCapacity()) {
                        energyContainer.extractEnergy(recipe.energy(), false);
                        ingredientTank.grow(-recipe.ingredientStack().getAmount());
                        addToTank(tank1, stack1);
                        addToTank(tank2, stack2);
                        setChanged();
                    }
                }
            }
        }
    }

    private static void addToTank(FluidTank tank, FluidStack stack) {
        FluidStack tankStack = tank.getStack();
        if (tankStack.isEmpty()) {
            tank.setFluid(stack.getFluid(), stack.getAmount());
        }
        else {
            tank.grow(stack.getAmount());
        }
    }

    private boolean addFluidFromBucket() {
        if (ingredientTank.getAmount() + BUCKET_AMOUNT < ingredientTank.getMaxCapacity()) {
            ItemStack inputStack = getItem(1);
            ItemStack outputStack = getItem(0);
            boolean hasSpace = outputStack.getCount() < outputStack.getMaxStackSize();

            if (!inputStack.isEmpty() && (outputStack.isEmpty() || hasSpace)) {
                if (inputStack.getItem() instanceof BucketItem item) {
                    Fluid fluid = FluidBucketHooks.getFluid(item);

                    if (!fluid.isSame(Fluids.EMPTY)) {
                        if (outputStack.isEmpty()) {
                            setItem(0, new ItemStack(Items.BUCKET));
                        }
                        else if (outputStack.is(Items.BUCKET) && hasSpace) {
                            outputStack.grow(1);
                        }
                        else return false;

                        setItem(1, ItemStack.EMPTY);
                        addToTank(ingredientTank, FluidStack.create(fluid, BUCKET_AMOUNT));
                        setChanged();
                        return true;
                    }
                }
            }
        }
        return false;
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

    public NonNullList<FluidTank> getResultTanks() {
        return resultTanks;
    }
}
