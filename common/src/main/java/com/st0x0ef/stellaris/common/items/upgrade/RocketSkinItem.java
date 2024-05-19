package com.st0x0ef.stellaris.common.items.upgrade;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class RocketSkinItem extends UpgradeItem {

    final private ResourceLocation rocketSkinName;

    public RocketSkinItem(ResourceLocation rocketSkinName, Properties properties) {
        super(properties);
        this.rocketSkinName = rocketSkinName;
    }

    public ResourceLocation getRocketSkinName() {
        return rocketSkinName;
    }

    public String getNameSpace()  {
        return rocketSkinName.getNamespace();
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.literal("Skin : " + this.getRocketSkinName()));
    }


}
