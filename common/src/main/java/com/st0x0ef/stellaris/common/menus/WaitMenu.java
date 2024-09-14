package com.st0x0ef.stellaris.common.menus;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class WaitMenu extends AbstractContainerMenu {
    private final Player player;
    public boolean freeze_gui = false;
    public final String firstPlayerName;

    public static WaitMenu create(int syncId, Inventory inventory, FriendlyByteBuf data) {
        return new WaitMenu(syncId, inventory, data.readUtf());
    }
    public WaitMenu(int syncId, Inventory playerInventory, String firstPlayerName)
    {
        super(MenuTypesRegistry.WAIT_MENU.get(), syncId);
        this.player = playerInventory.player;
        this.firstPlayerName = firstPlayerName;
    }
    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        return ItemStack.EMPTY;
    }
    @Override
    public boolean stillValid(Player player) {
        return !player.isDeadOrDying();
    }

    public Player getPlayer() {
        return player;
    }

    public void switchFreezeGui() {
        freeze_gui = !freeze_gui;
    }

    public String getFirstPlayerName() {
        return firstPlayerName;
    }
}

