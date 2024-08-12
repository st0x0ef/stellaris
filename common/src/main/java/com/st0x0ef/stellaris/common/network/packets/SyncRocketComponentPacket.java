package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.data_components.RocketComponent;
import com.st0x0ef.stellaris.common.menus.RocketMenu;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;

public class SyncRocketComponentPacket extends BaseS2CMessage {

    private final RocketComponent component;

    public SyncRocketComponentPacket(FriendlyByteBuf buffer) {
        this(RocketComponent.fromNetwork(buffer));
    }

    public SyncRocketComponentPacket(RocketComponent component) {
        this.component = component;
    }

    @Override
    public MessageType getType() {
        return NetworkRegistry.SYNC_ROCKET_COMPONENT_ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        component.toNetwork(buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        LocalPlayer player = (LocalPlayer) context.getPlayer();
        if (player.containerMenu instanceof RocketMenu menu) {
            menu.getRocket().rocketComponent = component;

            menu.getRocket().MODEL_UPGRADE = component.getModelUpgrade();
            menu.getRocket().SKIN_UPGRADE = component.getSkinUpgrade();
            menu.getRocket().TANK_UPGRADE = component.getTankUpgrade();
            menu.getRocket().MOTOR_UPGRADE = component.getMotorUpgrade();
        }
    }
}
