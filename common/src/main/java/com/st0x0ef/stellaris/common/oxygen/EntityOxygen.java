package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.common.blocks.entities.OxygenSourceBlockEntity;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.registry.DamageSourceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class EntityOxygen {
    public static void tick(Entity entity) {
        if (!StellarisData.isPlanet(entity.level().dimension())) { return; }

        List<OxygenSourceBlockEntity> oxygen_source = new ArrayList<>();

        for (int x = -16; x < 16; x++) {
            for (int z = -16; z < 16; z++) {
                for (int y = -16; y < 16; y++) {
                    if (entity.level().getBlockEntity(new BlockPos(x + entity.getBlockX(), y + entity.getBlockY(), z + entity.getBlockZ())) instanceof OxygenSourceBlockEntity blockEntity) {
                        oxygen_source.add(blockEntity);
                    }
                }
            }
        }

        if (oxygen_source.isEmpty()) {
            entity.hurt(DamageSourceRegistry.of(entity.level(), DamageSourceRegistry.OXYGEN), 0.5f);
        }
    }
}
