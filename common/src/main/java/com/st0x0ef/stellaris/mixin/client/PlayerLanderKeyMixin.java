package com.st0x0ef.stellaris.mixin.client;

import com.st0x0ef.stellaris.common.keybinds.KeyVariables;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.network.packets.KeyHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class PlayerLanderKeyMixin {

    @Final
    @Shadow
    private Minecraft minecraft;



    @Inject(at = @At(value = "TAIL"), method = "keyPress", cancellable = true)
    private void keyPress(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo info) {
        if (windowPointer == this.minecraft.getWindow().getWindow()) {
            keyEvent(minecraft.player, minecraft.options.keyJump, key, scanCode, action, modifiers);
        }
    }

    public void keyEvent(Player player, KeyMapping keyWanted, int key, int scanCode, int action, int modifiers) {
        if ((keyWanted.getDefaultKey().getValue() == key && action == GLFW.GLFW_RELEASE && KeyVariables.isHoldingJump(player))  || (KeyVariables.isHoldingJump(player))) {
            KeyVariables.KEY_JUMP.put(player.getUUID(), false);
            RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), player.registryAccess());
            NetworkRegistry.sendToServer(NetworkRegistry.KEY_HANDLER_ID, KeyHandler.encode(new KeyHandler("key_jump", false), buffer));
        } else if (keyWanted.getDefaultKey().getValue() == key && action == GLFW.GLFW_PRESS && !KeyVariables.isHoldingJump(player)) {
            KeyVariables.KEY_JUMP.put(player.getUUID(), true);
            RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), player.registryAccess());
            NetworkRegistry.sendToServer(NetworkRegistry.KEY_HANDLER_ID, KeyHandler.encode(new KeyHandler("key_jump", true), buffer));
        }
    }
}
