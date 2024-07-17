package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.armors.JetSuit;
import com.st0x0ef.stellaris.common.data.recipes.FuelRefineryRecipe;
import com.st0x0ef.stellaris.common.data_components.JetSuitComponent;
import com.st0x0ef.stellaris.common.data_components.SpaceArmorComponent;
import com.st0x0ef.stellaris.common.menus.FuelRefineryMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import com.st0x0ef.stellaris.common.registry.RecipesRegistry;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;
import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class FuelRefineryBlockEntity extends BaseEnergyContainerBlockEntity {

    private final FluidTank ingredientTank = new FluidTank("ingredientTank", 5);
    private final FluidTank resultTank = new FluidTank("resultTank", 5);
    private final RecipeManager.CachedCheck<FuelRefineryBlockEntity, FuelRefineryRecipe> cachedCheck = RecipeManager.createCheck(RecipesRegistry.FUEL_REFINERY_TYPE.get());

    public FuelRefineryBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FUEL_REFINERY.get(), pos, state);
    }

    @Override
    public void tick() {
        if(this.getItem(2).is(Items.BUCKET)) {
            FluidTankHelper.extractFluidToItem(this, resultTank, 2, 3);
        } else if (getItem(2).get(DataComponentsRegistry.SPACE_ARMOR_ROCKET.get()) != null){
            setFuelToJetSuit(this.getItem(2));
        }


        if (!FluidTankHelper.addFluidFromBucket(this, ingredientTank, 0, 1)) {
            FluidTankHelper.extractFluidToItem(this, ingredientTank, 0, 1);
        }

        Optional<RecipeHolder<FuelRefineryRecipe>> recipeHolder = cachedCheck.getRecipeFor(this, level);
        if (recipeHolder.isPresent()) {
            FuelRefineryRecipe recipe = recipeHolder.get().value();
            WrappedBlockEnergyContainer energyContainer = getWrappedEnergyContainer();

            if (energyContainer.getStoredEnergy() >= recipe.energy()) {
                FluidStack resultStack = recipe.resultStack();

                if (resultTank.isEmpty() || resultTank.getStack().isFluidEqual(resultStack)) {
                    if (resultTank.getAmount() + resultStack.getAmount() < resultTank.getMaxCapacity()) {
                        energyContainer.extractEnergy(recipe.energy(), false);
                        ingredientTank.grow(-recipe.ingredientStack().getAmount());
                        FluidTankHelper.addToTank(resultTank, resultStack);
                        setChanged();
                    }
                }
            }
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.stellaris.fuel_refinery");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new FuelRefineryMenu(containerId, inventory, this, this);
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    protected int getMaxCapacity() {
        return 6000;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ingredientTank.load(provider, tag);
        resultTank.load(provider, tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ingredientTank.save(provider, tag);
        resultTank.save(provider, tag);
    }

    public FluidTank getIngredientTank() {
        return ingredientTank;
    }

    public FluidTank getResultTank() {
        return resultTank;
    }

    public void setFuelToJetSuit(ItemStack oldStack) {

        SpaceArmorComponent oldSpaceArmorComponent = oldStack.get(DataComponentsRegistry.SPACE_ARMOR_ROCKET.get());
        ItemStack newStack = new ItemStack(ItemsRegistry.JETSUIT_SUIT);
        this.removeItem(2, 1);

        int amountToGive = 0;
        if (resultTank.getAmount() >= 3000) {
            amountToGive = 3000;
        } else {
            amountToGive = (int) resultTank.getAmount();
        }
        resultTank.grow(-amountToGive);
        SpaceArmorComponent newSpaceArmorComponent = new SpaceArmorComponent(
                oldSpaceArmorComponent.fuel() + amountToGive, oldSpaceArmorComponent.oxygen());

        newStack.set(DataComponentsRegistry.SPACE_ARMOR_ROCKET.get(), newSpaceArmorComponent);
        this.setItem(3, newStack);
        setChanged();
    }
}
