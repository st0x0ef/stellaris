package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.data_components.RocketComponent;
import com.st0x0ef.stellaris.common.menus.RocketMenu;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.apache.commons.lang3.SerializationUtils;

public class SyncRocketComponent {

    private final RocketComponent component;

    public SyncRocketComponent(RegistryFriendlyByteBuf buffer) {
        this((RocketComponent) SerializationUtils.deserialize(buffer.readByteArray()));
    }
    public SyncRocketComponent(RocketComponent component) {
        this.component = component;
    }

    public static RegistryFriendlyByteBuf encode(SyncRocketComponent message, RegistryFriendlyByteBuf buffer) {
        buffer.writeByteArray(SerializationUtils.serialize(message.component));
        return buffer;
    }

    public static void apply(RegistryFriendlyByteBuf buffer, NetworkManager.PacketContext context) {
        LocalPlayer player = (LocalPlayer) context.getPlayer();
        if (player.containerMenu instanceof RocketMenu menu) {
            menu.getRocket().rocketComponent = new SyncRocketComponent(buffer).component;
        }
    }
}
