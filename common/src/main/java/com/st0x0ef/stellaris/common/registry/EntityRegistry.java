package com.st0x0ef.stellaris.common.registry;

import com.google.common.base.Suppliers;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.*;
import com.st0x0ef.stellaris.common.entities.alien.Alien;
import com.st0x0ef.stellaris.common.entities.pygro.Pygro;
import com.st0x0ef.stellaris.common.entities.pygro.PygroMobsSensor;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.sensing.SensorType;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

//For everything related to entities
public class EntityRegistry {
    //Entity type

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(Stellaris.MODID, Registries.ENTITY_TYPE);
    public static final RegistrySupplier<EntityType<Alien>> ALIEN = ENTITY_TYPE.register("alien",
            Suppliers.memoize(() -> EntityType.Builder.of(Alien::new, MobCategory.MONSTER).sized(0.75f, 2.5f).build(new ResourceLocation(Stellaris.MODID, "alien").toString())));
    public static final RegistrySupplier<EntityType<AlienZombie>> ALIEN_ZOMBIE = ENTITY_TYPE.register("alien_zombie",
            Suppliers.memoize(() -> EntityType.Builder.of(AlienZombie::new, MobCategory.MONSTER).sized(0.6f, 2.4f).build(new ResourceLocation(Stellaris.MODID, "alien_zombie").toString())));
    public static final RegistrySupplier<EntityType<MartianRaptor>> MARTIAN_RAPTOR = ENTITY_TYPE.register("martian_raptor",
            Suppliers.memoize(() -> EntityType.Builder.of(MartianRaptor::new, MobCategory.MONSTER).sized(0.75f, 2.0f).build(new ResourceLocation(Stellaris.MODID, "martian_raptor").toString())));
    public static final RegistrySupplier<EntityType<Pygro>> PYGRO = ENTITY_TYPE.register("pygro",
            Suppliers.memoize(() -> EntityType.Builder.of(Pygro::new, MobCategory.MONSTER).sized(0.6f, 1.8f).build(new ResourceLocation(Stellaris.MODID, "pygro").toString())));
    public static final RegistrySupplier<EntityType<PygroBrute>> PYGRO_BRUTE = ENTITY_TYPE.register("pygro_brute",
            Suppliers.memoize(() -> EntityType.Builder.of(PygroBrute::new, MobCategory.MONSTER).sized(0.6f, 1.8f).build(new ResourceLocation(Stellaris.MODID, "pygro_brute").toString())));
    public static final RegistrySupplier<EntityType<Mogler>> MOGLER = ENTITY_TYPE.register("mogler",
            Suppliers.memoize(() -> EntityType.Builder.of(Mogler::new, MobCategory.MONSTER).sized(1.4f, 1.4f).build(new ResourceLocation(Stellaris.MODID, "mogler").toString())));
    public static final RegistrySupplier<EntityType<StarCrawler>> STAR_CRAWLER = ENTITY_TYPE.register("star_crawler",
            Suppliers.memoize(() -> EntityType.Builder.of(StarCrawler::new, MobCategory.MONSTER).sized(1.3f, 1.0f).build(new ResourceLocation(Stellaris.MODID, "star_crawler").toString())));
    public static final RegistrySupplier<EntityType<? extends IceSpit>> ICE_SPIT = ENTITY_TYPE.register("ice_spit",
            Suppliers.memoize(() -> EntityType.Builder.of(IceSpit::new, MobCategory.MISC).sized(0.5f, 0.5f).build(new ResourceLocation(Stellaris.MODID, "ice_spit").toString())));
    public static final RegistrySupplier<EntityType<IceShardArrowEntity>> ICE_SHARD_ARROW = ENTITY_TYPE.register("ice_shard_arrow",
            Suppliers.memoize(() -> EntityType.Builder.<IceShardArrowEntity>of(IceShardArrowEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(new ResourceLocation(Stellaris.MODID, "ice_shard_arrow").toString())));
    public static final RegistrySupplier<EntityType<CheeseBoss>> CHEESE_BOSS = ENTITY_TYPE.register("cheese_boss",
            Suppliers.memoize(() -> EntityType.Builder.of(CheeseBoss::new, MobCategory.MONSTER).sized(1.0f, 3.7f).build(new ResourceLocation(Stellaris.MODID, "cheese_boss").toString())));

    /**
     * Vehicles
     */
    public static final RegistrySupplier<EntityType<RocketEntity>> TINY_ROCKET = ENTITY_TYPE.register("tiny_rocket", () -> EntityType.Builder.of(RocketEntity::new, MobCategory.MISC).sized(1.1f, 4.4f).fireImmune().build(new ResourceLocation(Stellaris.MODID, "tiny_rocket").toString()));
    public static final RegistrySupplier<EntityType<RocketEntity>> BIG_ROCKET = ENTITY_TYPE.register("big_rocket", () -> EntityType.Builder.of(RocketEntity::new, MobCategory.MISC).sized(1.1f, 4.4f).fireImmune().build(new ResourceLocation(Stellaris.MODID, "big_rocket").toString()));

    public static final RegistrySupplier<EntityType<RocketEntity>> NORMAL_ROCKET = ENTITY_TYPE.register("normal_rocket",
            () -> EntityType.Builder.of(RocketEntity::new, MobCategory.MISC).sized(1.1f, 4.4f).build(new ResourceLocation(Stellaris.MODID, "normal_rocket").toString()));
    public static final RegistrySupplier<EntityType<RocketEntity>> SMALL_ROCKET = ENTITY_TYPE.register("small_rocket",
            () -> EntityType.Builder.of(RocketEntity::new, MobCategory.MISC).sized(0.8f, 3.2f).build(new ResourceLocation(Stellaris.MODID, "small_rocket").toString()));

    public static final RegistrySupplier<EntityType<LanderEntity>> LANDER = ENTITY_TYPE.register("lander",
            () -> EntityType.Builder.<LanderEntity>of(LanderEntity::new, MobCategory.MISC).sized(2.5f, 1.0f).build(new ResourceLocation(Stellaris.MODID, "lander").toString()));

    //Entity Attributes
    public static void registerAttributes(BiConsumer<Supplier<? extends EntityType<? extends LivingEntity>>, Supplier<AttributeSupplier.Builder>> attributes) {
        attributes.accept(EntityRegistry.ALIEN,  Alien::setCustomAttributes);
        attributes.accept(EntityRegistry.ALIEN_ZOMBIE,  AlienZombie::setCustomAttributes);
        attributes.accept(EntityRegistry.MARTIAN_RAPTOR,  MartianRaptor::CreateRaptorAttributes);
        attributes.accept(EntityRegistry.PYGRO_BRUTE, PygroBrute::setCustomAttributes);
        attributes.accept(EntityRegistry.PYGRO, Pygro::setCustomAttributes);
        attributes.accept(EntityRegistry.MOGLER, Mogler::setCustomAttributes);
        attributes.accept(EntityRegistry.STAR_CRAWLER, StarCrawler::setCustomAttributes);
        attributes.accept(EntityRegistry.CHEESE_BOSS, CheeseBoss::setCustomAttributes);
    }

    //Entity Sensor
    public static final DeferredRegister<SensorType<?>> SENSOR = DeferredRegister.create(Stellaris.MODID, Registries.SENSOR_TYPE);
    public static final RegistrySupplier<SensorType<PygroMobsSensor>> PYGRO_SENSOR = SENSOR.register("pygro_sensor", ()-> new SensorType<>(PygroMobsSensor::new));
}
