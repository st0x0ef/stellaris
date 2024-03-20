package com.st0x0ef.stellaris.common.entities.alien;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

/*TODO actually implement this*/
public class AlienTrade implements ItemListing {
    @Nullable
    @Override
    public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
        return null;
    }
}