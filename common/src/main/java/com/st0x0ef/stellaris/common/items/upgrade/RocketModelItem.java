package com.st0x0ef.stellaris.common.items.upgrade;

import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.RocketModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class RocketModelItem extends UpgradeItem {

    final private RocketModel model;

    public RocketModelItem(RocketModel model, Properties properties) {
        super(properties);
        this.model = model;
    }

    public RocketModel getModel() {
        return model;
    }


    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.literal("Model : " + this.getModel().toString()));
    }


}
