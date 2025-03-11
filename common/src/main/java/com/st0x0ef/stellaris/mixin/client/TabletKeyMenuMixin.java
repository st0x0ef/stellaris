package com.st0x0ef.stellaris.mixin.client;

import com.st0x0ef.stellaris.client.events.ClientEvents;
import com.st0x0ef.stellaris.client.registries.KeyMappingsRegistry;
import com.st0x0ef.stellaris.common.network.packets.OpenTabletEntryPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class TabletKeyMenuMixin {

    @Unique
    private boolean stellaris$isHolding = false;

    @Inject(at = @At("HEAD"), method = "tick")
    private void onTabletKeyPressed(CallbackInfo ci) {
        if (!stellaris$isHolding) {
            if (ClientEvents.timeClicked > 0) ClientEvents.timeClicked--;
        } else {
            stellaris$isHolding = false;
        }

    }

    @Inject(at = @At("HEAD"), method = "keyPressed")
    private void onTabletKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (KeyMappingsRegistry.OPEN_TABLET_INFO.key.getValue() == keyCode) {
            stellaris$isHolding = true;
            if(ClientEvents.entryHovered != null) {
                ClientEvents.timeClicked++;
                if(ClientEvents.timeClicked == 30) {
                    NetworkManager.sendToServer(new OpenTabletEntryPacket(ClientEvents.entryHovered));
                    ClientEvents.timeClicked = 0;
                    ClientEvents.entryHovered = null;
                }
            }
        } else {
            stellaris$isHolding = false;
        }
    }



    @Inject(at = @At("HEAD"), method = "onClose")
    private void onScreenClose(CallbackInfo ci) {
        ClientEvents.timeClicked = 0;
        ClientEvents.entryHovered = null;
        stellaris$isHolding = false;
    }

}
