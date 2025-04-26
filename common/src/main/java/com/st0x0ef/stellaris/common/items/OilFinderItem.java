package com.st0x0ef.stellaris.common.items;

import com.fej1fun.potentials.energy.ItemEnergyStorage;
import com.fej1fun.potentials.providers.EnergyProvider;
import com.st0x0ef.stellaris.common.oil.OilUtils;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class OilFinderItem extends Item implements EnergyProvider.ITEM {

    public OilFinderItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
//        UniversalEnergyStorage energy = getEnergy(player.getItemInHand(usedHand));
//        if (energy.getEnergy() < 1)
//            return InteractionResultHolder.fail(player.getItemInHand(usedHand));

        if (level instanceof ServerLevel serverLevel)  {
            int oilLevel = OilUtils.getOilLevel(serverLevel, player.chunkPosition());

            MutableComponent component = Component.literal("Found Oil " + oilLevel + "mb");
            if (oilLevel == 0) component = Component.literal("No oil found");
            component.withColor(OilUtils.getOilLevelColor(oilLevel));

            player.getItemInHand(usedHand).hurtAndBreak(2, player, EquipmentSlot.MAINHAND);
            //energy.extract(1, false);

            player.displayClientMessage(component, true);

            return super.use(level, player, usedHand);
        }

        return InteractionResult.FAIL;
    }

    @Override
    public @NotNull ItemEnergyStorage getEnergy(@NotNull ItemStack stack) {
        return new ItemEnergyStorage(stack, DataComponentsRegistry.ENERGY.get(), 1000, 20, 1);
    }
}