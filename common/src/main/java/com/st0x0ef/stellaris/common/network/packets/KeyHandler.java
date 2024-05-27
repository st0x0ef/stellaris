package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.client.registries.KeyMappings;
import com.st0x0ef.stellaris.common.keybinds.KeyVariables;
import com.st0x0ef.stellaris.common.menus.PlanetSelectionMenu;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class KeyHandler {
    public final String key;
    public final boolean condition;

    public KeyHandler(String key, boolean condition) {
        this.key = key;
        this.condition = condition;
    }

    public KeyHandler(FriendlyByteBuf buffer) {
        this.key = buffer.readUtf();
        this.condition = buffer.readBoolean();
    }

    public static KeyHandler decode(FriendlyByteBuf buffer) {
        return new KeyHandler(buffer);
    }

    public static void encode(KeyHandler message, FriendlyByteBuf buffer) {
        buffer.writeUtf(message.key);
        buffer.writeBoolean(message.condition);
    }

    public static void apply(KeyHandler message, Supplier<NetworkManager.PacketContext> contextSupplier) {
        NetworkManager.PacketContext context = contextSupplier.get();

        contextSupplier.get().queue(() -> {
            Player player = context.getPlayer();
            switch (message.key) {
                case "rocket_start":
                    KeyMappings.startRocket(player);
                    break;
                case "key_jump":
                    KeyVariables.KEY_JUMP.put(player.getUUID(), message.condition);
                    break;
                case "freeze_planet_menu":
                    if (player.containerMenu instanceof PlanetSelectionMenu menu) {
                        menu.switchFreezeGui();
                    }
                    break;
            }
        });
    }


}
