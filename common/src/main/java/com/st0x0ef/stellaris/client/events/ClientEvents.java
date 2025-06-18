package com.st0x0ef.stellaris.client.events;

import com.st0x0ef.stellaris.client.registries.KeyMappingsRegistry;
import com.st0x0ef.stellaris.client.screens.tablet.TabletMainScreen;
import com.st0x0ef.stellaris.common.items.CustomTabletEntry;
import com.st0x0ef.stellaris.common.keybinds.KeyVariables;
import com.st0x0ef.stellaris.common.network.packets.KeyHandlerPacket;
import com.st0x0ef.stellaris.common.network.packets.OpenTabletEntryPacket;
import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.networking.NetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class ClientEvents {

    private static ResourceLocation entryHovered = null;
    private static int timeClicked = 0;
    private static boolean isHolding = false;

    public static void registerEvents() {
        ClientTooltipEvent.ITEM.register((stack, lines, context, flag) -> {
            ResourceLocation entryId = getEntryId(stack);
            if (entryId != null && TabletMainScreen.INFOS.containsKey(entryId)) {
                entryHovered = entryId;

                lines.add(Component.translatable("tooltip.item.stellaris.open_tablet", KeyMappingsRegistry.OPEN_TABLET_INFO.getTranslatedKeyMessage().getString()));

                if (timeClicked > 0) {
                    lines.add(Component.literal("||".repeat(timeClicked)).withStyle(ChatFormatting.GRAY));
                }
                return;
            }

            entryHovered = null;
        });

        ClientTickEvent.CLIENT_POST.register(client -> {
            if (!isHolding) {
                if (timeClicked > 0) {
                    timeClicked--;
                }
                return;
            }
            isHolding = false;
        });

        ClientRawInputEvent.KEY_PRESSED.register(((client, keyCode, scanCode, action, modifiers) -> {
            Player player = client.player;
            if (player == null) {
                return EventResult.pass();
            }

            tryOpenTableEntry(keyCode);

            KeyVariables.getKey(client).forEach((key, name) -> {
                if (key.getDefaultKey().getValue() == keyCode && action == GLFW.GLFW_RELEASE) {
                    KeyVariables.setKeyVariable(name, client.player.getUUID(), false);
                    NetworkManager.sendToServer(new KeyHandlerPacket(name, false));
                }
                else if (key.getDefaultKey().getValue() == keyCode && action == GLFW.GLFW_PRESS) {
                    KeyVariables.setKeyVariable(name, client.player.getUUID(), true);
                    NetworkManager.sendToServer(new KeyHandlerPacket(name, true));
                }
            });
            return EventResult.pass();
        }));

        ClientGuiEvent.SET_SCREEN.register((screen) -> {
            if (screen == null) {
                timeClicked = 0;
                entryHovered = null;
                isHolding = false;
            }

            return CompoundEventResult.pass();
        });
    }

    private static void tryOpenTableEntry(int keyCode) {
        if (KeyMappingsRegistry.OPEN_TABLET_INFO.key.getValue() == keyCode) {
            isHolding = true;
            if (entryHovered != null) {
                timeClicked++;
                if (timeClicked == 30) {
                    NetworkManager.sendToServer(new OpenTabletEntryPacket(entryHovered));
                    timeClicked = 0;
                    entryHovered = null;
                }
            }
            return;
        }

        isHolding = false;
        if (entryHovered != null) {
            timeClicked--;
            return;
        }
        timeClicked = 0;
    }

    private static ResourceLocation getEntryId(ItemStack stack) {
        if (stack.getItem() instanceof CustomTabletEntry entry) {
            return entry.getEntryName(stack);
        }
        return ResourceLocation.fromNamespaceAndPath("items", stack.getItem().arch$registryName().getPath());
    }
}
