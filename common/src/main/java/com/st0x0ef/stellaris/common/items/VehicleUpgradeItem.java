package com.st0x0ef.stellaris.common.items;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import com.st0x0ef.stellaris.common.vehicle_upgrade.SkinUpgrade;
import com.st0x0ef.stellaris.common.vehicle_upgrade.VehicleUpgrade;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class VehicleUpgradeItem extends Item implements CustomTabletEntry{

    private final VehicleUpgrade upgrade;

    public VehicleUpgradeItem(Properties properties, VehicleUpgrade upgrade) {
        super(properties);
        this.upgrade = upgrade;
    }

    public VehicleUpgrade getUpgrade() {
        return upgrade;
    }

    @Override
    public ResourceLocation getEntryName(ItemStack stack) {
        if(stack.getItem() instanceof VehicleUpgradeItem item && item.upgrade instanceof SkinUpgrade) {
            return ResourceLocation.parse("rocket:skins");
        }
        return null;
    }
}

