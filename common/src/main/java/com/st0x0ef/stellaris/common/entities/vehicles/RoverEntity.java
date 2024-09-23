package com.st0x0ef.stellaris.common.entities.vehicles;

import com.google.common.collect.Sets;
import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTankHelper;
import com.st0x0ef.stellaris.common.keybinds.KeyVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class RoverEntity extends IVehicleEntity implements HasCustomInventoryScreen, ContainerListener {
    private double speed = 0;
    private final int fuelCapacityModifier = 0;

    public float flyingSpeed = 0.02F;
    public float animationSpeedOld;
    public float animationSpeed;
    public float animationPosition;

    private float FUEL_TIMER = 0;
    protected SimpleContainer inventory;

    public static final EntityDataAccessor<Integer> FUEL = SynchedEntityData.defineId(RoverEntity.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Integer> FUEL_CAPACITY = SynchedEntityData.defineId(RoverEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> FORWARD = SynchedEntityData.defineId(RoverEntity.class, EntityDataSerializers.BOOLEAN);

    public static final int DEFAULT_FUEL_BUCKETS = 3;

    public RoverEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.inventory = new SimpleContainer(14);

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.put("InventoryCustom", this.inventory.createTag(registryAccess()));

        compound.putInt("fuel", this.getEntityData().get(FUEL));
        compound.putInt("fuel_capacity", this.getEntityData().get(FUEL_CAPACITY));
        compound.putBoolean("forward", this.getEntityData().get(FORWARD));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        Tag inventoryCustom = compound.get("InventoryCustom");
        if (inventoryCustom instanceof CompoundTag) {
            inventory.deserializeNBT((CompoundTag) inventoryCustom);
        }

        this.getEntityData().set(FUEL, compound.getInt("fuel"));
        this.getEntityData().set(FUEL_CAPACITY, compound.getInt("fuel_capacity"));
        this.getEntityData().set(FORWARD, compound.getBoolean("forward"));
    }

    public int getFuelCapacity() {
        return (DEFAULT_FUEL_BUCKETS + fuelCapacityModifier) *  (int) FluidTankHelper.BUCKET_AMOUNT;
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

    @Deprecated
    public boolean canBeRiddenInWater() {
        return true;
    }


    @Override
    public void onPassengerTurned(Entity entity) {
        this.applyYawToEntity(entity);
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
        Vec3[] avector3d = new Vec3[]{getCollisionHorizontalEscapeVector(this.getBbWidth(), livingEntity.getBbWidth(), livingEntity.getYRot()), getCollisionHorizontalEscapeVector(this.getBbWidth(), livingEntity.getBbWidth(), livingEntity.getYRot() - 22.5F), getCollisionHorizontalEscapeVector(this.getBbWidth(), livingEntity.getBbWidth(), livingEntity.getYRot() + 22.5F), getCollisionHorizontalEscapeVector(this.getBbWidth(), livingEntity.getBbWidth(), livingEntity.getYRot() - 45.0F), getCollisionHorizontalEscapeVector(this.getBbWidth(), livingEntity.getBbWidth(), livingEntity.getYRot() + 45.0F)};
        Set<BlockPos> set = Sets.newLinkedHashSet();
        double d0 = this.getBoundingBox().maxY;
        double d1 = this.getBoundingBox().minY - 0.5D;
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();

        for(Vec3 vector3d : avector3d) {
            blockpos$mutable.set(this.getX() + vector3d.x, d0, this.getZ() + vector3d.z);

            for(double d2 = d0; d2 > d1; --d2) {
                set.add(blockpos$mutable.immutable());
                blockpos$mutable.move(Direction.DOWN);
            }
        }

        for(BlockPos blockpos : set) {
            if (!this.level().getFluidState(blockpos).is(FluidTags.LAVA)) {
                double d3 = this.level().getBlockFloorHeight(blockpos);
                if (DismountHelper.isBlockFloorValid(d3)) {
                    Vec3 vector3d1 = Vec3.upFromBottomCenterOf(blockpos, d3);

                    for(Pose pose : livingEntity.getDismountPoses()) {
                        if (DismountHelper.isBlockFloorValid(this.level().getBlockFloorHeight(blockpos))) {
                            livingEntity.setPose(pose);
                            return vector3d1;
                        }
                    }
                }
            }
        }

        return new Vec3(this.getX(), this.getBoundingBox().maxY, this.getZ());
    }

    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(entityToUpdate.getYRot() - this.getYRot());
        float f1 = Mth.clamp(f, -105.0F, 105.0F);
        entityToUpdate.yRotO += f1 - f;
        entityToUpdate.setYRot(entityToUpdate.getYRot() + f1 - f);
        entityToUpdate.setYHeadRot(entityToUpdate.getYRot());
    }

    @Override
    protected void removePassenger(Entity passenger) {
        if (passenger.isCrouching() && !passenger.level().isClientSide) {
            if (passenger instanceof ServerPlayer) {
                this.setSpeed(0f);
            }
        }
        super.removePassenger(passenger);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        ItemStack itemStack = new ItemStack(ItemsRegistry.ROVER_ITEM.get(), 1);
        itemStack.getOrCreateTag().putInt(BeyondEarth.MODID + ":fuel", this.entityData.get(FUEL));

        return itemStack;
    }

    @Override
    public void kill() {
        this.spawnRoverItem();
        this.dropEquipment();

        if (!this.level().isClientSide) {
            this.remove(RemovalReason.DISCARDED);
        }
    }



    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() != null && source.getEntity().isCrouching() && !this.isVehicle()) {
            this.spawnRoverItem();
            this.dropEquipment();

            if (!this.level().isClientSide) {
                this.remove(RemovalReason.DISCARDED);
            }
            return true;
        }
        return false;
    }

    protected void spawnRoverItem() {
        ItemStack itemStack = new ItemStack(ItemsRegistry.ROVER_ITEM.get(), 1);
        itemStack.getOrCreateTag().putInt(BeyondEarth.MODID + ":fuel", this.getEntityData().get(FUEL));

        ItemEntity entityToSpawn = new ItemEntity(level(), this.getX(), this.getY(), this.getZ(), itemStack);
        entityToSpawn.setPickUpDelay(10);
        level().addFreshEntity(entityToSpawn);
    }

    protected void dropEquipment() {
        for (int i = 0; i < inventory.getSlots(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
                this.spawnAtLocation(itemstack);
            }
        }
    }




    public Player getFirstPlayerPassenger() {
        if (!this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof Player player) {

            return player;
        }

        return null;
    }

    public void rotateRover() {
        Player player = this.getFirstPlayerPassenger();

        if (player != null) {

            if ((KeyVariables.isHoldingRight(player) && KeyVariables.isHoldingLeft(player)) || player.getVehicle().getEntityData().get(RoverEntity.FUEL) == 0 || player.getVehicle().isEyeInFluid(FluidTags.WATER)) {
                return;
            }

            if (this.getforward()) {
                if (KeyVariables.isHoldingRight(player)) {
                    Methods.setEntityRotation(this, 1);
                }
            } else {
                if (KeyVariables.isHoldingRight(player)) {
                    Methods.setEntityRotation(this, -1);
                }
            }

            if (this.getforward()) {
                if (KeyVariables.isHoldingLeft(player)) {
                    Methods.setEntityRotation(this, -1);
                }
            } else {
                if (KeyVariables.isHoldingLeft(player)) {
                    Methods.setEntityRotation(this, 1);
                }
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        super.interact(player, hand);
        InteractionResult result = InteractionResult.sidedSuccess(this.level().isClientSide);

        if (!this.level().isClientSide) {
            if (player.isCrouching()) {
                NetworkHooks.openScreen((ServerPlayer) player, new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return RoverEntity.this.getDisplayName();
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                        FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
                        packetBuffer.writeVarInt(RoverEntity.this.getId());
                        return new RoverMenu.GuiContainer(id, inventory, packetBuffer);
                    }
                }, buf -> buf.writeVarInt(this.getId()));

                return InteractionResult.CONSUME;
            }

            player.startRiding(this);
            return InteractionResult.CONSUME;
        }

        return result;
    }

    public boolean getforward() {
        return this.entityData.get(FORWARD);
    }

    @Override
    public void tick() {
        super.tick();

        /** Reset Fall Damage for Passengers too */
        this.resetFallDistance();
        this.rotateRover();

        //Fuel Load up
        if (this.inventory.getStackInSlot(0).getItem() instanceof BucketItem) {
            if (((BucketItem) this.getInventory().getStackInSlot(0).getItem()).getFluid().is(TagRegistry.FLUID_VEHICLE_FUEL_TAG)) {
                if (this.entityData.get(FUEL) + FluidUtils.BUCKET_SIZE <= getFuelCapacity()) {
                    this.getEntityData().set(FUEL, (this.getEntityData().get(FUEL) + FluidUtils.BUCKET_SIZE));
                    this.inventory.setStackInSlot(0, new ItemStack(Items.BUCKET));
                }
            }
        }

        if (this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof Player passanger) || this.isInWater()) {
            return;
        }

        FUEL_TIMER++;

        passanger.resetFallDistance();

        float FUEL_USE_TICK = 8;
        if (passanger.zza > 0.01 && this.getEntityData().get(FUEL) != 0) {

            if (FUEL_TIMER > FUEL_USE_TICK) {
                this.entityData.set(FUEL, this.getEntityData().get(FUEL) - 1);
                FUEL_TIMER = 0;
            }
            this.entityData.set(FORWARD, true);
        } else if (passanger.zza < -0.01 && this.getEntityData().get(FUEL) != 0) {

            if (FUEL_TIMER > FUEL_USE_TICK) {
                this.entityData.set(FUEL, this.getEntityData().get(FUEL) - 1);
                FUEL_TIMER = 0;
            }
            this.entityData.set(FORWARD, false);
        }
    }

    @Override
    public float getFrictionInfluencedSpeed(float p_21331_) {
        return this.onGround() ? this.getSpeed() * (0.21600002F / (p_21331_ * p_21331_ * p_21331_)) : this.flyingSpeed;
    }

    @Override
    public void travel(Vec3 p_21280_) {
        this.calculateEntityAnimation(this, this instanceof FlyingAnimal);

        if (!this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof Player passanger) {

            this.flyingSpeed = this.getSpeed() * 0.15F;
            this.setMaxUpStep(1.0F);

            double pmovement = passanger.zza;

            if (pmovement == 0 || this.getEntityData().get(FUEL) == 0 || this.isEyeInFluid(FluidTags.WATER)) {
                pmovement = 0;
                this.setSpeed(0f);

                if (speed != 0 && speed > 0.02) {
                    speed = speed - 0.02;
                }
            }

            if (this.entityData.get(FORWARD) && this.getEntityData().get(FUEL) != 0) {
                if (this.getSpeed() >= 0.01) {
                    if (speed <= 0.32) {
                        speed = speed + 0.02;
                    }
                }

                if (this.getSpeed() < 0.25) {
                    this.setSpeed(this.getSpeed() + 0.02F);
                }

            }

            if (!this.entityData.get(FORWARD)) {

                if (this.getEntityData().get(FUEL) != 0 && !this.isEyeInFluid(FluidTags.WATER)) {

                    if (this.getSpeed() <= 0.04) {
                        this.setSpeed(this.getSpeed() + 0.02f);
                    }
                }

                if (this.getSpeed() >= 0.08) {
                    this.setSpeed(0f);
                }
            }

            if (this.entityData.get(FORWARD)) {
                this.setWellRotationPlus(4.0f, 0.4f);
            } else {
                this.setWellRotationMinus(8.0f, 0.8f);
            }

            super.travel(new Vec3(0, 0, pmovement));
            return;
        }

        super.travel(new Vec3(0, 0, 0));
    }

    public void setWellRotationMinus(float rotation1, float rotation2) {
        this.animationSpeedOld = this.animationSpeed;
        double d1 = this.getX() - this.xo;
        double d0 = this.getZ() - this.zo;
        float f1 = -Mth.sqrt((float) (d1 * d1 + d0 * d0)) * rotation1;
        if (f1 > 1.0F)
            f1 = 1.0F;
        this.animationSpeed += (f1 - this.animationSpeed) * rotation2;
        this.animationPosition += this.animationSpeed;
    }

    public void setWellRotationPlus(float rotation1, float rotation2) {
        this.animationSpeedOld = this.animationSpeed;
        double d1 = this.getX() - this.xo;
        double d0 = this.getZ() - this.zo;
        float f1 = Mth.sqrt((float) (d1 * d1 + d0 * d0)) * rotation1;
        if (f1 > 1.0F)
            f1 = 1.0F;
        this.animationSpeed += (f1 - this.animationSpeed) * rotation2;
        this.animationPosition += this.animationSpeed;
    }

    public void calculateEntityAnimation(RoverEntity p_21044_, boolean p_21045_) {
        p_21044_.animationSpeedOld = p_21044_.animationSpeed;
        double d0 = p_21044_.getX() - p_21044_.xo;
        double d1 = p_21045_ ? p_21044_.getY() - p_21044_.yo : 0.0D;
        double d2 = p_21044_.getZ() - p_21044_.zo;
        float f = (float)Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 4.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        p_21044_.animationSpeed += (f - p_21044_.animationSpeed) * 0.4F;
        p_21044_.animationPosition += p_21044_.animationSpeed;
    }

    @Override
    public void containerChanged(Container container) {

    }

    @Override
    public void openCustomInventoryScreen(Player player) {

    }
}
