package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.data_components.RocketComponent;
import com.st0x0ef.stellaris.common.menus.RocketMenu;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class SyncRocketComponent extends BaseS2CMessage {

    private final RocketComponent component;

    public SyncRocketComponent(RegistryFriendlyByteBuf buffer) {
        this(RocketComponent.fromNetwork(buffer));
    }

    public SyncRocketComponent(RocketComponent component) {
        this.component = component;
    }

    @Override
    public MessageType getType() {
        return NetworkRegistry.SYNC_ROCKET_COMPONENT_ID;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        this.component.toNetwork(buf);

    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        LocalPlayer player = (LocalPlayer) context.getPlayer();
        if (player.containerMenu instanceof RocketMenu menu) {
            menu.getRocket().rocketComponent = this.component;
        }
    }
}
