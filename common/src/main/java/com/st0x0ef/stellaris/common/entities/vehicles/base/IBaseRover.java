package com.st0x0ef.stellaris.common.entities.vehicles.base;

import com.st0x0ef.stellaris.common.entities.vehicles.IVehicleEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.List;

public abstract class IBaseRover extends IVehicleEntity
{
    public static float SCALE_FACTOR = 0.7F;

    private int steps;
    private double clientX;
    private double clientY;
    private double clientZ;
    private double clientYaw;
    private double clientPitch;

    protected float deltaRotation;

    public IBaseRover(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.blocksBuilding = true;
        recalculateBoundingBox();
    }

    @Override
    public float maxUpStep() {
        return 1f;
    }

    @Override
    public void tick() {
        if (!level().isClientSide) {
            this.xo = getX();
            this.yo = getY();
            this.zo = getZ();
        }

        super.tick();
        tickLerp();

        //recalculateBoundingBox();
    }

    public void recalculateBoundingBox() {
        double width = getCarWidth();
        double height = getCarHeight();
        setBoundingBox(new AABB(getX() - width / 2D, getY(), getZ() - width / 2D, getX() + width / 2D, getY() + height, getZ() + width / 2D));
    }

    public double getCarWidth() {
        return 1.3D;
    }

    public double getCarHeight() {
        return 1.6D;
    }

    //I Drive
    public Player getDriver() {
        List<Entity> passengers = getPassengers();
        if (passengers.size() <= 0) {
            return null;
        }

        if (passengers.get(0) instanceof Player) {
            return (Player) passengers.get(0);
        }

        return null;
    }

    public abstract int getPassengerSize();

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() < getPassengerSize();
    }

    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setYBodyRot(getYRot());
        float f = Mth.wrapDegrees(entityToUpdate.getYRot() - getYRot());
        float f1 = Mth.clamp(f, -130.0F, 130.0F);
        entityToUpdate.yRotO += f1 - f;
        entityToUpdate.setYRot(entityToUpdate.getYRot() + f1 - f);
        entityToUpdate.setYHeadRot(entityToUpdate.getYRot());
    }

    @Override
    public void onPassengerTurned(Entity entityToUpdate) {
        this.applyYawToEntity(entityToUpdate);
    }

    public abstract Vector3d[] getPlayerOffsets();

    @Override
    public void positionRider(Entity passenger, MoveFunction moveFunction) {
        if (!hasPassenger(passenger)) {
            return;
        }

        double front = 0.0F;
        double side = 0.0F;
        double height = 0.0F;

        List<Entity> passengers = getPassengers();

        if (passengers.size() > 0) {
            int i = passengers.indexOf(passenger);

            Vector3d offset = getPlayerOffsets()[i];
            front = offset.x;
            side = offset.z;
            height = offset.y;
        }

        Vec3 vec3d = (new Vec3(front, height, side)).yRot(-getYRot() * 0.017453292F - ((float) Math.PI / 2F));
        passenger.setPos(getX() + vec3d.x, getY() + vec3d.y, getZ() + vec3d.z);
        passenger.setYRot(passenger.getYRot() + deltaRotation);
        passenger.setYHeadRot(passenger.getYHeadRot() + this.deltaRotation);
        applyYawToEntity(passenger);
    }

    @Override
    public LivingEntity getControllingPassenger() {
        return getDriver();
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        if (!(entity instanceof IBaseRover)) {
                return false;
        }

        return (entity.canBeCollidedWith() || entity.isPushable()) && !isPassengerOfSameVehicle(entity);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return isAlive();
    }

    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            this.steps = 0;
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }

        if (this.steps > 0) {
            double d0 = getX() + (clientX - getX()) / (double) steps;
            double d1 = getY() + (clientY - getY()) / (double) steps;
            double d2 = getZ() + (clientZ - getZ()) / (double) steps;
            double d3 = Mth.wrapDegrees(clientYaw - (double) getYRot());
            setYRot((float) ((double) getYRot() + d3 / (double) steps));
            setXRot((float) ((double) getXRot() + (clientPitch - (double) getXRot()) / (double) steps));
            --steps;
            setPos(d0, d1, d2);
            setRot(getYRot(), getXRot());
        }
    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements) {
        this.clientX = x;
        this.clientY = y;
        this.clientZ = z;
        this.clientYaw = yaw;
        this.clientPitch = pitch;
        this.steps = 10;
    }

    public static double calculateMotionX(float speed, float rotationYaw) {
        return Mth.sin(-rotationYaw * 0.017453292F) * speed;
    }

    public static double calculateMotionZ(float speed, float rotationYaw) {
        return Mth.cos(rotationYaw * 0.017453292F) * speed;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!player.isShiftKeyDown()) {
            if (player.getVehicle() != this) {
                if (!level().isClientSide) {
                    player.startRiding(this);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    public abstract boolean doesEnterThirdPerson();

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity entity) {
        Direction direction = getMotionDirection();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.getDismountLocationForPassenger(entity);
        }
        int[][] offsets = DismountHelper.offsetsForDirection(direction);
        AABB bb = entity.getLocalBoundsForPose(Pose.STANDING);
        AABB carBB = getBoundingBox();
        for (int[] offset : offsets) {
            Vec3 dismountPos = new Vec3(getX() + (double) offset[0] * (carBB.getXsize() / 2D + bb.getXsize() / 2D + 1D / 16D), getY(), getZ() + (double) offset[1] * (carBB.getXsize() / 2D + bb.getXsize() / 2D + 1D / 16D));
            double y = level().getBlockFloorHeight(new BlockPos((int) dismountPos.x, (int) dismountPos.y, (int) dismountPos.z));
            if (DismountHelper.isBlockFloorValid(y)) {
                if (DismountHelper.canDismountTo(level(), entity, bb.move(dismountPos))) {
                    return dismountPos;
                }
            }
        }
        return super.getDismountLocationForPassenger(entity);
    }
}
