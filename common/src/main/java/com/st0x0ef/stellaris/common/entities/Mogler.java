package com.st0x0ef.stellaris.common.entities;

import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class Mogler extends Hoglin {
    public Mogler(EntityType<? extends Mogler> type, Level world) {
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

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkManager.createAddEntityPacket(this);
    }
}
