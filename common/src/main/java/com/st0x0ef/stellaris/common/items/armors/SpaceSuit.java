package com.st0x0ef.stellaris.common.items.armors;

import com.fej1fun.potentials.fluid.ItemFluidStorage;
import com.fej1fun.potentials.fluid.UniversalFluidItemStorage;
import com.st0x0ef.stellaris.common.data_components.SpaceSuitModules;
import com.st0x0ef.stellaris.common.items.module.SpaceSuitModule;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import dev.architectury.fluid.FluidStack;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpaceSuit extends AbstractSpaceArmor.AbstractSpaceChestplate {

    public SpaceSuit(Holder<ArmorMaterial> material, Type type, Properties properties) {
        // The enchantable flag is set to true to allow enchantments on the Space Suit.
        super(material, type, properties, true);
    }


    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (entity instanceof Player player && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof SpaceSuit) {
            ItemStack spaceSuitItemStack = player.getItemBySlot(EquipmentSlot.CHEST);

            if (stack != spaceSuitItemStack) return;

            List<SpaceSuitModule> modules = getModules(stack);
            if (!modules.isEmpty()) {
                modules.forEach(spaceSuitModule -> spaceSuitModule.tick(spaceSuitItemStack, level, player));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        List<SpaceSuitModule> modules = getModules(stack);

        if (Platform.getEnv() != EnvType.CLIENT) {
            return;
        }

        if (!modules.isEmpty()) {
            modules.forEach(spaceSuitModule -> spaceSuitModule.addToTooltips(stack, context, tooltipComponents, tooltipFlag));
        }

        if (Screen.hasShiftDown()) {
            if (!modules.isEmpty()) {
                tooltipComponents.add(Component.translatable("spacesuit.stellaris.modules"));
                modules.forEach(spaceSuitModule -> tooltipComponents.add(spaceSuitModule.displayName().withStyle(ChatFormatting.GRAY)));
            }
        }
        else {
            tooltipComponents.add(Component.translatable("spacesuit.stellaris.shift_for_modules"));
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


    @Override
    public @NotNull UniversalFluidItemStorage getFluidTank(@NotNull ItemStack itemStack) {
        return new ItemFluidStorage(DataComponentsRegistry.FLUID_LIST.get(), itemStack, 2, 3000) {
            @Override
            public boolean isFluidValid(int tank, FluidStack stack) {
                return switch (tank) {
                    case 0 -> stack.getFluid().isSame(FluidRegistry.OXYGEN_STILL.get());
                    case 1 -> stack.getFluid().isSame(FluidRegistry.DIESEL_STILL.get()) &&
                            SpaceSuitModules.containsInModules(itemStack, (SpaceSuitModule) ItemsRegistry.MODULE_FUEL.get());
                    default -> false;
                };
            }
        };
    }
}
