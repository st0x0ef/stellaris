package com.st0x0ef.stellaris.common.items;

import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TabletItem extends Item {

    public TabletItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        PlanetUtil.openTabletMenu(player, ResourceLocation.parse("null:null"));
        return super.use(level, player, usedHand);
    }

}
