package com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen;

import com.st0x0ef.stellaris.common.armors.JetSuit;
import com.st0x0ef.stellaris.common.blocks.entities.machines.BaseEnergyContainerBlockEntity;
import com.st0x0ef.stellaris.common.menus.OxygenDistributorMenu;
import com.st0x0ef.stellaris.common.oxygen.OxygenContainer;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class OxygenDistributorBlockEntity extends BaseEnergyContainerBlockEntity implements OxygenContainerBlockEntity {

    private final OxygenContainer oxygenContainer = new OxygenContainer(6000);
    private int timer = 0;

    public OxygenDistributorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.OXYGEN_DISTRIBUTOR.get(), pos, state);
    }

    @Override
    public void tick() {
        if (getWrappedEnergyContainer().getStoredEnergy() > 0 && oxygenContainer.getOxygenStored() > 0) {
            if (PlanetUtil.isPlanet(level.dimension())) {
                timer++;
                if (timer >= 4) {
                    oxygenContainer.addOxygenAt(getBlockPos(), false);
                    oxygenContainer.tick(level);
                    timer = 0;
                }
            }

            if (this.getItem(1).getItem() instanceof JetSuit.Suit jetSuit) {
                if (oxygenContainer.removeOxygenStored(100, true) && jetSuit.oxygenContainer.addOxygenStored(100, true)) {
                    oxygenContainer.removeOxygenStored(100, false);
                    jetSuit.oxygenContainer.addOxygenStored(100, false);
                }
            }
        }
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
        return 2;
    }

    @Override
    public OxygenContainer getOxygenContainer() {
        return oxygenContainer;
    }

    @Override
    public int getRange() {
        return 32;
    }
}
