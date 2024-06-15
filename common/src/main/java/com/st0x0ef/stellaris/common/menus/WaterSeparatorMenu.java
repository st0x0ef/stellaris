package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTank;
import com.st0x0ef.stellaris.common.blocks.entities.machines.WaterSeparatorBlockEntity;
import com.st0x0ef.stellaris.common.menus.slot.FluidContainerSlot;
import com.st0x0ef.stellaris.common.menus.slot.ResultSlot;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.network.packets.SyncWidgetsTanks;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

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
        addSlot(new FluidContainerSlot(container, 1, 56, 114, false, false)); // Water tank input
        addSlot(new FluidContainerSlot(container, 2, 20, 114, true, true)); // Left tank output
        addSlot(new FluidContainerSlot(container, 3, 140, 114, true, true)); // Right tank output
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
            FluidTank resultTank1 = blockEntity.getResultTanks().getFirst();
            FluidTank resultTank2 = blockEntity.getResultTanks().get(1);
            NetworkRegistry.sendToPlayer(player, NetworkRegistry.SYNC_FLUID_TANKS_ID, SyncWidgetsTanks.encode(new SyncWidgetsTanks(
                    new long[] {resultTank1.getAmount(), resultTank2.getAmount()},
                    new ResourceLocation[] {resultTank1.getStack().getFluid().arch$registryName(), resultTank2.getStack().getFluid().arch$registryName()}
            ), createBuf(player)));
            NetworkRegistry.sendToPlayer(player, NetworkRegistry.SYNC_FLUID_TANKS_ID, SyncWidgetsTanks.encode(new SyncWidgetsTanks(
                    new long[] {blockEntity.ingredientTank.getAmount()},
                    new ResourceLocation[] {blockEntity.ingredientTank.getStack().getFluid().arch$registryName()}
            ), createBuf(player)));
            NetworkRegistry.sendToPlayer(player, NetworkRegistry.SYNC_FLUID_TANKS_ID, SyncWidgetsTanks.encode(new SyncWidgetsTanks(
                    new long[] {blockEntity.getWrappedEnergyContainer().getStoredEnergy(), 0, 0} // The 0s are fillers, so the array size is 3
            ), createBuf(player)));
        }
    }

    public static RegistryFriendlyByteBuf createBuf(Player player) {
        return new RegistryFriendlyByteBuf(Unpooled.buffer(), player.level().getServer().registryAccess());
    }
}
