package com.st0x0ef.stellaris.client.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;


public class OxygenOverlay {
    public static ResourceLocation OXYGEN_OVERLAY = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "oxygen_overlay");
    public static ServerLevel serverLevel;
    public static LivingEntity livingEntity;
    public static BlockPos blockPos;
           public static void render(GuiGraphics graphic, DeltaTracker deltaTracker) {
               if (livingEntity != null && !livingEntity.level().isClientSide()) {
                   if (!GlobalOxygenManager.getInstance().getOrCreateDimensionManager((ServerLevel) livingEntity.level()).breath(livingEntity)) {
                       if (!PlanetUtil.hasOxygenAt(serverLevel, blockPos) && !Utils.isLivingInJetSuit(livingEntity) && !Utils.isLivingInSpaceSuit(livingEntity)) {


                           RenderSystem.setShader(GameRenderer::getPositionTexShader);
                           RenderSystem.disableDepthTest();
                           RenderSystem.depthMask(false);
                           RenderSystem.enableBlend();
                           graphic.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                           graphic.blit(OXYGEN_OVERLAY, 0, 0, -90, 0.0F, 0.0F, graphic.guiWidth(), graphic.guiHeight(), graphic.guiWidth(), graphic.guiHeight());
                           RenderSystem.disableBlend();
                           graphic.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                           RenderSystem.depthMask(true);
                           RenderSystem.enableDepthTest();
                       }
                   }
                   /**
                    * will be rewritten to improve smooth texture render
                    */
               }
           }


}




