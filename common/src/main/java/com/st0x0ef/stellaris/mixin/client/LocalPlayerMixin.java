package com.st0x0ef.stellaris.mixin.client;

import com.st0x0ef.stellaris.client.screens.FlagUploadScreen;
import com.st0x0ef.stellaris.injects.LocalPlayerScreenOpener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin implements LocalPlayerScreenOpener {
    @Shadow @Final protected Minecraft minecraft;

    @Override
    public void stellaris$OpenFlagScreen() {
        minecraft.setScreen(new FlagUploadScreen());
    }
}
