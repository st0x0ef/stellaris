package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.blocks.entities.machines.WaterSeparatorBlockEntity;
import com.st0x0ef.stellaris.common.menus.slot.WaterSeparatorSlot;
import com.st0x0ef.stellaris.common.menus.slot.ResultSlot;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.network.packets.SyncWidgetsTanks;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class WaterSeparatorMenu extends BaseContainer {

    private final Container container;
    private final WaterSeparatorBlockEntity blockEntity;

    public static WaterSeparatorMenu create(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        WaterSeparatorBlockEntity blockEntity = (WaterSeparatorBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos());
        return new WaterSeparatorMenu(containerId, inventory, new SimpleContainer(4), blockEntity);
    }

    public WaterSeparatorMenu(int containerId, Inventory inventory, Container container, WaterSeparatorBlockEntity blockEntity) {
        super(MenuTypesRegistry.WATER_SEPARATOR_MENU.get(), containerId, 4, inventory, 58);
        this.container = container;
        this.blockEntity = blockEntity;

        addSlot(new ResultSlot(container, 0, 104, 114)); // Water tank output
        addSlot(new WaterSeparatorSlot(container, 1, 56, 114, false, false)); // Water tank input
        addSlot(new WaterSeparatorSlot(container, 2, 20, 114, true, true)); // Left tank output
        addSlot(new WaterSeparatorSlot(container, 3, 140, 114, true, true)); // Right tank output
    }

    @Override
    public boolean stillValid(Player player) {
        if (!player.isLocalPlayer()) {
            this.syncWidgets((ServerPlayer) player);
        }

        return container.stillValid(player);
    }

    public WaterSeparatorBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public void syncWidgets(ServerPlayer player) {

        if (!player.level().isClientSide()) {
            RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), player.level().getServer().registryAccess());

            List<Long> values
                    = new ArrayList<Long>();

            this.blockEntity.getResultTanks().forEach((fluidTank -> {
                values.add(fluidTank.getAmount());
            }));

            Long[] simpleArray = new Long[ values.size() ];
            values.toArray( simpleArray );

            NetworkRegistry.sendToPlayer(player, NetworkRegistry.SYNC_FLUID_TANKS_ID, SyncWidgetsTanks.encode(new SyncWidgetsTanks( ArrayUtils.toPrimitive(simpleArray)), buffer));
            NetworkRegistry.sendToPlayer(player, NetworkRegistry.SYNC_FLUID_TANKS_ID, SyncWidgetsTanks.encode(new SyncWidgetsTanks(new long[]{this.blockEntity.ingredientTank.getAmount()}), buffer));

        }
    }


}
