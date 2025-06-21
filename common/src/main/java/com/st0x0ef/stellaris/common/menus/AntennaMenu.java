package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.blocks.entities.machines.AntennaBlockEntity;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class AntennaMenu extends AbstractContainerMenu {

    private final Player player;
    private final AntennaBlockEntity blockEntity;
    public int launchPadId;

    public static AntennaMenu create(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        AntennaBlockEntity blockEntity = (AntennaBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos());
        return new AntennaMenu(containerId, inventory, blockEntity, buf.readInt());
    }

    public AntennaMenu(int containerId, Inventory inventory, AntennaBlockEntity blockEntity, int launchPadId) {
        super(MenuTypesRegistry.ANTENNA_MENU.get(), containerId);
        this.player = inventory.player;
        this.blockEntity = blockEntity;
        this.launchPadId = launchPadId;
    }


    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.level().isClientSide();
    }

    public Player getPlayer() {
        return player;
    }

    public AntennaBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
