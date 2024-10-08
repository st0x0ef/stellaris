package com.st0x0ef.stellaris.common.items.armors;

import com.st0x0ef.stellaris.common.data_components.SpaceSuitModules;
import com.st0x0ef.stellaris.common.items.module.SpaceSuitModule;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class SpaceSuit extends AbstractSpaceArmor.AbstractSpaceChestplate {

    public SpaceSuit(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        super.onArmorTick(stack, level, player);
        List<SpaceSuitModule> modules = stack.getOrDefault(DataComponentsRegistry.SPACE_SUIT_MODULES.get(), new SpaceSuitModules(List.of())).getModules();
        if (!modules.isEmpty()) {
            modules.forEach(spaceSuitModule -> spaceSuitModule.tick(stack, level, player));
        }
    }

    public NonNullList<ItemStack> scrapArmorModules(ItemStack stack) {
        Iterable<ItemStack> items = stack.getOrDefault(DataComponentsRegistry.SPACE_SUIT_MODULES.get(), new SpaceSuitModules(List.of())).itemsCopy();
        NonNullList<ItemStack> itemsToReturn = NonNullList.create();
        items.forEach(itemsToReturn::add);
        stack.set(DataComponentsRegistry.SPACE_SUIT_MODULES.get(), new SpaceSuitModules(List.of()));
        return itemsToReturn;
    }
}
