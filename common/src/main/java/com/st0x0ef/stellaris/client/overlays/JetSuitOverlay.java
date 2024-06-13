package com.st0x0ef.stellaris.client.overlays;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.armors.JetSuit;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class JetSuitOverlay {

    public static void render(GuiGraphics graphics, float partialTick) {
        Player player = Minecraft.getInstance().player;

        if (Utils.isLivingInJetSuit(player)) {
            Minecraft mc = Minecraft.getInstance();
            ItemStack itemStack = player.getItemBySlot(EquipmentSlot.CHEST);

            int x = 5;
            int y = 5;

            if (itemStack.getItem() instanceof JetSuit.Suit jetSuitItem) {
                Component modeText = jetSuitItem.getModeType(itemStack).getComponent();
                ChatFormatting chatFormatting = jetSuitItem.getModeType(itemStack).getChatFormatting();

                /** TEXT */
                Font font = mc.font;
                Component text = Component.translatable("general." + Stellaris.MODID + ".jet_suit_mode").append(": ").withStyle(chatFormatting).append(modeText.copy().withStyle(ChatFormatting.GRAY));
                graphics.drawString(font, text, (x + (80 - font.width(text)) / 2), y + 80 + 3, 0xFFFFFF);
            }
        }

    }
}
