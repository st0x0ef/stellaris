package com.st0x0ef.stellaris.client.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.helper.ScreenHelper;
import com.st0x0ef.stellaris.common.entities.vehicles.RocketEntity;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RocketBarOverlay {

    public static final ResourceLocation ROCKET = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/planet_bar/rocket.png");

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;

        if (player.getVehicle() instanceof RocketEntity
                //|| player.getVehicle() instanceof LanderEntity
                ) {
            Level level = Minecraft.getInstance().level;

            float yHeight = (float) player.getY() / 5.3F;

            if (yHeight < 0) {
                yHeight = 0;
            } else if (yHeight > 113) {
                yHeight = 113;
            }

            ResourceLocation planet = PlanetUtil.getPlanetBar(level.dimension().location());

            /** ROCKET BAR IMAGE */
            RenderSystem.setShaderTexture(0, planet);
            graphics.blit(planet, 0, (graphics.guiHeight() / 2) - 128 / 2, 0, 0, 16, 128, 16, 128);

            /** ROCKET_Y IMAGE */
            RenderSystem.setShaderTexture(0, ROCKET);
            ScreenHelper.renderWithFloat.blit(graphics.pose(), 4, ((float) graphics.guiHeight() / 2) + ((float) 103 / 2) - yHeight, 0, 0, 8, 11, 8, 11);
        }

    }
}
