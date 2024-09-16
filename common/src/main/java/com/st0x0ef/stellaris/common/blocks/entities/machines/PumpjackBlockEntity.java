package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.armors.JetSuit;
import com.st0x0ef.stellaris.common.blocks.machines.CoalGeneratorBlock;
import com.st0x0ef.stellaris.common.data.recipes.FuelRefineryRecipe;
import com.st0x0ef.stellaris.common.data.recipes.input.FluidInput;
import com.st0x0ef.stellaris.common.menus.FuelRefineryMenu;
import com.st0x0ef.stellaris.common.menus.PumpjackMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
import com.st0x0ef.stellaris.common.registry.RecipesRegistry;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;
import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Optional;

public class PumpjackBlockEntity extends BaseEnergyContainerBlockEntity {

    private boolean isGenerating = false;
    private final FluidTank resultTank = new FluidTank("resultTank", 5);
    public int chunkOilLevel = 0;
    public PumpjackBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.PUMPJACK.get(), pos, state);
    }


    @Override
    public void tick() {

        FluidTankHelper.extractFluidToItem(this, resultTank, 0, 1);

        ChunkAccess access = this.level.getChunk(this.worldPosition);

        chunkOilLevel = access.stellaris$getChunkOilLevel();

        WrappedBlockEnergyContainer energyContainer = getWrappedEnergyContainer();

        if (energyContainer.getStoredEnergy() >= 20 && access.stellaris$getChunkOilLevel() >= 1) {
            if (resultTank.getAmount() + 1 <= resultTank.getMaxCapacity()) {
                access.stellaris$setChunkOilLevel(access.stellaris$getChunkOilLevel() - 1);
                FluidStack tankStack = resultTank.getStack();

                if (tankStack.isEmpty()) {
                    resultTank.setFluid(FluidRegistry.OIL_ATTRIBUTES.getSourceFluid(), 1);
                }
                resultTank.grow(1);
                energyContainer.extractEnergy(20, false);
                isGenerating = true;
                setChanged();
            }
        }

        if (isGenerating) {
            BlockState state = getBlockState().setValue(CoalGeneratorBlock.LIT, isGenerating);
            level.setBlock(getBlockPos(), state, 3);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.stellaris.fuel_refinery");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new PumpjackMenu(containerId, inventory, this,  this);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    protected int getMaxCapacity() {
        return 6000;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        resultTank.load(provider, tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        resultTank.save(provider, tag);
    }

    public FluidTank getResultTank() {
        return resultTank;
    }

    public int chunkOilLevel() {
        return chunkOilLevel;
    }
}