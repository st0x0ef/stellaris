package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.blocks.entities.machines.FuelRefineryBlockEntity;
import com.st0x0ef.stellaris.common.menus.slot.FluidContainerSlot;
import com.st0x0ef.stellaris.common.menus.slot.ResultSlot;
import com.st0x0ef.stellaris.common.menus.slot.SpecificFluidContainerSlot;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class FuelRefineryMenu extends BaseContainer {

    private final Container container;
    private final FuelRefineryBlockEntity blockEntity;

    public static FuelRefineryMenu create(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        FuelRefineryBlockEntity blockEntity = (FuelRefineryBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos());
        return new FuelRefineryMenu(containerId, inventory, new SimpleContainer(6), blockEntity);
    }

    public FuelRefineryMenu(int containerId, Inventory inventory, Container container, FuelRefineryBlockEntity blockEntity) {
        super(MenuTypesRegistry.FUEL_REFINERY.get(), containerId, 6, inventory, 10, 142);
        this.container = container;
        this.blockEntity = blockEntity;

        // Ingredient tank
        addSlot(new SpecificFluidContainerSlot(container, FluidRegistry.OIL_STILL.get(), 0, 14, 76, false));
        addSlot(new ResultSlot(container, 1, 14, 110));

        // Fuel tank
        addSlot(new FluidContainerSlot(container, 2, 102, 76, false));
        addSlot(new ResultSlot(container, 3, 102, 110));

        // Diesel tank
        addSlot(new FluidContainerSlot(container, 4, 150, 76, false));
        addSlot(new ResultSlot(container, 5, 150, 110));
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public FuelRefineryBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
