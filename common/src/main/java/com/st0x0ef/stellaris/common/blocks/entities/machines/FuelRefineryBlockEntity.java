package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.fej1fun.potentials.components.FluidAmountMapDataComponent;
import com.fej1fun.potentials.providers.FluidProvider;
import com.st0x0ef.stellaris.common.data.recipes.FuelRefineryRecipe;
import com.st0x0ef.stellaris.common.data.recipes.input.FluidInput;
import com.st0x0ef.stellaris.common.menus.FuelRefineryMenu;
import com.st0x0ef.stellaris.common.network.packets.SyncFluidPacket;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
import com.st0x0ef.stellaris.common.registry.RecipesRegistry;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.FluidUtil;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.SingleFluidStorage;
import dev.architectury.fluid.FluidStack;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class FuelRefineryBlockEntity extends BaseEnergyContainerBlockEntity implements FluidProvider.BLOCK {

    private final SingleFluidStorage inputTank;
    private final SingleFluidStorage outputFuelTank;
    private final SingleFluidStorage outputDieselTank;

    private final RecipeManager.CachedCheck<FluidInput, FuelRefineryRecipe> cachedCheck = RecipeManager.createCheck(RecipesRegistry.FUEL_REFINERY_TYPE.get());

    public FuelRefineryBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FUEL_REFINERY.get(), pos, state);
        this.inputTank = new SingleFluidStorage(10000) {
            @Override
            protected void onChange() {
                setChanged();
                if (level != null && level.getServer() != null && !level.getServer().getPlayerList().getPlayers().isEmpty()) {
                    NetworkManager.sendToPlayers(level.getServer().getPlayerList().getPlayers(),
                            new SyncFluidPacket(new FluidAmountMapDataComponent(List.of(getFluidInTank(0).getFluid()), List.of(getFluidValueInTank())), 0, getBlockPos(), Direction.UP));
                }
            }

            @Override
            public boolean isFluidValid(int tank, FluidStack stack) {
                return stack.getFluid().isSame(FluidRegistry.OIL_STILL.get());
            }
        };
        this.outputFuelTank = new SingleFluidStorage(10000) {
            @Override
            protected void onChange() {
                setChanged();
                if (level != null && level.getServer() != null && !level.getServer().getPlayerList().getPlayers().isEmpty()) {
                    NetworkManager.sendToPlayers(level.getServer().getPlayerList().getPlayers(),
                            new SyncFluidPacket(new FluidAmountMapDataComponent(List.of(getFluidInTank(0).getFluid()), List.of(getFluidValueInTank())), 0, getBlockPos(), Direction.NORTH));
                }
            }
        };
        this.outputDieselTank = new SingleFluidStorage(10000) {
            @Override
            protected void onChange() {
                setChanged();
                if (level != null && level.getServer() != null && !level.getServer().getPlayerList().getPlayers().isEmpty()) {
                    NetworkManager.sendToPlayers(level.getServer().getPlayerList().getPlayers(),
                            new SyncFluidPacket(new FluidAmountMapDataComponent(List.of(getFluidInTank(0).getFluid()), List.of(getFluidValueInTank())), 0, getBlockPos(), Direction.SOUTH));
                }
            }
        };
    }

    @Override
    public void tick() {
        FluidUtil.moveFluidToItem(0, inputTank, 1, items, 1000);
        FluidUtil.moveFluidToItem(0, outputFuelTank, 2, items, 1000);
        FluidUtil.moveFluidToItem(0, outputDieselTank, 4, items, 1000);

        FluidUtil.moveFluidFromItem(0, 0, items, inputTank, 1000);

        if (level == null) {
            return;
        }

        Optional<RecipeHolder<FuelRefineryRecipe>> recipeHolder = cachedCheck.getRecipeFor(new FluidInput(level.getBlockEntity(getBlockPos())), level);
        if (recipeHolder.isPresent()) {
            FuelRefineryRecipe recipe = recipeHolder.get().value();

            if (energyContainer.getEnergy() >= recipe.energy()) {

                if (inputTank.getFluidValueInTank() >= recipe.ingredientStack().getAmount()) {
                    if ((outputFuelTank.getFluidInTank(0).isEmpty() || outputFuelTank.getFluidInTank(0).isFluidEqual(recipe.fuelStack())) &&
                            (outputDieselTank.getFluidInTank(0).isEmpty() || outputDieselTank.getFluidInTank(0).isFluidEqual(recipe.dieselStack()))) {
                        boolean shouldUseEnergyAndDrainOil = false;
                        if (outputFuelTank.getFluidValueInTank() + recipe.fuelStack().getAmount() < outputFuelTank.getTankCapacity(0)) {
                            outputFuelTank.fill(recipe.fuelStack().copy(), false);
                            shouldUseEnergyAndDrainOil = true;
                        }
                        if (outputDieselTank.getFluidValueInTank() + recipe.dieselStack().getAmount() < outputDieselTank.getTankCapacity(0)) {
                            outputDieselTank.fill(recipe.dieselStack().copy(), false);
                            shouldUseEnergyAndDrainOil = true;
                        }
                        if (shouldUseEnergyAndDrainOil) {
                            inputTank.drain(recipe.ingredientStack().copy(), false);
                            energyContainer.extract(recipe.energy(), false);
                            setChanged();
                        }
                    }
                }
            }
        }
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("block.stellaris.fuel_refinery");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new FuelRefineryMenu(containerId, inventory, this, this);
    }

    @Override
    public int getContainerSize() {
        return 6;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        inputTank.load(tag, provider, "input");
        outputFuelTank.load(tag, provider, "fuel");
        outputDieselTank.load(tag, provider, "diesel");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        inputTank.save(tag, provider, "input");
        outputFuelTank.save(tag, provider, "fuel");
        outputDieselTank.save(tag, provider, "diesel");
    }

    public SingleFluidStorage getIngredientTank() {
        return inputTank;
    }
    public SingleFluidStorage getOutputFuelTank() {
        return outputFuelTank;
    }
    public SingleFluidStorage getOutputDieselTank() {
        return outputDieselTank;
    }

    @Override
    public @Nullable SingleFluidStorage getFluidTank(@Nullable Direction direction) {
        if (direction == null) {
            return inputTank;
        }

        return switch (direction) {
            case UP, DOWN -> inputTank;
            case EAST, NORTH -> outputFuelTank;
            case WEST, SOUTH -> outputDieselTank;
        };
    }
}
