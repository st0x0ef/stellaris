package com.st0x0ef.stellaris.common.blocks.entities;

import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class UraniumBlockEntity extends BlockEntity {
    public UraniumBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.URANIUM_BLOCK.get(), pos, state);
    }

    public void tick() {
        AABB area = new AABB(this.getBlockPos()).inflate(5);

        List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, area);
        for (LivingEntity entity : entities) {
            if(/**!Methods.isLivingInJetSuit(entity) &&*/ !entity.getType().is(TagRegistry.ENTITY_RADIATION_INVULNERABLE_TAG)) {
                //entity.addEffect(new MobEffectInstance(MobEffectsRegistry.RADIATION.get(), 100));
            }
        }
    }
}
