package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.blocks.entities.machines.FuelRefineryBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.WaterSeparatorBlockEntity;
import com.st0x0ef.stellaris.common.menus.*;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class SyncWidgetsTanksPacket extends BaseS2CMessage {

    private final long[] component;
    private final ResourceLocation[] locations;

    public SyncWidgetsTanksPacket(FriendlyByteBuf buffer) {
        this.component = buffer.readLongArray();
        int length = buffer.readInt();
        this.locations = new ResourceLocation[length];

        for (int i = 0; i < length; i++) {
            locations[i] = buffer.readResourceLocation();
        }
    }

    public SyncWidgetsTanksPacket(long[] component) {
        this(component, new ResourceLocation[] {});
    }

    public SyncWidgetsTanksPacket(long[] values, ResourceLocation[] locations) {
        this.component = values;
        this.locations = locations;
    }

    @Override
    public MessageType getType() {
        return NetworkRegistry.SYNC_FLUID_TANKS_ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeLongArray(component);

        buf.writeInt(locations.length);
        for (ResourceLocation location : locations) {
            buf.writeResourceLocation(location);
        }
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        LocalPlayer player = (LocalPlayer) context.getPlayer();
        if (player.containerMenu instanceof WaterSeparatorMenu menu) {
            WaterSeparatorBlockEntity blockEntity = menu.getBlockEntity();
            if (component.length == 2 && locations.length == 2) {
                blockEntity.resultTanks.getFirst().setFluid(BuiltInRegistries.FLUID.get(locations[0]), component[0]);
                blockEntity.resultTanks.getLast().setFluid(BuiltInRegistries.FLUID.get(locations[1]), component[1]);
            }
            else if (component.length == 1 && locations.length == 1) {
                blockEntity.ingredientTank.setFluid(BuiltInRegistries.FLUID.get(locations[0]), component[0]);
            }
            else if (component.length == 3) {
                blockEntity.getWrappedEnergyContainer().setEnergy(component[0]);
            }
        } else if (player.containerMenu instanceof FuelRefineryMenu menu) {
            FuelRefineryBlockEntity blockEntity = menu.getBlockEntity();
            if (component.length == 2 && locations.length == 2) {
                blockEntity.getIngredientTank().setFluid(BuiltInRegistries.FLUID.get(locations[0]), component[0]);
                blockEntity.getResultTank().setFluid(BuiltInRegistries.FLUID.get(locations[1]), component[1]);
            }
            else if (component.length == 1) {
                blockEntity.getWrappedEnergyContainer().setEnergy(component[0]);
            }
        } else if (player.containerMenu instanceof SolarPanelMenu menu) {
            menu.getEnergyContainer().setEnergy(component[0]);
        } else if (player.containerMenu instanceof CoalGeneratorMenu menu) {
            menu.getBlockEntity().getWrappedEnergyContainer().setEnergy(component[0]);
        } else if (player.containerMenu instanceof RadioactiveGeneratorMenu menu) {
            menu.getBlockEntity().getWrappedEnergyContainer().setEnergy(component[0]);
        } else if (player.containerMenu instanceof OxygenDistributorMenu menu) {
            menu.getBlockEntity().getWrappedEnergyContainer().setEnergy(component[0]);
        }
    }
}
