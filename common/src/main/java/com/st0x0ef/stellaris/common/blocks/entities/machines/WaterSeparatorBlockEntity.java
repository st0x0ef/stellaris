package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.fej1fun.potentials.components.FluidAmountMapDataComponent;
import com.fej1fun.potentials.fluid.UniversalFluidStorage;
import com.fej1fun.potentials.providers.FluidProvider;
import com.st0x0ef.stellaris.common.blocks.machines.BaseMachineBlock;
import com.st0x0ef.stellaris.common.blocks.machines.WaterSeparatorBlock;
import com.st0x0ef.stellaris.common.data.recipes.WaterSeparatorRecipe;
import com.st0x0ef.stellaris.common.data.recipes.input.FluidInput;
import com.st0x0ef.stellaris.common.menus.WaterSeparatorMenu;
import com.st0x0ef.stellaris.common.network.packets.SyncFluidPacket;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
import com.st0x0ef.stellaris.common.registry.RecipesRegistry;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.FluidStorage;
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
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class WaterSeparatorBlockEntity extends BaseEnergyContainerBlockEntity implements FluidProvider.BLOCK {

    public static final int HYDROGEN_TANK = 0;
    public static final int OXYGEN_TANK = 1;

    public final SingleFluidStorage ingredientTank = new SingleFluidStorage(3000, 3000, 0) {

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
            return stack.getFluid() == Fluids.WATER;
        }
    };
    public final FluidStorage resultTanks = new FluidStorage(2, 6000, 0, 1000) {

        @Override
        protected void onChange(int tank) {
            setChanged();
            if (level != null && level.getServer() != null && !level.getServer().getPlayerList().getPlayers().isEmpty()) {
                NetworkManager.sendToPlayers(level.getServer().getPlayerList().getPlayers(),
                        new SyncFluidPacket(new FluidAmountMapDataComponent(List.of(getFluidInTank(tank).getFluid()), List.of(getFluidValueInTank(tank))), tank, getBlockPos(), getBlockState().getValue(WaterSeparatorBlock.FACING).getClockWise()));
            }
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            if (tank == HYDROGEN_TANK) {
                return stack.getFluid() == FluidRegistry.HYDROGEN_STILL.get();
            }
            else if (tank == OXYGEN_TANK) {
                return stack.getFluid() == FluidRegistry.OXYGEN_STILL.get();
            }
            return false;
        }
    };

    private final RecipeManager.CachedCheck<FluidInput, WaterSeparatorRecipe> cachedCheck = RecipeManager.createCheck(RecipesRegistry.WATER_SEPERATOR_TYPE.get());

    public WaterSeparatorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.WATER_SEPARATOR_ENTITY.get(), pos, state);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("block.stellaris.water_separator");
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new WaterSeparatorMenu(containerId, inventory, this, this);
    }

    @Override
    public void tick() {
        FluidUtil.moveFluidToItem(OXYGEN_TANK, resultTanks, 3, items, 1000);
        FluidUtil.moveFluidToItem(HYDROGEN_TANK, resultTanks, 2, items, 1000);

        FluidUtil.moveFluidFromItem(0, 1, items, ingredientTank, 1000);
        Direction facing = getBlockState().getValue(WaterSeparatorBlock.FACING);
        FluidUtil.distributeFluidNearby(level, worldPosition, resultTanks.getFluidInTank(0), List.of(facing.getClockWise()));
        FluidUtil.distributeFluidNearby(level, worldPosition, resultTanks.getFluidInTank(1), List.of(facing.getCounterClockWise()));
        FluidUtil.distributeFluidNearby(level, worldPosition, ingredientTank.getFluidInTank(0), List.of(Direction.UP, Direction.DOWN, facing, facing.getOpposite()));

        if (level == null) {
            return;
        }

        Optional<RecipeHolder<WaterSeparatorRecipe>> recipeHolder = cachedCheck.getRecipeFor(new FluidInput(this), level);
        if (recipeHolder.isPresent()) {
            WaterSeparatorRecipe recipe = recipeHolder.get().value();

            if (energyContainer.getEnergy() >= recipe.energy()) {
                boolean shouldDrainWaterAndEnergy = false;
                if (resultTanks.getFluidValueInTank(HYDROGEN_TANK) < resultTanks.getTankCapacity(HYDROGEN_TANK)) {
                    resultTanks.fillWithoutLimits(recipe.resultStacks().getFirst(), false);
                    shouldDrainWaterAndEnergy = true;
                }
                if (resultTanks.getFluidValueInTank(OXYGEN_TANK) < resultTanks.getTankCapacity(OXYGEN_TANK)) {
                    resultTanks.fillWithoutLimits(recipe.resultStacks().get(1), false);
                    shouldDrainWaterAndEnergy = true;
                }

                if (shouldDrainWaterAndEnergy) {
                    ingredientTank.drainWithoutLimits(recipe.ingredientStack(), false);
                    energyContainer.extract(recipe.energy(), false);
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ingredientTank.save(tag, provider, "ingredient");
        resultTanks.save(tag, provider, "result");
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ingredientTank.load(tag, provider, "ingredient");
        resultTanks.load(tag, provider, "result");
    }

    @Override
    public @Nullable UniversalFluidStorage getFluidTank(@Nullable Direction direction) {
        Direction facing = getBlockState().getValue(BaseMachineBlock.FACING);
        if (facing.getCounterClockWise() == direction || facing.getClockWise() == direction) {
            return resultTanks;
        }
        return ingredientTank;
    }

    public SingleFluidStorage getIngredientTank() {
        return this.ingredientTank;
    }

    public FluidStorage getResultTanks() {
        return this.resultTanks;
    }
}
