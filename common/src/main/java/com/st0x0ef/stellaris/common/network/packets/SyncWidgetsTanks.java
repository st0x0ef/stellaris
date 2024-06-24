package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.blocks.entities.machines.FuelRefineryBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.WaterSeparatorBlockEntity;
import com.st0x0ef.stellaris.common.menus.*;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.fabricmc.api.EnvType;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class SyncWidgetsTanks extends BaseS2CMessage {

    private final long[] component;
    private final ResourceLocation[] locations;

    public SyncWidgetsTanks(RegistryFriendlyByteBuf buffer) {
        this.component = buffer.readLongArray();
        int length = buffer.readInt();
        this.locations = new ResourceLocation[length];

        for (int i = 0; i < length; i++) {
            locations[i] = buffer.readResourceLocation();
        }
    }

    public SyncWidgetsTanks(long[] component) {
        this(component, new ResourceLocation[] {});
    }

    public SyncWidgetsTanks(long[] values, ResourceLocation[] locations) {
        this.component = values;
        this.locations = locations;
    }


    public static void apply(RegistryFriendlyByteBuf buffer, NetworkManager.PacketContext context) {
        if (context.getEnv() != EnvType.CLIENT) {
            return;
        }

        LocalPlayer player = (LocalPlayer) context.getPlayer();
        SyncWidgetsTanks syncWidgetsTanks = new SyncWidgetsTanks(buffer);
        switch (player.containerMenu) {
            case WaterSeparatorMenu menu -> {
                WaterSeparatorBlockEntity blockEntity = menu.getBlockEntity();
                if (syncWidgetsTanks.component.length == 2 && syncWidgetsTanks.locations.length == 2) {
                    blockEntity.resultTanks.getFirst().setFluid(BuiltInRegistries.FLUID.get(syncWidgetsTanks.locations[0]), syncWidgetsTanks.component[0]);
                    blockEntity.resultTanks.getLast().setFluid(BuiltInRegistries.FLUID.get(syncWidgetsTanks.locations[1]), syncWidgetsTanks.component[1]);
                }
                else if (syncWidgetsTanks.component.length == 1 && syncWidgetsTanks.locations.length == 1) {
                    blockEntity.ingredientTank.setFluid(BuiltInRegistries.FLUID.get(syncWidgetsTanks.locations[0]), syncWidgetsTanks.component[0]);
                }
                else if (syncWidgetsTanks.component.length == 3) {
                    blockEntity.getWrappedEnergyContainer().setEnergy(syncWidgetsTanks.component[0]);
                }
            }
            case FuelRefineryMenu menu -> {
                FuelRefineryBlockEntity blockEntity = menu.getBlockEntity();
                if (syncWidgetsTanks.component.length == 2 && syncWidgetsTanks.locations.length == 2) {
                    blockEntity.getIngredientTank().setFluid(BuiltInRegistries.FLUID.get(syncWidgetsTanks.locations[0]), syncWidgetsTanks.component[0]);
                    blockEntity.getResultTank().setFluid(BuiltInRegistries.FLUID.get(syncWidgetsTanks.locations[1]), syncWidgetsTanks.component[1]);
                }
                else if (syncWidgetsTanks.component.length == 1) {
                    blockEntity.getWrappedEnergyContainer().setEnergy(syncWidgetsTanks.component[0]);
                }
            }
            case SolarPanelMenu menu -> menu.getEnergyContainer().setEnergy(syncWidgetsTanks.component[0]);
            case CoalGeneratorMenu menu ->
                    menu.getBlockEntity().getWrappedEnergyContainer().setEnergy(syncWidgetsTanks.component[0]);
            case RadioactiveGeneratorMenu menu ->
                    menu.getBlockEntity().getWrappedEnergyContainer().setEnergy(syncWidgetsTanks.component[0]);
            case OxygenDistributorMenu menu ->
                    menu.getBlockEntity().getWrappedEnergyContainer().setEnergy(syncWidgetsTanks.component[0]);
            default -> {
            }
        }
    }

    @Override
    public MessageType getType() {
        return NetworkRegistry.SYNC_FLUID_TANKS_ID;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buffer) {
        buffer.writeLongArray(this.component);

        buffer.writeInt(this.locations.length);
        for (ResourceLocation location : this.locations) {
            buffer.writeResourceLocation(location);
        }

    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (context.getEnv() != EnvType.CLIENT) {
            return;
        }

        LocalPlayer player = (LocalPlayer) context.getPlayer();
        SyncWidgetsTanks syncWidgetsTanks = this;
        switch (player.containerMenu) {
            case WaterSeparatorMenu menu -> {
                WaterSeparatorBlockEntity blockEntity = menu.getBlockEntity();
                if (syncWidgetsTanks.component.length == 2 && syncWidgetsTanks.locations.length == 2) {
                    blockEntity.resultTanks.getFirst().setFluid(BuiltInRegistries.FLUID.get(syncWidgetsTanks.locations[0]), syncWidgetsTanks.component[0]);
                    blockEntity.resultTanks.getLast().setFluid(BuiltInRegistries.FLUID.get(syncWidgetsTanks.locations[1]), syncWidgetsTanks.component[1]);
                }
                else if (syncWidgetsTanks.component.length == 1 && syncWidgetsTanks.locations.length == 1) {
                    blockEntity.ingredientTank.setFluid(BuiltInRegistries.FLUID.get(syncWidgetsTanks.locations[0]), syncWidgetsTanks.component[0]);
                }
                else if (syncWidgetsTanks.component.length == 3) {
                    blockEntity.getWrappedEnergyContainer().setEnergy(syncWidgetsTanks.component[0]);
                }
            }
            case FuelRefineryMenu menu -> {
                FuelRefineryBlockEntity blockEntity = menu.getBlockEntity();
                if (syncWidgetsTanks.component.length == 2 && syncWidgetsTanks.locations.length == 2) {
                    blockEntity.getIngredientTank().setFluid(BuiltInRegistries.FLUID.get(syncWidgetsTanks.locations[0]), syncWidgetsTanks.component[0]);
                    blockEntity.getResultTank().setFluid(BuiltInRegistries.FLUID.get(syncWidgetsTanks.locations[1]), syncWidgetsTanks.component[1]);
                }
                else if (syncWidgetsTanks.component.length == 1) {
                    blockEntity.getWrappedEnergyContainer().setEnergy(syncWidgetsTanks.component[0]);
                }
            }
            case SolarPanelMenu menu -> menu.getEnergyContainer().setEnergy(syncWidgetsTanks.component[0]);
            case CoalGeneratorMenu menu ->
                    menu.getBlockEntity().getWrappedEnergyContainer().setEnergy(syncWidgetsTanks.component[0]);
            case RadioactiveGeneratorMenu menu ->
                    menu.getBlockEntity().getWrappedEnergyContainer().setEnergy(syncWidgetsTanks.component[0]);
            case OxygenDistributorMenu menu ->
                    menu.getBlockEntity().getWrappedEnergyContainer().setEnergy(syncWidgetsTanks.component[0]);
            default -> {
            }
        }
    }
}
