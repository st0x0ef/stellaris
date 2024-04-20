package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.registry.DamageSourceRegistry;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public abstract class OxygenSourceBlockEntity extends BlockEntity {
    public final int range;

    protected OxygenSourceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int range) {
        super(type, pos, blockState);
        this.range = range;
    }

    protected void tick() {
        if (this.level.dimension() == Level.OVERWORLD || this.level.dimension() == Level.NETHER || this.level.dimension() == Level.END) { return; } // TODO : Change this to check if we are on a planet when it will be implemented

        List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, new AABB(this.getBlockPos()).inflate(range));

        for (LivingEntity entity : entities) {
            if (entity instanceof Player player) {
                // Check if the player wears a jet suit, if not he takes damage.
            }

            else {
                if (!entity.getType().is(TagRegistry.ENTITY_NO_OXYGEN_NEEDED_TAG)) {
                    entity.hurt(DamageSourceRegistry.of(entity.level(), DamageSourceRegistry.OXYGEN), 0.5f);
                }
            }
        }
    }
}
