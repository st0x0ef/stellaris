package com.st0x0ef.stellaris.common.items;

import com.fej1fun.potentials.energy.ItemEnergyStorage;
import com.fej1fun.potentials.energy.UniversalEnergyStorage;
import com.fej1fun.potentials.providers.EnergyProvider;
import com.st0x0ef.stellaris.common.oil.OilUtils;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OilFinderItem extends Item implements EnergyProvider.ITEM {

    public OilFinderItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level.isClientSide())
            return InteractionResultHolder.fail(player.getItemInHand(usedHand));

        UniversalEnergyStorage energy = getEnergy(player.getItemInHand(usedHand));
        if (energy.getEnergy() < 1)
            return InteractionResultHolder.fail(player.getItemInHand(usedHand));

        int oilLevel = level.getChunk(player.getOnPos()).stellaris$getChunkOilLevel();

        MutableComponent component = Component.literal("Found Oil " + level.getChunk(player.getOnPos()).stellaris$getChunkOilLevel() + "mb");
        if (oilLevel == 0) component = Component.literal("No oil found");
        component.withColor(OilUtils.getOilLevelColor(oilLevel));

        //player.getItemInHand(usedHand).hurtAndBreak(2, player, EquipmentSlot.MAINHAND);
        energy.extract(1, false);

        player.displayClientMessage(component, true);

        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.item.stellaris.oil_finder").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public @NotNull ItemEnergyStorage getEnergy(@NotNull ItemStack stack) {
        return new ItemEnergyStorage(stack, DataComponentsRegistry.ENERGY.get(), 1000, 20, 1);
    }
}