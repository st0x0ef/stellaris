package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.armors.JetSuit;
import com.st0x0ef.stellaris.common.entities.RocketEntity;
import com.st0x0ef.stellaris.common.keybinds.KeyVariables;
import com.st0x0ef.stellaris.common.menus.PlanetSelectionMenu;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class KeyHandlerPacket extends BaseC2SMessage {
    public final String key;
    public final boolean condition;

    public KeyHandlerPacket(String key, boolean condition) {
        this.key = key;
        this.condition = condition;
    }

    public KeyHandlerPacket(FriendlyByteBuf buffer) {
        this.key = buffer.readUtf();
        this.condition = buffer.readBoolean();
    }

    @Override
    public MessageType getType() {
        return NetworkRegistry.KEY_HANDLER_ID;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(key);
        buffer.writeBoolean(condition);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        context.queue(() -> {
            switch (key) {
                case "key_up":
                    KeyVariables.KEY_UP.put(player.getUUID(), condition);
                    break;

                case "key_down":
                    KeyVariables.KEY_DOWN.put(player.getUUID(), condition);
                    break;

                case "key_right":
                    KeyVariables.KEY_RIGHT.put(player.getUUID(), condition);
                    break;

                case "key_left":
                    KeyVariables.KEY_LEFT.put(player.getUUID(), condition);
                    break;
                case "switch_jet_suit_mode":
                    if (Utils.isLivingInJetSuit(player)) {
                        ItemStack itemStack = player.getItemBySlot(EquipmentSlot.CHEST);
                        JetSuit.Suit item = (JetSuit.Suit) itemStack.getItem();
                        item.switchJetSuitMode(player, itemStack);
                    }
                    break;
                case "rocket_start":
                    if (player.getVehicle() instanceof RocketEntity rocketEntity) rocketEntity.startRocket();
                    break;
                case "key_jump":
                    KeyVariables.KEY_JUMP.put(player.getUUID(), condition);
                    break;
                case "freeze_planet_menu":
                    if (player.containerMenu instanceof PlanetSelectionMenu menu) menu.switchFreezeGui();
                    break;
                default:
                    Stellaris.LOG.error("unknown key action {}", key);
            }
        });
    }
}
