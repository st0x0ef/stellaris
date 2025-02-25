package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.fej1fun.potentials.capabilities.Capabilities;
import com.fej1fun.potentials.components.FluidAmountMapDataComponent;
import com.fej1fun.potentials.fluid.UniversalFluidStorage;
import com.fej1fun.potentials.providers.FluidProvider;
import com.st0x0ef.stellaris.common.network.packets.SyncFluidPacketWithoutDirection;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.FluidUtil;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.SingleFluidStorage;
import dev.architectury.fluid.FluidStack;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WaterPumpBlockEntity extends BaseEnergyBlockEntity implements FluidProvider.BLOCK {

    private static final int NEEDED_ENERGY = 100;
    private final SingleFluidStorage waterTank = new SingleFluidStorage(1000) {
        @Override
        protected void onChange() {
            setChanged();
            if (level != null && level.getServer() != null && !level.getServer().getPlayerList().getPlayers().isEmpty())
                NetworkManager.sendToPlayers(level.getServer().getPlayerList().getPlayers(),
                        new SyncFluidPacketWithoutDirection(new FluidAmountMapDataComponent(List.of(getFluidInTank(0).getFluid()), List.of(getFluidValueInTank())), 0, getBlockPos()));

        }
    };

    public WaterPumpBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.WATER_PUMP.get(), pos, state, 100);
    }

    @Override
    public void tick() {
        if (level == null) return;
        if (energyContainer.getEnergy() < NEEDED_ENERGY) return;

        BlockPos belowPos = worldPosition.below();
        FluidState belowFluidState = level.getFluidState(belowPos);

        if (belowFluidState.is(Fluids.WATER) && belowFluidState.isSource()) {
            BlockState belowState = level.getBlockState(belowPos);
            if (waterTank.getFluidInTank(0).isEmpty()) {
                if (belowState.getBlock() instanceof BucketPickup bucketPickup) {
                    if (!bucketPickup.pickupBlock(null, level, belowPos, belowState).isEmpty()) {
                        waterTank.fill(FluidStack.create(Fluids.WATER, 1000), false);
                        energyContainer.extract(NEEDED_ENERGY, false);
                    }
                }
            }
        }

        UniversalFluidStorage fluidCapAbove = Capabilities.Fluid.BLOCK.getCapability(level, getBlockPos().above(), Direction.DOWN);
        if (fluidCapAbove != null && !waterTank.getFluidInTank(0).isEmpty()) {
            FluidUtil.moveFluid(getFluidTank(null), fluidCapAbove, waterTank.getFluidInTank(0));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        waterTank.load(tag, provider, "water");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        waterTank.save(tag, provider, "water");
    }

    public SingleFluidStorage getWaterTank() {
        return waterTank;
    }

    @Override
    public @NotNull UniversalFluidStorage getFluidTank(@Nullable Direction direction) {
        return this.waterTank;
    }
}
