package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.data_components.RocketComponent;
import com.st0x0ef.stellaris.common.menus.RocketMenu;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class SyncRocketComponent {

    private final RocketComponent component;

    public SyncRocketComponent(RegistryFriendlyByteBuf buffer) {
        this(RocketComponent.fromNetwork(buffer));
    }
    public SyncRocketComponent(RocketComponent component) {
        this.component = component;
    }

    public static RegistryFriendlyByteBuf encode(SyncRocketComponent message, RegistryFriendlyByteBuf buffer) {
        message.component.toNetwork(buffer);
        return buffer;
    }

    public static void apply(RegistryFriendlyByteBuf buffer, NetworkManager.PacketContext context) {
        LocalPlayer player = (LocalPlayer) context.getPlayer();
        if (player.containerMenu instanceof RocketMenu menu) {
            menu.getRocket().rocketComponent = new SyncRocketComponent(buffer).component;
        }
    }
}