package com.st0x0ef.stellaris.mixin.client;

import com.st0x0ef.stellaris.common.entities.LanderEntity;
import com.st0x0ef.stellaris.common.keybinds.KeyVariables;
import com.st0x0ef.stellaris.common.network.packets.KeyHandlerPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(KeyboardHandler.class)
public abstract class PlayerLanderKeyMixin {

    @Final
    @Shadow
    private Minecraft minecraft;



    @Inject(at = @At(value = "TAIL"), method = "keyPress", cancellable = true)
    private void keyPress(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo info) {
        if (windowPointer == this.minecraft.getWindow().getWindow()) {
            if (minecraft.player == null) return;

            sendKeyToServerAndClientHashMap(key, action, minecraft.player, this.minecraft.options.keyUp, KeyVariables.KEY_UP, "key_up", KeyVariables.isHoldingUp(minecraft.player));
            sendKeyToServerAndClientHashMap(key, action, minecraft.player, this.minecraft.options.keyDown, KeyVariables.KEY_DOWN, "key_down", KeyVariables.isHoldingDown(minecraft.player));
            sendKeyToServerAndClientHashMap(key, action, minecraft.player, this.minecraft.options.keyRight, KeyVariables.KEY_RIGHT, "key_right", KeyVariables.isHoldingRight(minecraft.player));
            sendKeyToServerAndClientHashMap(key, action, minecraft.player, this.minecraft.options.keyLeft, KeyVariables.KEY_LEFT, "key_left", KeyVariables.isHoldingLeft(minecraft.player));


            if (minecraft.player.getVehicle() instanceof LanderEntity) {
                keyEvent(minecraft.player, minecraft.options.keyJump, key, scanCode, action, modifiers);
            }
        }
    }

    public void keyEvent(Player player, KeyMapping keyWanted, int key, int scanCode, int action, int modifiers) {
        if ((keyWanted.getDefaultKey().getValue() == key && action == GLFW.GLFW_RELEASE && KeyVariables.isHoldingJump(player))  || (KeyVariables.isHoldingJump(player))) {
            KeyVariables.KEY_JUMP.put(player.getUUID(), false);

            NetworkManager.sendToServer(new KeyHandlerPacket(
                    "key_jump", false
            ));

        } else if (keyWanted.getDefaultKey().getValue() == key && action == GLFW.GLFW_PRESS && !KeyVariables.isHoldingJump(player)) {
            KeyVariables.KEY_JUMP.put(player.getUUID(), true);

            NetworkManager.sendToServer(new KeyHandlerPacket(
                    "key_jump", true
            ));
        }
    }


    private static void sendKeyToServerAndClientHashMap(int key, int action, Player player, KeyMapping keyWanted, Map<UUID, Boolean> variableKey, String keyString, boolean isPressed) {
        if (player == null) {
            return;
        }

        if ((keyWanted.getDefaultKey().getValue() == key && action == GLFW.GLFW_RELEASE && isPressed) || isPressed) {
            variableKey.put(player.getUUID(), false);

            NetworkManager.sendToServer(new KeyHandlerPacket(
                    keyString, false
            ));
        }

        if (keyWanted.getDefaultKey().getValue() == key && action == GLFW.GLFW_PRESS && !isPressed) {
            variableKey.put(player.getUUID(), true);

            NetworkManager.sendToServer(new KeyHandlerPacket(
                    keyString, true
            ));
        }
    }

}
