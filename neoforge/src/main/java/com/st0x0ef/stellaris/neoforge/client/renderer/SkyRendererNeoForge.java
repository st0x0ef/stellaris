package com.st0x0ef.stellaris.neoforge.client.renderer;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.Objects;

@Mod(Stellaris.MODID)
public class SkyRendererNeoForge {
    @SubscribeEvent
    public static void RenderWorldSky(RenderLevelStageEvent e) {
        if (e.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;

        Minecraft mc = Minecraft.getInstance();
        Player p = mc.player;
        ResourceKey<Level> dim = Objects.requireNonNull(p).level().dimension();

        SkyRenderer.render(dim, e.getPartialTick(), e.getProjectionMatrix(), e.getPoseStack(), e.getModelViewMatrix());
    }
}
