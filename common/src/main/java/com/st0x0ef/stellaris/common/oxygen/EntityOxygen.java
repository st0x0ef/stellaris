package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.common.blocks.entities.OxygenSourceBlockEntity;
import com.st0x0ef.stellaris.common.registry.DamageSourceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class EntityOxygen {
    public static void tick(Entity entity) {
        if (entity.level().dimension() == Level.OVERWORLD || entity.level().dimension() == Level.NETHER || entity.level().dimension() == Level.END) { return; } // TODO : Change this to check if we are on a planet when it will be implemented

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
