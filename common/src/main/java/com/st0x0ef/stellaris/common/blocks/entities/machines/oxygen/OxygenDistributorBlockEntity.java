package com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen;

import com.st0x0ef.stellaris.common.blocks.entities.ImplementedInventory;
import com.st0x0ef.stellaris.common.menus.OxygenDistributorMenu;
import com.st0x0ef.stellaris.common.oxygen.OxygenContainer;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class OxygenDistributorBlockEntity extends OxygenBlockEntity implements ImplementedInventory {

    private int timer = 0;
    private NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);

    public OxygenDistributorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.OXYGEN_DISTRIBUTOR.get(), pos, state, new OxygenContainer(6000), 32);
    }

    @Override
    public void tick() {
        timer++;
        if (timer >= 4) {
            timer = 0;
            if (PlanetUtil.isPlanet(this.level.dimension())) {
                container.addOxygenAt(this.getBlockPos(), false);
                container.tick(this.level);
            }
        }
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
}
