package com.st0x0ef.stellaris.common.blocks.entities;

import com.st0x0ef.stellaris.common.registry.EffectsRegistry;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class RadioactiveBlockEntity extends BlockEntity {
    public int radioactivityLevel;

    public RadioactiveBlockEntity(BlockPos pos, BlockState state, int level) {
        super(EntityRegistry.URANIUM_BLOCK.get(), pos, state);
        this.radioactivityLevel = level;
    }

    public void tick() {
        AABB area = new AABB(this.getBlockPos()).inflate(5);

        List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, area);
        for (LivingEntity entity : entities) {
            if(/**!Methods.isLivingInJetSuit(entity) &&*/ !entity.getType().is(TagRegistry.ENTITY_RADIATION_INVULNERABLE_TAG)) {
                switch (radioactivityLevel) {
                    case 1 : entity.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE_LVL_1.get(), 100));
                    case 2 : entity.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE_LVL_2.get(), 100));
                    case 3 : entity.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE_LVL_3.get(), 100));
                }
            }
        }
    }
}
