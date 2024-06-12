package com.st0x0ef.stellaris.neoforge.client.renderer;

import com.mojang.blaze3d.shaders.FogShape;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import com.st0x0ef.stellaris.Stellaris;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ViewportEvent;

import java.util.Objects;

@Mod(Stellaris.MODID)
public class MarsFog {
    @SubscribeEvent
    public static void setupFog(ViewportEvent.RenderFog event) {
        Minecraft mc = Minecraft.getInstance();
        Stellaris.LOG.error(PlanetUtil.getPlanet(mc.level.dimension()).name());

        if (Objects.equals(PlanetUtil.getPlanet(mc.level.dimension()).name(), "Mars")) {
            float fogLevel = mc.level.getRainLevel((float) event.getPartialTick()) - 0.02F;
            float renderDistance = mc.gameRenderer.getRenderDistance();

            if (fogLevel > 0.0F) {
                float farPlaneDistance = Math.max(renderDistance - (fogLevel * renderDistance), 10);

                event.setNearPlaneDistance(event.getMode() == FogRenderer.FogMode.FOG_SKY ? 0.0F : farPlaneDistance * 0.9F);
                event.setFarPlaneDistance(farPlaneDistance + 0.8F);
                event.setFogShape(FogShape.CYLINDER);
                event.setCanceled(true);
            }
        }
    }
}