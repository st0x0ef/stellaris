package com.st0x0ef.stellaris.common.blocks.entities;

import com.st0x0ef.stellaris.common.blocks.RadioactiveBlock;
import com.st0x0ef.stellaris.common.blocks.entities.machines.TickingBlockEntity;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.EffectsRegistry;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class RadioactiveBlockEntity extends BlockEntity implements TickingBlockEntity {

    private final int radioactivityLevel;
    private int tickCount = 0;

    public RadioactiveBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.RADIOACTIVE_BLOCK.get(), pos, state);

        if (state.getBlock() instanceof RadioactiveBlock block) {
            radioactivityLevel = block.getRadioactivityLevel();
            return;
        }
        radioactivityLevel = 0;
    }

    @Override
    public void tick() {
        AABB aabb = new AABB(getBlockPos()).inflate(5);
        if (tickCount >= 100) {
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb);

            for (LivingEntity entity : entities) {
                if (!Utils.isLivingInJetSuit(entity) && !entity.getType().is(TagRegistry.ENTITY_RADIATION_INVULNERABLE_TAG)) {
                    entity.addEffect(new MobEffectInstance(EffectsRegistry.getHolder(EffectsRegistry.RADIOACTIVE), 100, radioactivityLevel));
                }
            }
            tickCount = 0;
        }
        tickCount++;
    }
}
