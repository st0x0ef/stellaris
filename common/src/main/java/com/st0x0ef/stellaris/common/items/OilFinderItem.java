package com.st0x0ef.stellaris.common.items;

import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

public class OilFinderItem extends Item {
    public OilFinderItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

        int oilLevel = level.getChunk(pos).stellaris$getChunkOilLevel();

        MutableComponent component = Component.literal("Found Oil " + level.getChunk(pos).stellaris$getChunkOilLevel());
        if(oilLevel > 5000) {
            component.withColor(Utils.getColorHexCode("green"));
        } else if(oilLevel <= 5000 && oilLevel > 2500) {
            component.withColor(Utils.getColorHexCode("orange"));
        } else if(oilLevel <= 2500 && oilLevel > 0) {
            component.withColor(Utils.getColorHexCode("red"));
        } else {
            component = Component.literal("No oil found");
        }

        context.getItemInHand().setDamageValue(context.getItemInHand().getDamageValue() - 1);
        player.displayClientMessage(component, true);

        return super.useOn(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.item.stellaris.oil_finder").withStyle(ChatFormatting.GRAY));
    }


}