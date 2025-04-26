package com.st0x0ef.stellaris.client.overlays;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.vehicles.LanderEntity;
import com.st0x0ef.stellaris.common.entities.vehicles.RocketEntity;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

public class RocketBarOverlay {

    public static final ResourceLocation ROCKET = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/planet_bar/rocket.png");

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;

        if (player != null && (player.getVehicle() instanceof RocketEntity || player.getVehicle() instanceof LanderEntity)) {
            double min = player.level().getMinY();

            for (int i = player.level().getMinY(); i < player.level().getMaxY(); i++) {
                if (player.level().getBlockState(new BlockPos(player.getBlockX(), i, player.getBlockZ())).getBlock() != Blocks.AIR) {
                    min = i;
                }
            }

            double yHeight = ((player.getY() - min) / (600 - min)) * 113;

            ResourceLocation planet = PlanetUtil.getPlanetBar(player.level().dimension().location());

            /** ROCKET BAR IMAGE */
            graphics.blit(RenderType::guiTextured, planet, 0, (graphics.guiHeight() / 2) - 128 / 2, 0, 0, 16, 128, 16, 128);

            /** ROCKET_Y IMAGE */
            graphics.blit(RenderType::guiTextured, ROCKET, 4, (int) (((double) graphics.guiHeight() / 2) + (103f / 2f) - yHeight), 0, 0, 8, 11, 8, 11);
        }
    }
}
