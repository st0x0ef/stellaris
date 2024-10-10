package com.st0x0ef.stellaris.client.overlays;

import com.st0x0ef.stellaris.client.screens.GUISprites;
import com.st0x0ef.stellaris.common.items.armors.SpaceSuit;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

@Environment(EnvType.CLIENT)
public class SpaceSuitOverlay {


    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;

        if (player!=null) {
            if (Utils.isLivingInSpaceSuit(player)) {
                ItemStack itemStack = player.getItemBySlot(EquipmentSlot.CHEST);

                //we can add default gui stuff for the space suit here
                graphics.blit(GUISprites.SPACESUIT_OXYGEN_BAR, 5, 5, 0, 0, 37, 10, 37, 10);
                //graphics.blitSprite(SPRITE, getWidth(), getHeight(), 0, getHeight() - i, getX(), getY() + getHeight() - i, getWidth(), i);

                //first layer is 1 since what we add is 0
                if (itemStack.getItem() instanceof SpaceSuit spaceSuit) {
                    AtomicInteger y = new AtomicInteger(37);
                    spaceSuit.getModules(itemStack).forEach(module -> {
                        y.set(module.renderStackedGui(graphics, deltaTracker, player, itemStack, y.get())+1);
                        module.renderToGui(graphics, deltaTracker, player, itemStack);
                    });
                    y.set(37);


                }
            }
        }

    }
}
