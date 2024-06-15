package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.blocks.entities.machines.FuelRefineryBlockEntity;
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

public class FuelRefineryMenu extends BaseContainer {

    private final Container container;
    private final FuelRefineryBlockEntity blockEntity;

    public static FuelRefineryMenu create(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        FuelRefineryBlockEntity blockEntity = (FuelRefineryBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos());
        return new FuelRefineryMenu(containerId, inventory, new SimpleContainer(4), blockEntity);
    }

    public FuelRefineryMenu(int containerId, Inventory inventory, Container container, FuelRefineryBlockEntity blockEntity) {
        super(MenuTypesRegistry.FUEL_REFINERY.get(), containerId, 4, inventory, 18);
        this.container = container;
        this.blockEntity = blockEntity;

        // Ingredient tank
        addSlot(new FluidContainerSlot(container, 0, 12, 22, false, false));
        addSlot(new ResultSlot(container, 1, 12, 52));

        // Result tank
        addSlot(new FluidContainerSlot(container, 2, 127, 22, true, false));
        addSlot(new ResultSlot(container, 3, 127, 52));
    }

    @Override
    public boolean stillValid(Player player) {
        if (!player.isLocalPlayer()) {
            syncWidgets((ServerPlayer) player);
        }
        return container.stillValid(player);
    }

    public FuelRefineryBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public void syncWidgets(ServerPlayer player) {
        if (!player.level().isClientSide()) {
            NetworkRegistry.sendToPlayer(player, NetworkRegistry.SYNC_FLUID_TANKS_ID, SyncWidgetsTanks.encode(new SyncWidgetsTanks(
                    new long[] {blockEntity.getIngredientTank().getAmount(), blockEntity.getResultTank().getAmount()},
                    new ResourceLocation[] {blockEntity.getIngredientTank().getStack().getFluid().arch$registryName(), blockEntity.getResultTank().getStack().getFluid().arch$registryName()}
            ), WaterSeparatorMenu.createBuf(player)));
            NetworkRegistry.sendToPlayer(player, NetworkRegistry.SYNC_FLUID_TANKS_ID, SyncWidgetsTanks.encode(new SyncWidgetsTanks(
                    new long[] {blockEntity.getWrappedEnergyContainer().getStoredEnergy()}
            ), WaterSeparatorMenu.createBuf(player)));
        }
    }
}
