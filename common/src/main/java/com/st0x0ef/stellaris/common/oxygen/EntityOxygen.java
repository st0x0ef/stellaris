package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.common.armors.JetSuit;
import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenContainerBlockEntity;
import com.st0x0ef.stellaris.common.registry.DamageSourceRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class EntityOxygen {

    public static void tick(Entity entity) {
        if (PlanetUtil.hasOxygen(entity.level().dimension())) return;

        if (entity instanceof LivingEntity livingEntity && Utils.isLivingInJetSuit(livingEntity)) {
            JetSuit.Suit suit = (JetSuit.Suit) livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem();
            if (suit.oxygenContainer.removeOxygenStored(false)) return;
        }

        boolean canBreathThisTick = false;

        for (int x = -16; x < 16; x++) {
            for (int z = -16; z < 16; z++) {
                for (int y = -16; y < 16; y++) {
                    if (entity.level().getBlockEntity(new BlockPos(x + entity.getBlockX(), y + entity.getBlockY(), z + entity.getBlockZ())) instanceof OxygenContainerBlockEntity source) {
                        canBreathThisTick = source.getOxygenContainer().removeOxygenAt(entity.getOnPos(), false);
                    }
                }
            }
        }

        if (!canBreathThisTick) {
            entity.hurt(DamageSourceRegistry.of(entity.level(), DamageSourceRegistry.OXYGEN), 0.5f);
        }
    }
}
