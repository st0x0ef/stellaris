package com.st0x0ef.stellaris.common.items.module;

import com.st0x0ef.stellaris.common.module.Module;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

public class OilFinderModule extends SpaceSuitModuleItem {

    int oilLevel = 0;

    public OilFinderModule(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(ItemStack stack, Level level, Player player) {
        super.tick(stack, level, player);
        if (!level.isClientSide) {
            this.oilLevel = level.getChunk(player.blockPosition()).stellaris$getChunkOilLevel();
        }
    }

    @Override
    public int renderStackedGui(GuiGraphics graphics, DeltaTracker deltaTracker, Player player, ItemStack stack, int y) {
        graphics.drawString(Minecraft.getInstance().font, Component.literal("Oil Level: " + oilLevel), 5, y, 0xFFFFFF);
        y += Minecraft.getInstance().font.lineHeight;
        return y;
    }

    @Override
    public MutableComponent displayName() {
        return Component.translatable("spacesuit.stellaris.oil_finder");
    }

    @Override
    public <T extends Item & Module> Supplier<T> getAsItem() {
        return (Supplier<T>) ItemsRegistry.MODULE_OIL_FINDER;
    }

    @Override
    public void addToTooltips(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("spacesuit.stellaris.oil_finder"));
    }

}
