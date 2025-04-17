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
import net.minecraft.server.level.ServerLevel;
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
    private final SingleFluidStorage outputTank;

    private final RecipeManager.CachedCheck<FluidInput, FuelRefineryRecipe> cachedCheck = RecipeManager.createCheck(RecipesRegistry.FUEL_REFINERY_TYPE.get());

    public FuelRefineryBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FUEL_REFINERY.get(), pos, state);
        this.inputTank = new SingleFluidStorage(10000, 10000, 0) {
            @Override
            protected void onChange() {
                setChanged();
                if (level != null && level.getServer() != null && !level.getServer().getPlayerList().getPlayers().isEmpty())
                    NetworkManager.sendToPlayers(level.getServer().getPlayerList().getPlayers(),
                            new SyncFluidPacket(new FluidAmountMapDataComponent(List.of(getFluidInTank(0).getFluid()), List.of(getFluidValueInTank())), 0, getBlockPos(), Direction.UP));
            }

            @Override
            public boolean isFluidValid(int tank, FluidStack stack) {
                return stack.getFluid().isSame(FluidRegistry.OIL_STILL.get());
            }
        };
        this.outputTank = new SingleFluidStorage(10000, 0, 10000) {
            @Override
            protected void onChange() {
                setChanged();
                if (level != null && level.getServer() != null && !level.getServer().getPlayerList().getPlayers().isEmpty())
                    NetworkManager.sendToPlayers(level.getServer().getPlayerList().getPlayers(),
                            new SyncFluidPacket(new FluidAmountMapDataComponent(List.of(getFluidInTank(0).getFluid()), List.of(getFluidValueInTank())), 0, getBlockPos(), Direction.DOWN));
            }
        };
    }

    @Override
    public void tick() {
        FluidUtil.moveFluidToItem(0, outputTank,2, items, 1000);
        FluidUtil.moveFluidToItem(0, inputTank,1, items, 1000);

        FluidUtil.moveFluidFromItem(0, 0, items, inputTank, 1000);

        if (level == null) return;

        if (level instanceof ServerLevel serverLevel) {
            Optional<RecipeHolder<FuelRefineryRecipe>> recipeHolder = cachedCheck.getRecipeFor(new FluidInput(level.getBlockEntity(getBlockPos())), serverLevel);
            if (recipeHolder.isPresent()) {
                FuelRefineryRecipe recipe = recipeHolder.get().value();

                if (energyContainer.getEnergy() >= recipe.energy()) {
                    FluidStack resultStack = recipe.resultStack().copy();

                    if (outputTank.getFluidInTank(0).isEmpty() || outputTank.getFluidInTank(0).isFluidEqual(resultStack)) {
                        if (outputTank.getFluidValueInTank() + resultStack.getAmount() < outputTank.getTankCapacity(0)) {
                            energyContainer.extract((int) recipe.energy(), false);
                            inputTank.drainWithoutLimits(recipe.ingredientStack().copy(), false);
                            outputTank.fillWithoutLimits(resultStack, false);
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
        return 4;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        inputTank.load(tag, provider, "input");
        outputTank.load(tag, provider, "output");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        inputTank.save(tag, provider, "input");
        outputTank.save(tag, provider, "output");
    }

    public SingleFluidStorage getIngredientTank() {
        return inputTank;
    }
    public SingleFluidStorage getResultTank() {
        return outputTank;
    }

    @Override
    public @Nullable SingleFluidStorage getFluidTank(@Nullable Direction direction) {
        //TODO better directions
        if (direction == null) {
            return outputTank;
        }

        return switch (direction) {
            case UP, WEST, SOUTH -> inputTank;
            case DOWN, EAST, NORTH -> outputTank;
        };
    }
}
