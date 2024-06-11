package com.st0x0ef.stellaris.client.registries;

import com.mojang.blaze3d.platform.InputConstants;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.RocketEntity;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.network.packets.KeyHandler;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import static io.netty.buffer.Unpooled.*;

public class KeyMappingsRegistry {
    public static void register() {
        KeyMapping ROCKET_START = new KeyMapping("key." + Stellaris.MODID + ".rocket_start", InputConstants.Type.KEYSYM, InputConstants.KEY_SPACE, "category." + Stellaris.MODID + ".default");
        KeyMapping FREEZE_PLANET_MENU = new KeyMapping("key." + Stellaris.MODID + ".freeze_planet_menu", InputConstants.Type.KEYSYM, InputConstants.KEY_X, "category." + Stellaris.MODID + ".default");

        KeyMappingRegistry.register(ROCKET_START);
        KeyMappingRegistry.register(FREEZE_PLANET_MENU);

        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            while (ROCKET_START.consumeClick()) {
                RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(buffer(), minecraft.level.registryAccess());
                NetworkRegistry.sendToServer(NetworkRegistry.KEY_HANDLER_ID, KeyHandler.encode(new KeyHandler("rocket_start", true), buffer));
            }

            while (FREEZE_PLANET_MENU.consumeClick()) {
                RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(buffer(), minecraft.level.registryAccess());
                NetworkRegistry.sendToServer(NetworkRegistry.KEY_HANDLER_ID, KeyHandler.encode(new KeyHandler("freeze_planet_menu", true), buffer));
            }
        });
    }
}
