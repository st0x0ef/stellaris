package com.st0x0ef.stellaris.common.entities.vehicles.base;

import com.st0x0ef.stellaris.common.network.packets.SyncRoverPacket;
import com.st0x0ef.stellaris.common.utils.MathUtils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractRoverBase extends IBaseRover
{

    private float wheelRotation;

    private boolean collidedLastTick;

    private static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(AbstractRoverBase.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> FORWARD = SynchedEntityData.defineId(AbstractRoverBase.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> BACKWARD = SynchedEntityData.defineId(AbstractRoverBase.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(AbstractRoverBase.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> RIGHT = SynchedEntityData.defineId(AbstractRoverBase.class, EntityDataSerializers.BOOLEAN);

    public AbstractRoverBase(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public float maxUpStep() {
        return 1F;
    }

    public abstract float getMaxSpeed();

    public abstract float getMaxReverseSpeed();

    public abstract float getAcceleration();

    public abstract float getMaxRotationSpeed();

    public abstract float getMinRotationSpeed();

    public abstract float getRollResistance();

    public abstract float getRotationModifier();

    public abstract float getPitch();

    @Override
    public void tick() {
        super.tick();

        Runnable task;
        while ((task = tasks.poll()) != null) {
            task.run();
        }

        updateGravity();
        controlRover();
        checkPush();

        move(MoverType.SELF, getDeltaMovement());

        if (level().isClientSide) {
        }

        updateWheelRotation();
    }

    public void centerCar() {
        Direction facing = getDirection();
        switch (facing) {
            case SOUTH:
                setYRot(0F);
                break;
            case NORTH:
                setYRot(180F);
                break;
            case EAST:
                setYRot(-90F);
                break;
            case WEST:
                setYRot(90F);
                break;
        }
    }

    private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();

    @Override
    public boolean canCollideWith(Entity entityIn) {
        if (!level().isClientSide && entityIn instanceof LivingEntity && !getPassengers().contains(entityIn)) {
            if (entityIn.getBoundingBox().intersects(getBoundingBox())) {
                float speed = getSpeed();
                if (speed > 0.35F) {
                    float damage = speed * 10;
                    tasks.add(() -> {
                        ServerLevel serverLevel = (ServerLevel) level();
                        Optional<Holder.Reference<DamageType>> holder = serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(DamageTypes.DROWN);
                        holder.ifPresent(damageTypeReference -> entityIn.hurt(new DamageSource(damageTypeReference, this), damage));
                    });
                }
            }
        }
        return super.canCollideWith(entityIn);
    }


    public void checkPush() {
        List<Player> list = level().getEntitiesOfClass(Player.class, getBoundingBox().expandTowards(0.2, 0, 0.2).expandTowards(-0.2, 0, -0.2));

        for (Player player : list) {
            if (!player.hasPassenger(this) && player.isShiftKeyDown()) {
                double motX = calculateMotionX(0.05F, player.getYRot());
                double motZ = calculateMotionZ(0.05F, player.getYRot());
                move(MoverType.PLAYER, new Vec3(motX, 0, motZ));
                return;
            }
        }
    }

    public void controlRover() {
        if (!isVehicle() && isEnoughFuel()) {
            setForward(false);
            setBackward(false);
            setLeft(false);
            setRight(false);
        }

        float modifier = 0.5F;

        float maxSp = getMaxSpeed() * modifier;
        float maxBackSp = getMaxReverseSpeed() * modifier;

        float speed = MathUtils.subtractToZero(getSpeed(), getRollResistance());

        if (isForward()) {
            if (speed <= maxSp) {
                speed = Math.min(speed + getAcceleration(), maxSp);
            }
        }

        if (isBackward()) {
            if (speed >= -maxBackSp) {
                speed = Math.max(speed - getAcceleration(), -maxBackSp);
            }
        }

        setSpeed(speed);

        float rotationSpeed = 0;
        if (Math.abs(speed) > 0.02F) {
            rotationSpeed = Mth.abs(getRotationModifier() / (float) Math.pow(speed, 2));
            rotationSpeed = Mth.clamp(rotationSpeed, getMinRotationSpeed(), getMaxRotationSpeed());
        }

        deltaRotation = 0;

        if (speed < 0) {
            rotationSpeed = -rotationSpeed;
        }

        if (isLeft()) {
            deltaRotation -= rotationSpeed;
        }
        if (isRight()) {
            deltaRotation += rotationSpeed;
        }

        deltaRotation = Mth.clamp(deltaRotation, -45.0F, 45.0F);

        setYRot(getYRot() + deltaRotation);
        float delta = Math.abs(getYRot() - yRotO);

        while (getYRot() > 180F) {
            setYRot(getYRot() - 360F);
            yRotO = getYRot() - delta;
        }
        while (getYRot() <= -180F) {
            setYRot(getYRot() + 360F);
            yRotO = delta + getYRot();
        }

        if (horizontalCollision) {
            if (level().isClientSide && !collidedLastTick) {
                onCollision(speed);
                collidedLastTick = true;
            }
        } else {
            setDeltaMovement(calculateMotionX(getSpeed(), getYRot()), getDeltaMovement().y, calculateMotionZ(getSpeed(), getYRot()));
            if (level().isClientSide) {
                collidedLastTick = false;
            }
        }
    }

    protected abstract boolean isEnoughFuel();



    private float getaFloat() {
        float modifier = 1F;

        float maxSp = getMaxSpeed() * modifier;
        float maxBackSp = getMaxReverseSpeed() * modifier;

        float speed = MathUtils.subtractToZero(getSpeed(), getRollResistance());

        if (isForward()) {
            if (speed <= maxSp) {
                speed = Math.min(speed + getAcceleration(), maxSp);
            }
        }

        if (isBackward()) {
            if (speed >= -maxBackSp) {
                speed = Math.max(speed - getAcceleration(), -maxBackSp);
            }
        }
        return speed;
    }

    public void onCollision(float speed) {
        setSpeed(0.01F);
        setDeltaMovement(0D, getDeltaMovement().y, 0D);
    }

    public boolean canPlayerDriveCar(Player player) {
        if (player.equals(getDriver())) {
            return true;
        } else if (isInWater() || isInLava()) {
            return false;
        } else {
            return false;
        }
    }

    private void updateGravity() {
        if (isNoGravity()) {
            setDeltaMovement(getDeltaMovement().x, 0D, getDeltaMovement().z);
            return;
        }
        setDeltaMovement(getDeltaMovement().x, getDeltaMovement().y - 0.2D, getDeltaMovement().z);
    }

    public void updateControls(boolean forward, boolean backward, boolean left, boolean right, Player player) {
        boolean needsUpdate = false;

        if (isForward() != forward) {
            setForward(forward);
            needsUpdate = true;
        }

        if (isBackward() != backward) {
            setBackward(backward);
            needsUpdate = true;
        }

        if (isLeft() != left) {
            setLeft(left);
            needsUpdate = true;
        }

        if (isRight() != right) {
            setRight(right);
            needsUpdate = true;
        }
        if (level().isClientSide && needsUpdate) {
            NetworkManager.sendToServer(new SyncRoverPacket(forward, backward, left, right, player));
        }
    }

    public abstract double getPlayerYOffset();

    public boolean canPlayerEnterCar(Player player) {
        return true;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!canPlayerEnterCar(player)) {
            return InteractionResult.FAIL;
        }
        return super.interact(player, hand);
    }

    public float getKilometerPerHour() {
        return (getSpeed() * 20 * 60 * 60) / 1000;
    }

    public float getWheelRotationAmount() {
        return 120F * getSpeed();
    }

    public void updateWheelRotation() {
        wheelRotation += getWheelRotationAmount();
    }

    public float getWheelRotation(float partialTicks) {
        return wheelRotation + getWheelRotationAmount() * partialTicks;
    }

    public boolean isAccelerating() {
        boolean b = (isForward() || isBackward()) && !horizontalCollision;
        return b;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SPEED, 0F);
        builder.define(FORWARD, false);
        builder.define(BACKWARD, false);
        builder.define(LEFT, false);
        builder.define(RIGHT, false);
    }

    public void setSpeed(float speed) {
        this.entityData.set(SPEED, speed);
    }

    public float getSpeed() {
        return this.entityData.get(SPEED);
    }

    public void setForward(boolean forward) {
        entityData.set(FORWARD, forward);
    }

    public boolean isForward() {
        if (getDriver() == null || !canPlayerDriveCar(getDriver())) {
            return false;
        }
        return entityData.get(FORWARD);
    }

    public void setBackward(boolean backward) {
        entityData.set(BACKWARD, backward);
    }

    public boolean isBackward() {
        if (getDriver() == null || !canPlayerDriveCar(getDriver())) {
            return false;
        }
        return entityData.get(BACKWARD);
    }

    public void setLeft(boolean left) {
        entityData.set(LEFT, left);
    }

    public boolean isLeft() {
        return entityData.get(LEFT);
    }

    public void setRight(boolean right) {
        entityData.set(RIGHT, right);
    }

    public boolean isRight() {
        return entityData.get(RIGHT);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }
}
