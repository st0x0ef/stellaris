package com.st0x0ef.stellaris.client.overlays;

import com.st0x0ef.stellaris.Stellaris;
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

                if (itemStack.getItem() instanceof SpaceSuit spaceSuit) {
                    AtomicInteger layer = new AtomicInteger();
                    spaceSuit.getModules(itemStack).forEach(module -> {
                        module.renderToGui(graphics, deltaTracker, player, itemStack, layer.get());
                        layer.getAndIncrement();
                    });
                    layer.set(0);
                }
            }
        }

    }
}
