package com.st0x0ef.stellaris.common.items.module;

import com.fej1fun.potentials.capabilities.Capabilities;
import com.fej1fun.potentials.fluid.UniversalFluidStorage;
import com.st0x0ef.stellaris.client.screens.GUISprites;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class FuelModule extends Item implements SpaceSuitModule {

    public FuelModule(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public MutableComponent displayName() {
        return Component.translatable("spacesuit.stellaris.fuel_module");
    }

    @Override
    public void renderToGui(GuiGraphics graphics, DeltaTracker deltaTracker, Player player, ItemStack stack) {
        UniversalFluidStorage storage = Capabilities.Fluid.ITEM.getCapability(stack);
        if (storage == null) {
            return;
        }

        graphics.blit(GUISprites.SPACESUIT_FUEL_BAR, 5, 16, 0, 0, 37, 10, 37, 10);

        int i = Mth.ceil(Mth.clamp((float) storage.getFluidInTank(1).getAmount() / (float) storage.getTankCapacity(1),
                0.0F, 1.0F) * (24 - 1));
        graphics.blitSprite(GUISprites.SPACESUIT_FULL_BAR_SPRITE, 24, 4, 0, 0, 15, 19, i, 4);
    }

    @Override
    public void addToTooltips(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        UniversalFluidStorage storage = Capabilities.Fluid.ITEM.getCapability(stack);
        if (storage == null) {
            return;
        }
        tooltipComponents.add(Component.translatable("tooltip.item.stellaris.diesel", storage.getFluidInTank(1).getAmount()).append(" §r/§8 " + storage.getTankCapacity(1)));
    }

}
