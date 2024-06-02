package com.st0x0ef.stellaris.common.entities;

import com.google.common.collect.Sets;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.RocketModel;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data_components.RocketComponent;
import com.st0x0ef.stellaris.common.items.upgrade.FuelType;
import com.st0x0ef.stellaris.common.items.upgrade.RocketUpgradeItem;
import com.st0x0ef.stellaris.common.menus.RocketMenu;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.registry.EntityData;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import com.st0x0ef.stellaris.common.registry.SoundRegistry;
import com.st0x0ef.stellaris.common.rocket_upgrade.ModelUpgrade;
import com.st0x0ef.stellaris.common.rocket_upgrade.MotorUpgrade;
import com.st0x0ef.stellaris.common.rocket_upgrade.SkinUpgrade;
import com.st0x0ef.stellaris.common.rocket_upgrade.TankUpgrade;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class RocketEntity extends IVehicleEntity implements HasCustomInventoryScreen, ContainerListener {

    public int START_TIMER;
    public boolean ROCKET_START;
    public int FUEL;

    public SkinUpgrade SKIN_UPGRADE;
    public ModelUpgrade MODEL_UPGRADE;
    public MotorUpgrade MOTOR_UPGRADE;
    public TankUpgrade TANK_UPGRADE;

    protected SimpleContainer inventory;

    public RocketEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.START_TIMER = 0;
        this.ROCKET_START = false;
        this.FUEL = 0;

        this.SKIN_UPGRADE = SkinUpgrade.getBasic();
        this.MODEL_UPGRADE = ModelUpgrade.getBasic();
        this.MOTOR_UPGRADE = MotorUpgrade.getBasic();
        this.TANK_UPGRADE = TankUpgrade.getBasic();

        this.inventory = new SimpleContainer(14);
    }

    @Override
    public void tick() {
        super.tick();

        this.rocketExplosion();
        this.burnEntities();
        this.checkContainer();
        if (ROCKET_START) {
            this.spawnParticle();
            this.startTimerAndFlyMovement();

            if (this.getY() > 600) {
                this.openPlanetMenu(this.getFirstPlayerPassenger());
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.put("InventoryCustom", this.inventory.createTag(registryAccess()));
        compound.putInt("fuel", FUEL);
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        ListTag inventoryCustom = compound.getList("InventoryCustom", 10);
        this.inventory.fromTag(inventoryCustom, registryAccess());
        FUEL = compound.getInt("fuel");
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
                if (player.getMainHandItem().is(ItemsRegistry.FUEL_BUCKET.get())) {
                    fillUpRocket(1000);
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
                } else {
                    this.openCustomInventoryScreen(player);
                }
                return InteractionResult.CONSUME;
            }

            this.doPlayerRide(player);
            return InteractionResult.CONSUME;
        }

        return result;
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity entity) {
        return this.position().add(this.getPassengerAttachmentPoint(entity, getDimensions(this.getPose()),1.0F)).add(0d,2,0d);
    }

    @Override
    public void openCustomInventoryScreen(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            MenuRegistry.openExtendedMenu(serverPlayer, new ExtendedMenuProvider() {
                @Override
                public void saveExtraData(FriendlyByteBuf packetByteBuf) {
                    packetByteBuf.writeVarInt(RocketEntity.this.getId());
                }

                @Override
                public Component getDisplayName() {
                    return Component.literal("Rocket");
                }

                @Override
                public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                    FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
                    packetBuffer.writeInt(RocketEntity.this.FUEL);
                    packetBuffer.writeVarInt(RocketEntity.this.getId());
                    return new RocketMenu(syncId, inv, inventory, RocketEntity.this.getId());
                }
            });
        }
    }

    @Override
    public void kill() {
        this.dropEquipment();
        this.spawnRocketItem();

        if (!this.level().isClientSide) {
            this.remove(RemovalReason.DISCARDED);
        }

    }


    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity sourceEntity = source.getEntity();

        if (sourceEntity != null && sourceEntity.isCrouching() && !this.isVehicle()) {

            this.spawnRocketItem();
            this.dropEquipment();

            if (!this.level().isClientSide) {
                this.remove(RemovalReason.DISCARDED);
            }

            return true;
        }

        return false;
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

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return this.getRocketItem();
    }


    public void spawnParticle() {
        if (this.level() instanceof ServerLevel level) {
            Vec3 vec = this.getDeltaMovement();

            if (START_TIMER == 200) {
                for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
                    level.sendParticles(player, (ParticleOptions) ParticleTypes.FLAME, true, this.getX() - vec.x, this.getY() - vec.y - 2.2, this.getZ() - vec.z, 20, 0.1, 0.1, 0.1, 0.001);
                    level.sendParticles(player, (ParticleOptions) ParticleTypes.FLAME, true, this.getX() - vec.x, this.getY() - vec.y - 3.2, this.getZ() - vec.z, 10, 0.1, 0.1, 0.1, 0.04);
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
            if (!this.ROCKET_START) {
                this.ROCKET_START = true;
                this.level().playSound(null, this, SoundRegistry.ROCKET_SOUND.get(), SoundSource.NEUTRAL, 1, 1);
                startTimerAndFlyMovement();
            }
            //} else {
            //    Methods.sendVehicleHasNoFuelMessage(player);
            //}
        }
    }


    public void startTimerAndFlyMovement() {
        if (START_TIMER < 200) {
            START_TIMER++;
        }

        if (START_TIMER == 200) {
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


    public void rocketExplosion() {
        if (START_TIMER == 200) {
            if (this.getDeltaMovement().y < -0.07) {
                destroyRocket(true);
            }
        }
    }
    public Player getFirstPlayerPassenger() {
        if (!this.getPassengers().isEmpty() && this.getPassengers().getFirst() instanceof Player player) {
            return player;
        }

        return null;
    }

    protected void dropEquipment() {
        for (int i = 0; i < this.inventory.getItems().size(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
                this.spawnAtLocation(itemstack);
            }
        }
    }
    public ItemStack getRocketItem() {
        ItemStack itemStack = new ItemStack(ItemsRegistry.ROCKET.get(), 1);
        RocketComponent rocketComponent = new RocketComponent(SKIN_UPGRADE.getRocketSkinLocation().toString(), RocketModel.fromString(MODEL_UPGRADE.getModel().toString()), MOTOR_UPGRADE.getFuelType(), TANK_UPGRADE.getTankCapacity(), FUEL);
        itemStack.set(DataComponentsRegistry.ROCKET_COMPONENT.get(), rocketComponent);
        return itemStack;
    }


    @Override
    public void containerChanged(Container container) {
        if (container.getItem(10).getItem() instanceof RocketUpgradeItem item) {
            this.MOTOR_UPGRADE = (MotorUpgrade) item.getUpgrade();
        }
        if (container.getItem(11).getItem() instanceof RocketUpgradeItem item) {
            this.TANK_UPGRADE = (TankUpgrade) item.getUpgrade();
        }
        if (container.getItem(12).getItem() instanceof RocketUpgradeItem item) {
            this.SKIN_UPGRADE = (SkinUpgrade) item.getUpgrade();
        }
        if (container.getItem(13).getItem() instanceof RocketUpgradeItem item) {
            this.MODEL_UPGRADE = (ModelUpgrade) item.getUpgrade();
            this.spawnRocketItem();
            this.dropEquipment();

            if (!this.level().isClientSide) {
                this.remove(RemovalReason.DISCARDED);
            }
        }

        if (inventory.getItem(0).is(FuelType.getFuelItem(MOTOR_UPGRADE.getFuelType()))) {
            fillUpRocket(1000);
        }
    }

    protected void doPlayerRide(Player player) {
        if (!this.level().isClientSide) {
            Vec3 entityPos = player.getPosition(0);
            player.setPosRaw(entityPos.x, entityPos.y + 40.0, entityPos.z);

            player.startRiding(this);

        }

    }

    private void checkContainer() {

    }

    public void fillUpRocket(int value) {
        if (FUEL == TANK_UPGRADE.getTankCapacity()) {
            return;
        }

        FUEL += value;
        if(FUEL > TANK_UPGRADE.getTankCapacity()) {
            FUEL = TANK_UPGRADE.getTankCapacity();
        }

        inventory.removeItem(0, 1);
        inventory.setItem(1, new ItemStack(Items.BUCKET, inventory.getItem(1).getCount()+1));
    }



    private void openPlanetMenu(Player player) {
        if(player == null) return;

        if(!player.getEntityData().get(EntityData.DATA_PLANET_MENU_OPEN)) {
            player.setNoGravity(true);
            player.getVehicle().setNoGravity(true);
            PlanetUtil.openPlanetSelectionMenu(player);
            player.getEntityData().set(EntityData.DATA_PLANET_MENU_OPEN, true);
        }

    }


    public void burnEntities() {
        if (START_TIMER == 200) {
            AABB aabb = AABB.ofSize(new Vec3(this.getX(), this.getY() - 2, this.getZ()), 2, 2, 2);
            List<LivingEntity> entities = this.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, aabb);

            for (LivingEntity entity : entities) {
                entity.setRemainingFireTicks(40);

            }
        }
    }
    protected void spawnRocketItem() {
        ItemEntity entityToSpawn = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.getRocketItem());
        entityToSpawn.setPickUpDelay(10);

        this.level().addFreshEntity(entityToSpawn);
    }

    public int getFuel() {
        return this.FUEL;
    }

    public Container getInventory() {
        return this.inventory;
    }

    public double getRocketSpeed() {
        return 0.8;
    }

    public String getFullSkinTexture() {
        String texture = SKIN_UPGRADE.getRocketSkinLocation().toString();
        if (MODEL_UPGRADE != null) {
            texture = texture.replace("normal", MODEL_UPGRADE.getModel().toString());
        }


        return texture;
    }

    public boolean canGoTo (Planet actual, Planet destination) {
        return Mth.abs(actual.distanceFromEarth() - destination.distanceFromEarth()) <= FuelType.getConsumptionBy100Kilometers(this.MOTOR_UPGRADE.getFuelType()) * this.TANK_UPGRADE.getTankCapacity();
    }
}
