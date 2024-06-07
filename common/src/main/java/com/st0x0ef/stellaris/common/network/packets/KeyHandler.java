package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.registries.KeyMappings;
import com.st0x0ef.stellaris.common.keybinds.KeyVariables;
import com.st0x0ef.stellaris.common.menus.PlanetSelectionMenu;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class KeyHandler {
    public final String key;
    public final boolean condition;

    public KeyHandler(String key, boolean condition) {
        this.key = key;
        this.condition = condition;
    }

    public KeyHandler(RegistryFriendlyByteBuf buffer) {
        this.key = buffer.readUtf();
        this.condition = buffer.readBoolean();
    }

    public static RegistryFriendlyByteBuf encode(KeyHandler message, RegistryFriendlyByteBuf buffer) {
        buffer.writeUtf(message.key);
        buffer.writeBoolean(message.condition);
        return buffer;
    }

    public static void apply(RegistryFriendlyByteBuf buffer, NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        context.queue(() -> {
            switch (buffer.readUtf()) {
                case "rocket_start":
                    KeyMappings.startRocket(player);
                    break;
                case "key_jump":
                    KeyVariables.KEY_JUMP.put(player.getUUID(), buffer.readBoolean());
                    break;
                case "freeze_planet_menu":
                    if (player.containerMenu instanceof PlanetSelectionMenu menu) {
                        Stellaris.LOG.error("Switching freeze gui");
                        menu.switchFreezeGui();
                    }
                    break;
            }
        });
    }


}
