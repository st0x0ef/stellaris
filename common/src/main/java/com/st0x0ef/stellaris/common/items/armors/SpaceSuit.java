package com.st0x0ef.stellaris.common.items.armors;

import com.st0x0ef.stellaris.common.data_components.SpaceSuitModules;
import com.st0x0ef.stellaris.common.items.module.SpaceSuitModule;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class SpaceSuit extends AbstractSpaceArmor.AbstractSpaceChestplate {

    public SpaceSuit(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel serverLevel, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
        super.inventoryTick(itemStack, serverLevel, entity, equipmentSlot);

        if (entity instanceof Player player && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof SpaceSuit) {
            ItemStack spaceSuitItemStack = player.getItemBySlot(EquipmentSlot.CHEST);
            List<SpaceSuitModule> modules = getModules(itemStack);
            if (!modules.isEmpty()) {
                modules.forEach(spaceSuitModule -> spaceSuitModule.tick(spaceSuitItemStack, serverLevel, player));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
        List<SpaceSuitModule> modules = getModules(itemStack);

        if (Platform.getEnv() != EnvType.CLIENT) return;

        if (!modules.isEmpty()) {
            modules.forEach(spaceSuitModule -> spaceSuitModule.addToTooltips(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag));
        }

        if(Screen.hasShiftDown()) {
            if (!modules.isEmpty()) {
                consumer.accept(Component.translatable("spacesuit.stellaris.modules"));
                modules.forEach(spaceSuitModule -> consumer.accept(spaceSuitModule.displayName().withStyle(ChatFormatting.GRAY)));
            }
        } else {
            consumer.accept(Component.translatable("spacesuit.stellaris.shift_for_modules"));
        }
    }

    public NonNullList<ItemStack> scrapArmorModules(ItemStack stack) {
        Iterable<ItemStack> items = stack.getOrDefault(DataComponentsRegistry.SPACE_SUIT_MODULES.get(), new SpaceSuitModules(List.of())).itemsCopy();
        NonNullList<ItemStack> itemsToReturn = NonNullList.create();
        items.forEach(itemsToReturn::add);
        stack.set(DataComponentsRegistry.SPACE_SUIT_MODULES.get(), SpaceSuitModules.empty());
        return itemsToReturn;
    }

    public List<SpaceSuitModule> getModules(ItemStack stack) {
        return stack.getOrDefault(DataComponentsRegistry.SPACE_SUIT_MODULES.get(), SpaceSuitModules.empty()).getModules();
    }
}
