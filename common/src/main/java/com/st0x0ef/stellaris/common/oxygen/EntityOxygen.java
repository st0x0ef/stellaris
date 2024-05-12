package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.common.blocks.entities.oxygen.OxygenSourceBlockEntity;
import com.st0x0ef.stellaris.common.registry.DamageSourceRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

public class EntityOxygen {

    public static void tick(Entity entity) {
        boolean canBreathThisTick = false;

        if (PlanetUtil.hasOxygen(entity.level().dimension())) { return; }

        for (int x = -16; x < 16; x++) {
            for (int z = -16; z < 16; z++) {
                for (int y = -16; y < 16; y++) {
                    if (entity.level().getBlockEntity(new BlockPos(x + entity.getBlockX(), y + entity.getBlockY(), z + entity.getBlockZ())) instanceof OxygenSourceBlockEntity source) {
                        canBreathThisTick = source.container.removeOxygenAt(entity.getOnPos(), false);
                    }
                }
            }
        }

        if (!canBreathThisTick) {
            entity.hurt(DamageSourceRegistry.of(entity.level(), DamageSourceRegistry.OXYGEN), 0.5f);
        }
    }
}
