package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.fej1fun.potentials.providers.FluidProvider;
import com.st0x0ef.stellaris.common.blocks.entities.ImplementedInventory;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.FluidUtil;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.SingleFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidTankBlockEntity extends BlockEntity implements FluidProvider.BLOCK, ImplementedInventory, TickingBlockEntity, MenuProvider {

    private final SingleFluidStorage fluidTank;
    protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    public FluidTankBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, 0);
    }

    public FluidTankBlockEntity(BlockPos pos, BlockState state, long capacity) {
        super(BlockEntityRegistry.TANK.get(), pos, state);
        fluidTank = new SingleFluidStorage(capacity) {

            @Override
            protected void onChange() {
                setChanged();
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        fluidTank.save(tag, registries, "tank");
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        fluidTank.load(tag, registries, "tank");
    }

    @Override
    public @Nullable SingleFluidStorage getFluidTank(@Nullable Direction direction) {
        return fluidTank;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void tick() {
        //First - Insert slot
        if (!items.getFirst().isEmpty())
            FluidUtil.moveFluidFromItem(0, 0, items, fluidTank, fluidTank.getTankCapacity(0) / 8);
        //Last - Extract slot
        if (!items.getLast().isEmpty())
            FluidUtil.moveFluidToItem(0, fluidTank, 1, items, fluidTank.getTankCapacity(0) / 8);

        FluidUtil.distributeFluidNearby(level, worldPosition, fluidTank.getFluidInTank(0).copyWithAmount(fluidTank.getTankCapacity(0) / 4));
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Fluid Tank");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }
}