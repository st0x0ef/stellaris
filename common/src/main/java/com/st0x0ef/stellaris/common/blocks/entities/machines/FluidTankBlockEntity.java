package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.fej1fun.potentials.components.FluidAmountMapDataComponent;
import com.fej1fun.potentials.providers.FluidProvider;
import com.st0x0ef.stellaris.common.blocks.entities.ImplementedInventory;
import com.st0x0ef.stellaris.common.blocks.machines.FluidTankBlock;
import com.st0x0ef.stellaris.common.menus.FluidTankMenu;
import com.st0x0ef.stellaris.common.network.packets.SyncFluidPacketWithoutDirection;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.FluidUtil;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.SingleFluidStorage;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluidTankBlockEntity extends BaseContainerBlockEntity implements FluidProvider.BLOCK, ImplementedInventory, TickingBlockEntity, MenuProvider {

    private final SingleFluidStorage fluidTank;
    protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    private int renderStage = -1; // -1 to force update on first tick

    public FluidTankBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, ((FluidTankBlock)state.getBlock()).capacity);
    }

    public FluidTankBlockEntity(BlockPos pos, BlockState state, long capacity) {
        super(BlockEntityRegistry.FLUID_TANK.get(), pos, state);
        fluidTank = new SingleFluidStorage(capacity) {
            @Override
            protected void onChange() {
                setChanged();
                if (level != null && level.getServer() != null && !level.getServer().getPlayerList().getPlayers().isEmpty()) {
                    NetworkManager.sendToPlayers(level.getServer().getPlayerList().getPlayers(),
                            new SyncFluidPacketWithoutDirection(new FluidAmountMapDataComponent(List.of(getFluidInTank(0).getFluid()), List.of(getFluidValueInTank())), 0, getBlockPos()));
                }
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        fluidTank.save(tag, registries, "fluid_tank");
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        fluidTank.load(tag, registries, "fluid_tank");
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
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public void tick() {
        int initialRenderStage = renderStage;

        //First - Insert slot
        if (!items.getFirst().isEmpty())
            FluidUtil.moveFluidFromItem(0, 0, items, fluidTank, 1000);
        //Last - Extract slot
        if (!items.getLast().isEmpty())
            FluidUtil.moveFluidToItem(0, fluidTank, 1, items, 1000);

        FluidUtil.distributeFluidNearby(level, worldPosition, fluidTank.getFluidInTank(0).copyWithAmount(fluidTank.getTankCapacity(0) / 20));

        //Update render stage
        renderStage = Math.toIntExact((fluidTank.getFluidValueInTank() * 9) / fluidTank.getTankCapacity(0));

        if (initialRenderStage != renderStage) {
            BlockState state = getBlockState().setValue(FluidTankBlock.STAGE, renderStage);
            level.setBlock(getBlockPos(), state, 3);
            setChanged();
        }
    }

   @Override
    protected Component getDefaultName() {
        return Component.translatable("screen.stellaris.fluid_tank");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new FluidTankMenu(containerId, inventory, this, this);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    public SingleFluidStorage getFluidTank() {
        return fluidTank;
    }
}