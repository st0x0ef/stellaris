package com.st0x0ef.stellaris.common.entities;

import com.st0x0ef.stellaris.common.menus.PlanetSelectionMenu;
import com.st0x0ef.stellaris.common.registry.ParticleRegistry;
import com.st0x0ef.stellaris.common.registry.SoundRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RocketEntity extends IVehicleEntity {

    public static final EntityDataAccessor<Integer> START_TIMER = SynchedEntityData.defineId(RocketEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> ROCKET_START = SynchedEntityData.defineId(RocketEntity.class, EntityDataSerializers.BOOLEAN);


    public RocketEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.entityData.set(START_TIMER, 0);
        this.entityData.set(ROCKET_START, false);

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder
                .define(ROCKET_START, false)
                .define(START_TIMER, 0)
                .build();

    }

    @Override
    public void tick() {
        super.tick();

//        this.rotateRocket();
//        this.checkOnBlocks();
//        this.fillUpRocket();
//        this.rocketExplosion();
//        this.burnEntities();

        if (this.entityData.get(ROCKET_START)) {
            this.spawnParticle();
            this.startTimerAndFlyMovement();

            if (this.getY() > 600) {
                this.openPlanetSelectionMenu();
            }
        }
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.getEntityData().set(ROCKET_START, compound.getBoolean("rocket_start"));
        this.getEntityData().set(START_TIMER, compound.getInt("start_timer"));

    }


    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void push(Entity entity) {

    }
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        super.interact(player, hand);
        InteractionResult result = InteractionResult.sidedSuccess(this.level().isClientSide);

        if (!this.level().isClientSide) {
            if (player.isCrouching()) {
                //this.openCustomInventoryScreen(player);
                return InteractionResult.CONSUME;
            }

            player.startRiding(this);
            return InteractionResult.CONSUME;
        }

        return result;
    }


    public double getRocketSpeed() {
        return Mth.clamp(0.65 * 1, 0.7, 1.5);
    }

    public void spawnParticle() {
        if (this.level() instanceof ServerLevel level) {
            Vec3 vec = this.getDeltaMovement();

            if (this.entityData.get(START_TIMER) == 200) {
                for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
                    level.sendParticles(player, (ParticleOptions) ParticleRegistry.LARGE_FLAME_PARTICLE.get(), true, this.getX() - vec.x, this.getY() - vec.y - 2.2, this.getZ() - vec.z, 20, 0.1, 0.1, 0.1, 0.001);
                    level.sendParticles(player, (ParticleOptions) ParticleRegistry.LARGE_SMOKE_PARTICLE.get(), true, this.getX() - vec.x, this.getY() - vec.y - 3.2, this.getZ() - vec.z, 10, 0.1, 0.1, 0.1, 0.04);
                }
            } else {
                for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
                    level.sendParticles(player, ParticleTypes.CAMPFIRE_COSY_SMOKE, true, this.getX() - vec.x, this.getY() - vec.y - 0.1, this.getZ() - vec.z, 6, 0.1, 0.1, 0.1, 0.023);
                }
            }
        }
    }

    public void startRocket() {
        Player player = (Player) this.getFirstPassenger();

        if (player != null) {
            SynchedEntityData data = this.getEntityData();

            //if (data.get(RocketEntity.FUEL) == this.getFuelCapacity()) {
                if (!data.get(RocketEntity.ROCKET_START)) {
                    System.out.println("Fly me to the moon");
                    data.set(RocketEntity.ROCKET_START, true);
                    this.level().playSound(null, this, SoundRegistry.ROCKET_SOUND.get(), SoundSource.NEUTRAL, 1, 1);
                    startTimerAndFlyMovement();
                }
            //} else {
            //    Methods.sendVehicleHasNoFuelMessage(player);
            //}
        }
    }


    public void startTimerAndFlyMovement() {
        if (this.entityData.get(START_TIMER) < 200) {
            this.entityData.set(START_TIMER, this.entityData.get(START_TIMER) + 1);
        }

        if (this.entityData.get(START_TIMER) == 200) {
            if (this.getDeltaMovement().y < this.getRocketSpeed() - 0.1) {
                this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y + 0.1, this.getDeltaMovement().z);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().x, this.getRocketSpeed(), this.getDeltaMovement().z);
            }
        }
    }

    private void destroyRocket(boolean explode) {
        if (!this.level().isClientSide) {
            if (explode) {
                this.level().explode(this, this.getX(), this.getBoundingBox().maxY, this.getZ(), 10, true, Level.ExplosionInteraction.TNT);
            }
            this.remove(RemovalReason.DISCARDED);
        }
    }

    public Player getFirstPlayerPassenger() {
        if (!this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof Player player) {
            return player;
        }

        return null;
    }

    public void openPlanetSelectionMenu() {
        Player player = this.getFirstPlayerPassenger();

        if (player != null) {
            player.openMenu(this.getPlanetMenuProvider());
        }
    }

    public MenuProvider getPlanetMenuProvider() {
        return new MenuProvider() {

            @Override
            public Component getDisplayName() {
                return Component.literal("Planets !!");
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                return PlanetSelectionMenu.create(syncId, inv);
            }
        };
    }


}
