package com.st0x0ef.stellaris.common.blocks.entities;

import com.st0x0ef.stellaris.Stellaris;
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

    public RadioactiveBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.RADIOACTIVE_BLOCK.get(), pos, state);
    }
    public RadioactiveBlockEntity(BlockPos pos, BlockState state, int level) {
        this(pos, state);
        this.radioactivityLevel = level;
    }

    int tickCount=0;
    public void tick() {
        AABB area = new AABB(this.getBlockPos()).inflate(5);
        if (tickCount==0||tickCount==99) {
            List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, area);
            for (LivingEntity entity : entities) {
                if (/**!Methods.isLivingInJetSuit(entity) ||*/!entity.getType().is(TagRegistry.ENTITY_RADIATION_INVULNERABLE_TAG)) {
                    if (radioactivityLevel == 1) {
                        entity.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE, 100, 0));
                    } else if (radioactivityLevel == 2) {
                        entity.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE, 100, 1));
                    } else if (radioactivityLevel == 3) {
                        entity.addEffect(new MobEffectInstance(EffectsRegistry.RADIOACTIVE, 100, 2));
                    } else {
                        Stellaris.LOG.info(String.valueOf(radioactivityLevel));
                    }
                }
            }
            tickCount=1;
        }
        tickCount++;
    }
}
