package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.fej1fun.potentials.components.FluidAmountMapDataComponent;
import com.fej1fun.potentials.fluid.UniversalFluidStorage;
import com.fej1fun.potentials.providers.FluidProvider;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.machines.DieselGeneratorBlock;
import com.st0x0ef.stellaris.common.menus.DieselGeneratorMenu;
import com.st0x0ef.stellaris.common.network.packets.SyncFluidPacketWithoutDirection;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
import com.st0x0ef.stellaris.common.utils.capabilities.energy.EnergyUtil;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DieselGeneratorBlockEntity extends BaseGeneratorBlockEntity implements FluidProvider.BLOCK {

    private int litTime;
    private int litDuration;

    private final SingleFluidStorage dieselTank;

    public DieselGeneratorBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BlockEntityRegistry.DIESEL_GENERATOR.get(), blockPos, blockState, 10, 12800);
    }

    public DieselGeneratorBlockEntity(BlockEntityType<?> entityType, BlockPos blockPos, BlockState blockState, int energyGeneratedPT, int maxCapacity) {
        super(entityType, blockPos, blockState, energyGeneratedPT, maxCapacity);

        this.dieselTank = new SingleFluidStorage(12800, 12800, 12800) {
            @Override
            protected void onChange() {
                setChanged();
                if (level != null && level.getServer() != null && !level.getServer().getPlayerList().getPlayers().isEmpty()) {
                    NetworkManager.sendToPlayers(level.getServer().getPlayerList().getPlayers(),
                            new SyncFluidPacketWithoutDirection(new FluidAmountMapDataComponent(List.of(getFluidInTank(0).getFluid()), List.of(getFluidValueInTank())), 0, getBlockPos()));
                }
            }

            @Override
            public boolean isFluidValid(int tank, FluidStack stack) {
                return stack.getFluid().isSame(FluidRegistry.DIESEL_STILL.get());
            }
        };
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new DieselGeneratorMenu(containerId, inventory, this, this);
    }

    public void tick() {
        boolean wasLit = isLit();
        boolean shouldUpdate = false;

        if (canGenerate()) {
            --litTime;
        }

        FluidUtil.moveFluidFromItem(0, 0, items, dieselTank, 1000);

        if (!dieselTank.isEmpty()) {
            int fuelTime = 20; // TODO : make this value configurable
            litDuration = fuelTime;
            litTime = fuelTime;
            dieselTank.drain(FluidStack.create(FluidRegistry.DIESEL_STILL.get(), 5), false);
            shouldUpdate = true;
        }

        if (wasLit != isLit()) {
            shouldUpdate = true;
            BlockState state = getBlockState().setValue(DieselGeneratorBlock.LIT, isLit());
            level.setBlock(getBlockPos(), state, 3);
        }
        if (shouldUpdate) {
            setChanged();
        }

        if (isLit()) {
            energyContainer.insert(energyGeneratedPT, false);
        }

        EnergyUtil.distributeEnergyNearby(level, worldPosition, maxCapacity);
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    @Override
    public boolean canGenerate() {
        boolean isMaxEnergy = energyContainer.getEnergy() == energyContainer.getMaxEnergy();
        return isLit() && !isMaxEnergy;
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        litTime = tag.getShort("BurnTime");
        dieselTank.load(tag, provider, "diesel");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putShort("BurnTime", (short) this.litTime);
        dieselTank.save(tag, provider, "diesel");
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.stellaris.diesel_generator");
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    public SingleFluidStorage getDieselTank() {
        return dieselTank;
    }

    @Override
    public @Nullable UniversalFluidStorage getFluidTank(@Nullable Direction direction) {
        return dieselTank;
    }
}
