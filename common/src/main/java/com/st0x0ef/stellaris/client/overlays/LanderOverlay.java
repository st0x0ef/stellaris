package com.st0x0ef.stellaris.client.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.vehicles.LanderEntity;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import static com.st0x0ef.stellaris.Stellaris.texture;

public class LanderOverlay {

    public static final ResourceLocation WARNING = texture("overlay/warning");

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Entity vehicle = player.getVehicle();

        if (vehicle instanceof LanderEntity lander && !lander.isInWall() && !lander.isInWater() && !lander.isInLava() && !lander.isLanded()) {
            /** FLASHING */
            float sin = (float) Math.sin((mc.level.getDayTime() + deltaTracker.getGameTimeDeltaPartialTick(true)) / 6.0f);
            float flash = Mth.clamp(sin, 0.0f, 4.0f);

            RenderSystem.setShaderColor(flash, flash, flash, flash);

            /** WARNING IMAGE */
            graphics.blit(RenderType::guiTextured, WARNING, graphics.guiWidth() / 2 - 58, 50, 0, 0, 116, 21, 116, 21);

            /** SPEED TEXT */
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            double speed = Math.round(100.0 * lander.getDeltaMovement().y()) / 100.0;

            Component message = Component.translatable("message." + Stellaris.MODID + ".speed", speed);
            graphics.drawString(Minecraft.getInstance().font, message, graphics.guiWidth() / 2 - 29, 80, -3407872);
        }
    }
}
