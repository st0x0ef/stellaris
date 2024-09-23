package com.st0x0ef.stellaris.common.entities.vehicles;

import com.google.common.collect.Sets;
import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTankHelper;
import com.st0x0ef.stellaris.common.data_components.RoverComponent;
import com.st0x0ef.stellaris.common.keybinds.KeyVariables;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import com.st0x0ef.stellaris.common.rocket_upgrade.FuelType;
import com.st0x0ef.stellaris.common.rocket_upgrade.MotorUpgrade;
import com.st0x0ef.stellaris.common.rocket_upgrade.TankUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class RoverEntity extends IVehicleEntity implements HasCustomInventoryScreen, ContainerListener {
    private double speed = 0;
    private final int fuelCapacityModifier = 0;
    public int FUEL;

    public float flyingSpeed = 0.02F;
    public float animationSpeedOld;
    public float animationSpeed;
    public float animationPosition;

    public MotorUpgrade MOTOR_UPGRADE;
    public TankUpgrade TANK_UPGRADE;

    private float FUEL_TIMER = 0;
    protected SimpleContainer inventory;

    private RoverComponent roverComponent;
    private Item currentFuelItem;


    public static final EntityDataAccessor<Integer> FUEL_CAPACITY = SynchedEntityData.defineId(RoverEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> FORWARD = SynchedEntityData.defineId(RoverEntity.class, EntityDataSerializers.BOOLEAN);

    public static final int DEFAULT_FUEL_BUCKETS = 3;

    public RoverEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.inventory = new SimpleContainer(14);

        this.roverComponent = new RoverComponent( currentFuelItem.toString(), FUEL, MOTOR_UPGRADE.getFluidTexture(), TANK_UPGRADE.getTankCapacity());

    }


    public void setRocketComponent(RoverComponent rocketComponent) {
        this.MOTOR_UPGRADE = rocketComponent.getMotorUpgrade();
        this.TANK_UPGRADE = rocketComponent.getTankUpgrade();
        this.FUEL = rocketComponent.getFuel();
        this.currentFuelItem = FuelType.getItemBasedOnTypeName(rocketComponent.fuelType());
    }


    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("InventoryCustom", this.inventory.createTag(registryAccess()));
        compound.putInt("fuel", FUEL);

        if (FUEL != 0 && currentFuelItem != null) {
            if (FuelType.Type.getTypeBasedOnItem(currentFuelItem) != null) {
                compound.putString("currentFuelItemType", FuelType.Type.getTypeBasedOnItem(currentFuelItem).getSerializedName());
            } else if (FuelType.Type.Radioactive.getTypeBasedOnItem(currentFuelItem) != null) {
                compound.putString("currentFuelItemType", FuelType.Type.Radioactive.getTypeBasedOnItem(currentFuelItem).getSerializedName());
            }
        }

        ListTag listTag = new ListTag();

        for(int i = 1; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemStack = this.inventory.getItem(i);
            if (!itemStack.isEmpty()) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putByte("Slot", (byte)(i - 1));
                listTag.add(itemStack.save(this.registryAccess(), compoundTag));
            }
        }

        compound.put("Items", listTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        ListTag inventoryCustom = compound.getList("InventoryCustom", 14);
        this.inventory.fromTag(inventoryCustom, registryAccess());
        FUEL = compound.getInt("fuel");

        if (FUEL != 0) {
            currentFuelItem = FuelType.getItemBasedOnTypeName(compound.getString("currentFuelItemType"));
        }

        ListTag listTag = compound.getList("Items", 10);

        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);
            int j = compoundTag.getByte("Slot") & 255;
            if (j < this.inventory.getContainerSize() - 1) {
                this.inventory.setItem(j + 1, ItemStack.parse(this.registryAccess(), compoundTag).orElse(ItemStack.EMPTY));
            }
        }
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

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return null;
    }


    @Override
    public void kill() {
        this.spawnRoverItem();
        this.dropEquipment();

        if (!this.level().isClientSide) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    public ItemStack getRoverItem() {
        ItemStack rover = new ItemStack(ItemsRegistry.ROVER.get(), 1);
        rover.set(DataComponentsRegistry.ROVER_COMPONENT.get(), roverComponent);

        return rover;
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
        ItemStack itemStack = this.getRoverItem();
        ItemEntity entityToSpawn = new ItemEntity(level(), this.getX(), this.getY(), this.getZ(), itemStack);
        entityToSpawn.setPickUpDelay(10);
        level().addFreshEntity(entityToSpawn);
    }

    protected void dropEquipment() {
        for (int i = 0; i < this.inventory.getItems().size(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
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

            if ((KeyVariables.isHoldingRight(player) && KeyVariables.isHoldingLeft(player)) || FUEL == 0 || player.getVehicle().isEyeInFluid(FluidTags.WATER)) {
                return;
            }

            if (this.getforward()) {
                if (KeyVariables.isHoldingRight(player)) {
                    this.setEntityRotation(this, 1);
                }
            } else {
                if (KeyVariables.isHoldingRight(player)) {
                    this.setEntityRotation(this, -1);
                }
            }

            if (this.getforward()) {
                if (KeyVariables.isHoldingLeft(player)) {
                    this.setEntityRotation(this, -1);
                }
            } else {
                if (KeyVariables.isHoldingLeft(player)) {
                    this.setEntityRotation(this, 1);
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
                if (!tryFillUpRocket(player.getMainHandItem().getItem())) {
                    this.openCustomInventoryScreen(player);
                } else {
                    player.getItemInHand(hand).grow(-1);
                    player.getInventory().add(new ItemStack(Items.BUCKET));
                }
                return InteractionResult.CONSUME;
            }

            player.startRiding(this, false);


            return InteractionResult.CONSUME;
        }

        return result;

    }

    public boolean tryFillUpRocket(Item item) {
        if (this.level().isClientSide) return false;
        if (FUEL == TANK_UPGRADE.getTankCapacity() || item == null) {
            return false;
        }

        if (MOTOR_UPGRADE.getFuelType().equals(FuelType.Type.RADIOACTIVE) && FuelType.Type.Radioactive.getTypeBasedOnItem(item) != null && canPutFuelBasedOnCurrentFuelItem(item)) {
            FUEL += 1000;
            if (FUEL > TANK_UPGRADE.getTankCapacity()) {
                FUEL = TANK_UPGRADE.getTankCapacity();
            }

            inventory.removeItem(0, 1);

            return true;
        }

        if (FuelType.Type.getTypeBasedOnItem(item) == MOTOR_UPGRADE.getFuelType() && canPutFuelBasedOnCurrentFuelItem(item)) {
            FUEL += 1000;
            if (FUEL > TANK_UPGRADE.getTankCapacity()) {
                FUEL = TANK_UPGRADE.getTankCapacity();
            }

            if (inventory.removeItem(0, 1).is(ItemsRegistry.FUEL_BUCKET.get())) {
                inventory.setItem(1, new ItemStack(Items.BUCKET, inventory.getItem(1).getCount()+1));
            }

            return true;
        }

        return false;
    }

    private boolean canPutFuelBasedOnCurrentFuelItem(Item item) {
        if (FUEL == 0) {
            currentFuelItem = item;
            return true;
        }

        return currentFuelItem == item;
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


        if (this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof Player passanger) || this.isInWater()) {
            return;
        }

        FUEL_TIMER++;

        passanger.resetFallDistance();

        float FUEL_USE_TICK = 8;
        if (passanger.zza > 0.01 && FUEL != 0) {

            if (FUEL_TIMER > FUEL_USE_TICK) {
                FUEL--;
                FUEL_TIMER = 0;
            }
            this.entityData.set(FORWARD, true);
        } else if (passanger.zza < -0.01 && FUEL != 0) {

            if (FUEL_TIMER > FUEL_USE_TICK) {
                FUEL--;
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
            //this.setMaxUpStep(1.0F);

            double pmovement = passanger.zza;

            if (pmovement == 0 || FUEL == 0 || this.isEyeInFluid(FluidTags.WATER)) {
                pmovement = 0;
                this.setSpeed(0f);

                if (speed != 0 && speed > 0.02) {
                    speed = speed - 0.02;
                }
            }

            if (this.entityData.get(FORWARD) && FUEL != 0) {
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

                if (FUEL != 0 && !this.isEyeInFluid(FluidTags.WATER)) {

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

    public RoverComponent getRocketComponent() {
        return this.roverComponent;
    }

}
