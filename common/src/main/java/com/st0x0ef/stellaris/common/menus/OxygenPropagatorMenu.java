package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenPropagatorBlockEntity;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class OxygenPropagatorMenu extends AbstractContainerMenu {

    private final OxygenPropagatorBlockEntity blockEntity;

    public OxygenPropagatorMenu(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        this(containerId, inventory, (OxygenPropagatorBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public OxygenPropagatorMenu(int containerId, Inventory inventory, OxygenPropagatorBlockEntity blockEntity) {
        super(MenuTypesRegistry.OXYGEN_PROPAGATOR.get(), containerId);
        this.blockEntity = blockEntity;

        addPlayerHotbar(inventory);
        addPlayerInventory(inventory);
    }

    public OxygenPropagatorBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null; // TODO
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(blockEntity.getBlockPos().getCenter()) <= 64;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, (84 + i * 18) + 61));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 204));
        }
    }
}
