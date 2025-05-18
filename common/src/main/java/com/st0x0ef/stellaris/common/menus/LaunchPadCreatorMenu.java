package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.entities.machines.FuelRefineryBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.LaunchPadCreatorBlockEntity;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.menus.slot.FluidContainerSlot;
import com.st0x0ef.stellaris.common.menus.slot.ResultSlot;
import com.st0x0ef.stellaris.common.menus.slot.SpecificFluidContainerSlot;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class LaunchPadCreatorMenu extends AbstractContainerMenu {

    private final Player player;
    private final LaunchPadCreatorBlockEntity blockEntity;
    public int launchPadId;

    public static LaunchPadCreatorMenu create(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        LaunchPadCreatorBlockEntity blockEntity = (LaunchPadCreatorBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos());
        return new LaunchPadCreatorMenu(containerId, inventory, blockEntity, buf.readInt());
    }

    public LaunchPadCreatorMenu(int containerId, Inventory inventory, LaunchPadCreatorBlockEntity blockEntity, int launchPadId) {
        super(MenuTypesRegistry.LAUNCHPAD_CREATOR_MENU.get(), containerId);
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
        return !player.isDeadOrDying();
    }

    public Player getPlayer() {
        return player;
    }

    public LaunchPadCreatorBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
