package com.st0x0ef.stellaris.client.overlays;

import com.st0x0ef.stellaris.common.effects.SandStormEffect;
import com.st0x0ef.stellaris.common.registry.EffectsRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;

public class EffectOverlays {
    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player.hasEffect(EffectsRegistry.SANDSTORM)) {
            guiGraphics.blit(RenderType::guiTextured, SandStormEffect.SANDSTORM_OVERLAY, 0, 0, -90, 0.0F, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
        }
    }
}
