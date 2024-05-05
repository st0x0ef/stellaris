package com.st0x0ef.stellaris.common.menus;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class PlanetSelectionMenu extends AbstractContainerMenu {
    private final Container inventory;
    protected PlanetSelectionMenu(@Nullable MenuType<?> menuType, int i, Container inventory) {
        super(menuType, i);
        this.inventory = inventory;
    }
    public static PlanetSelectionMenu create(int syncId, Inventory inventory) {

        return new PlanetSelectionMenu(syncId, inventory, new SimpleContainer(0));
    }
    public PlanetSelectionMenu(int syncId, Inventory playerInventory, Container container)
    {
        super(MenuTypesRegistry.PLANET_SELECTION_MENU.get(), syncId);
        this.inventory = (container);
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

