package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.menus.CoalGeneratorMenu;
import com.st0x0ef.stellaris.common.menus.SolarPanelMenu;
import com.st0x0ef.stellaris.common.menus.WaterSeparatorMenu;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class SyncWidgetsTanks {

    private final long[] component;

    public SyncWidgetsTanks(RegistryFriendlyByteBuf buffer) {
        this(buffer.readLongArray());
    }
    public SyncWidgetsTanks(long[] values) {
        this.component = values;
    }

    public static RegistryFriendlyByteBuf encode(SyncWidgetsTanks message, RegistryFriendlyByteBuf buffer) {
        buffer.writeLongArray(message.component);

        return buffer;
    }

    public static void apply(RegistryFriendlyByteBuf buffer, NetworkManager.PacketContext context) {
        LocalPlayer player = (LocalPlayer) context.getPlayer();
        SyncWidgetsTanks syncWidgetsTanks = new SyncWidgetsTanks(buffer);
        if (player.containerMenu instanceof WaterSeparatorMenu menu) {
            if(syncWidgetsTanks.component.length == 2) {
                menu.getBlockEntity().resultTanks.getFirst().setAmount(syncWidgetsTanks.component[0]);
                menu.getBlockEntity().resultTanks.getLast().setAmount(syncWidgetsTanks.component[1]);

                Stellaris.LOG.error("Amount in Packet {}", syncWidgetsTanks.component[1]);
                Stellaris.LOG.error("Amount in entity {}", menu.getBlockEntity().resultTanks.getLast().getAmount());
            } else {
                menu.getBlockEntity().ingredientTank.setAmount(syncWidgetsTanks.component[0]);
            }
        } else if (player.containerMenu instanceof SolarPanelMenu menu) {
            menu.getEnergyContainer().setEnergy(syncWidgetsTanks.component[0]);
        } else if (player.containerMenu instanceof CoalGeneratorMenu menu) {
            menu.getBlockEntity().getWrappedEnergyContainer().setEnergy(syncWidgetsTanks.component[0]);
        }
    }
}
