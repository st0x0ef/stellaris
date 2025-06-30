package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.blocks.entities.FlagBlockEntity;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class FlagUploadMenu extends BaseContainer {


    private final FlagBlockEntity blockEntity;

    public static FlagUploadMenu create(int syncId, Inventory inventory, FriendlyByteBuf data) {
        return new FlagUploadMenu(syncId, inventory, (FlagBlockEntity) inventory.player.level().getBlockEntity(data.readBlockPos()));
    }

    public FlagUploadMenu(int syncId, Inventory playerInventory, FlagBlockEntity entity) {
        super(MenuTypesRegistry.FLAG_MENU.get(), syncId, 2, playerInventory, 10, 106);

        this.blockEntity = entity;

    }

    public FlagBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.isAlive();
    }
}
