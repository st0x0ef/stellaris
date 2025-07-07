package com.st0x0ef.stellaris.client.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.common.entities.vehicles.RocketEntity;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.function.Consumer;

public class RocketStartOverlay {

    public static final ResourceLocation TIMER_1 = ResourceLocationUtils.texture("overlay/timer/timer_1");
    public static final ResourceLocation TIMER_2 = ResourceLocationUtils.texture("overlay/timer/timer_2");
    public static final ResourceLocation TIMER_3 = ResourceLocationUtils.texture("overlay/timer/timer_3");
    public static final ResourceLocation TIMER_4 = ResourceLocationUtils.texture("overlay/timer/timer_4");
    public static final ResourceLocation TIMER_5 = ResourceLocationUtils.texture("overlay/timer/timer_5");
    public static final ResourceLocation TIMER_6 = ResourceLocationUtils.texture("overlay/timer/timer_6");
    public static final ResourceLocation TIMER_7 = ResourceLocationUtils.texture("overlay/timer/timer_7");
    public static final ResourceLocation TIMER_8 = ResourceLocationUtils.texture("overlay/timer/timer_8");
    public static final ResourceLocation TIMER_9 = ResourceLocationUtils.texture("overlay/timer/timer_9");
    public static final ResourceLocation TIMER_10 = ResourceLocationUtils.texture("overlay/timer/timer_10");


    private static final ResourceLocation[] TIMER_TEXTURES = {
            TIMER_1, TIMER_2, TIMER_3, TIMER_4, TIMER_5,
            TIMER_6, TIMER_7, TIMER_8, TIMER_9, TIMER_10
    };


    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player.getVehicle() instanceof RocketEntity) {
            Entity vehicle = Minecraft.getInstance().player.getVehicle();
            int timer = 0;

            /** GET TIMER */
            if (vehicle instanceof RocketEntity rocket) {
                timer = rocket.START_TIMER;

                /** CHECK IF ROCKET IS STARTED */
                if (!rocket.getEntityData().get(RocketEntity.ROCKET_START)) {
                    return;
                }

                int timerWidth = graphics.guiWidth() / 2 - 31;
                int timerHeight = graphics.guiHeight() / 2 / 2;


                /** TIMER */
                renderTimer(timer, timerWidth, timerHeight, texture -> {
                    RenderSystem.setShaderTexture(0, texture);
                    graphics.blit(texture, timerWidth, timerHeight, 0, 0, 60, 38, 60, 38);
                });
            }
        }
    }

    private static void renderTimer(int timer, int timerWidth, int timerHeight, Consumer<ResourceLocation> render) {
        Optional.of(timer)
                .filter(t -> t >= 0 && t < 200)
                .map(t -> 9 - (t / 20))
                .filter(idx -> idx >= 0 && idx < TIMER_TEXTURES.length)
                .map(idx -> TIMER_TEXTURES[idx])
                .ifPresent(render::accept);
    }
}