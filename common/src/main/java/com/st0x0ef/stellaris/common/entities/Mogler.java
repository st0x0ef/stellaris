package com.st0x0ef.stellaris.common.entities;

import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Mogler extends Hoglin {
    public Mogler(EntityType<Mogler> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6)
                .add(Attributes.ATTACK_KNOCKBACK, 0.6)
                .add(Attributes.ATTACK_DAMAGE, 6);
    }

    public static boolean checkMoglerSpawnRules(EntityType<Mogler> p_219182_, LevelAccessor p_219183_, MobSpawnType p_219184_, BlockPos p_219185_, RandomSource p_219186_) {
        return !p_219183_.getBlockState(p_219185_.below()).is(Blocks.NETHER_WART_BLOCK);
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public boolean removeWhenFarAway(double d) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        Mogler moglerentity = EntityRegistry.MOGLER.get().create(serverLevel);
        if (moglerentity != null) {
            moglerentity.setPersistenceRequired();
        }
        return moglerentity;
    }

    private boolean MOGLER_SPAWN = true;
    @Override
    public void tick() {
        super.tick();
        if (!MOGLER_SPAWN) {
            if (!this.level().isClientSide) {
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }
}
