package com.st0x0ef.stellaris.common.menus;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class TestMenu extends AbstractContainerMenu {

    public static TestMenu create(int syncId, Inventory inventory, FriendlyByteBuf data) {
        return new TestMenu(syncId, inventory);
    }
    public TestMenu(int syncId, Inventory playerInventory)
    {
        super(MenuTypesRegistry.TEST_MENU.get(), syncId);
    }
    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        return ItemStack.EMPTY;
    }
    @Override
    public boolean stillValid(Player player) {
        return !player.isDeadOrDying();
    }
}

