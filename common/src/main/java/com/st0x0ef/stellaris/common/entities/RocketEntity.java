package com.st0x0ef.stellaris.common.entities;

import com.google.common.collect.Sets;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.RocketModel;
import com.st0x0ef.stellaris.common.data_components.RocketComponent;
import com.st0x0ef.stellaris.common.items.upgrade.RocketModelItem;
import com.st0x0ef.stellaris.common.items.upgrade.RocketSkinItem;
import com.st0x0ef.stellaris.common.menus.RocketMenu;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.registry.EntityData;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import com.st0x0ef.stellaris.common.registry.SoundRegistry;
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
import net.minecraft.resources.ResourceLocation;
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

    public static final EntityDataAccessor<Integer> START_TIMER = SynchedEntityData.defineId(RocketEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> ROCKET_START = SynchedEntityData.defineId(RocketEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> ROCKET_SKIN = SynchedEntityData.defineId(RocketEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<RocketModel> ROCKET_MODEL = SynchedEntityData.defineId(RocketEntity.class, EntityData.ROCKET_MODEL);

    public static final EntityDataAccessor<Integer> FUEL = SynchedEntityData.defineId(RocketEntity.class, EntityDataSerializers.INT);
    public static final int MAX_FUEL = 10000;



    public static final String DEFAULT_SKIN_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/vehicle/rocket_skin/normal/standard.png").toString();

    protected SimpleContainer inventory;

    public RocketEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.entityData.set(START_TIMER, 0);
        this.entityData.set(ROCKET_START, false);
        this.entityData.set(ROCKET_SKIN, DEFAULT_SKIN_TEXTURE);
        this.entityData.set(FUEL, 0);
        this.entityData.set(ROCKET_MODEL, RocketModel.NORMAL);

        this.inventory = new SimpleContainer(14);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder
                .define(ROCKET_START, false)
                .define(START_TIMER, 0)
                .define(ROCKET_SKIN, DEFAULT_SKIN_TEXTURE)
                .define(FUEL, 0)
                .define(ROCKET_MODEL, RocketModel.NORMAL)
                .build();

    }

    @Override
    public void tick() {
        super.tick();
//        this.rotateRocket();
//        this.checkOnBlocks();

        this.rocketExplosion();
        this.burnEntities();
        this.checkContainer();

        if (this.entityData.get(ROCKET_START)) {
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
        compound.putBoolean("rocket_start", this.getEntityData().get(ROCKET_START));
        compound.putInt("start_timer", this.getEntityData().get(START_TIMER));
        compound.putInt("fuel", this.getEntityData().get(FUEL));
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        ListTag inventoryCustom = compound.getList("InventoryCustom", 10);
        this.inventory.fromTag(inventoryCustom, registryAccess());

        this.getEntityData().set(ROCKET_START, compound.getBoolean("rocket_start"));
        this.getEntityData().set(START_TIMER, compound.getInt("start_timer"));
        this.getEntityData().set(FUEL, compound.getInt("fuel"));
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
        return this.position().add(this.getPassengerAttachmentPoint(entity, getDimensions(this.getPose()),1.0F)).subtract(0d,3.15d,0d);
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
                    packetBuffer.writeInt(RocketEntity.this.getEntityData().get(FUEL));
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

            if (this.entityData.get(START_TIMER) == 200) {
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
            if (!data.get(RocketEntity.ROCKET_START)) {
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


    public void rocketExplosion() {
        if (this.entityData.get(START_TIMER) == 200) {
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
        RocketComponent rocketComponent = new RocketComponent(this.getEntityData().get(ROCKET_SKIN), this.getEntityData().get(ROCKET_MODEL), this.getEntityData().get(FUEL));
        itemStack.set(DataComponentsRegistry.ROCKET_COMPONENT.get(), rocketComponent);

        return itemStack;
    }


    @Override
    public void containerChanged(Container container) {
        container.setItem(1, new ItemStack(ItemsRegistry.STEEL_NUGGET));
        Stellaris.LOG.error("Container Change ");
    }

    protected void doPlayerRide(Player player) {
        if (!this.level().isClientSide) {
            Vec3 entityPos = player.getPosition(0);
            player.setPosRaw(entityPos.x, entityPos.y + 40.0, entityPos.z);

            player.startRiding(this);

        }

    }

    private void checkContainer() {
        if (inventory.getItem(12).getItem() instanceof RocketSkinItem item) {
            this.getEntityData().set(ROCKET_SKIN, item.getRocketSkinName().toString());
        }

        if (inventory.getItem(13).getItem() instanceof RocketModelItem item) {
            this.getEntityData().set(ROCKET_MODEL, item.getModel());
            this.spawnRocketItem();
            this.dropEquipment();

            if (!this.level().isClientSide) {
                this.remove(RemovalReason.DISCARDED);
            }


        }

        if (inventory.getItem(0).is(ItemsRegistry.FUEL_BUCKET.get())) {
            fillUpRocket(1000);
        }
    }

    public void fillUpRocket(int value) {
        int oldFuel = this.getEntityData().get(FUEL);
        if (oldFuel==MAX_FUEL) {
            return;
        }
        int newFuel = oldFuel + value;
        if(newFuel <= MAX_FUEL) {
            this.getEntityData().set(FUEL, newFuel);
        } else {
            this.getEntityData().set(FUEL, MAX_FUEL);
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
        if (this.entityData.get(START_TIMER) == 200) {
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
        return this.getEntityData().get(FUEL);
    }

    public Container getInventory() {
        return this.inventory;
    }

    public void setSkinTexture(String texture) {
        this.getEntityData().set(ROCKET_SKIN, texture);
    }

    public String getSkinTexture() {
        return this.getEntityData().get(ROCKET_SKIN);
    }

    public double getRocketSpeed() {
        return Mth.clamp(0.65 * 1, 0.7, 1.5);
    }

    public String getFullSkinTexture() {
        String texture = this.getSkinTexture();
        texture = texture.replace("normal", this.getEntityData().get(ROCKET_MODEL).toString());
        return texture;
    }

}
