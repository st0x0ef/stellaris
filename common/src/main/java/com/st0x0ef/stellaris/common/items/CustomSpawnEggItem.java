package com.st0x0ef.stellaris.common.items;

import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CustomSpawnEggItem extends ArchitecturySpawnEggItem implements CustomTabletEntry {

    public CustomSpawnEggItem(RegistrySupplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Properties properties) {
        super(entityType, backgroundColor, highlightColor, properties);
    }

    @Override
    public ResourceLocation getEntryName(ItemStack stack) {
        return ResourceLocation.fromNamespaceAndPath("mobs", getType(stack).arch$registryName().getPath());
    }
}
