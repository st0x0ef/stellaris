package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.fej1fun.potentials.capabilities.Capabilities;
import com.fej1fun.potentials.fluid.UniversalFluidStorage;
import com.fej1fun.potentials.providers.FluidProvider;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.menus.OxygenDistributorMenu;
import com.st0x0ef.stellaris.common.network.packets.SyncFluidPacketWithoutDirection;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.FilteredFluidStorage;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.FluidStorage;
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
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class OxygenDistributorBlockEntity extends BaseEnergyContainerBlockEntity implements FluidProvider.BLOCK {

    public final FluidStorage oxygenTank;


    public OxygenDistributorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.OXYGEN_DISTRIBUTOR.get(), pos, state);

        this.oxygenTank = new FilteredFluidStorage(1, 20000, (n,fluidStack) -> fluidStack.getFluid().isSame(FluidRegistry.OXYGEN_STILL.get())) {
            @Override
            protected void onChange(int i) {
                setChanged();
                if (level != null && level.getServer() != null && !level.getServer().getPlayerList().getPlayers().isEmpty() && !this.getFluidInTank(0).isEmpty())
                    NetworkManager.sendToPlayers(level.getServer().getPlayerList().getPlayers(),
                            new SyncFluidPacketWithoutDirection(this.getFluidInTank(0), 0, getBlockPos()));
            }
        };
    }

    @Override
    public void tick() {
        UniversalFluidStorage oxygenTankItemStorage = Capabilities.Fluid.ITEM.getCapability(getItem(0));
        if (oxygenTankItemStorage != null) {
            if (oxygenTankItemStorage.getFluidInTank(0).getAmount() > 0 && oxygenTank.getFluidValueInTank(0) < oxygenTank.getTankCapacity(0)) {
                FluidStack stack = oxygenTankItemStorage.getFluidInTank(0).copyWithAmount(1);
                oxygenTankItemStorage.drain(stack, false);
                oxygenTank.fill(stack, false);
            }
        }

        if (level instanceof ServerLevel serverLevel && !oxygenTank.isEmpty()) {
            GlobalOxygenManager.getInstance().getOrCreateDimensionManager(serverLevel).tickOxygenRoom(getBlockPos());
        }
    }

    public boolean useOxygenAndEnergy() {
        if (oxygenTank.getFluidValueInTank(0) > 0 && this.energyContainer.getEnergy() >= 3) {
            oxygenTank.drain(oxygenTank.getFluidInTank(0).copyWithAmount(1), false);
            this.energyContainer.extract(3, false);
            return true;
        }
        return false;
    }

    public long addOxygen(long amount) {
        FluidStack stack = oxygenTank.isEmpty() ? FluidStack.create(FluidRegistry.OXYGEN_STILL.get(), amount) : oxygenTank.getFluidInTank(0).copyWithAmount(amount);
        return oxygenTank.fill(stack, false);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.stellaris.oxygen_distributor");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new OxygenDistributorMenu(containerId, inventory, this, this);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        oxygenTank.load(tag, provider, "oxygen");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        oxygenTank.save(tag, provider, "oxygen");
    }


    @Override
    public @Nullable UniversalFluidStorage getFluidTank(@Nullable Direction direction) {
        return oxygenTank;
    }
}
