package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.entities.GlobeTileEntity;
import com.st0x0ef.stellaris.common.blocks.entities.RadioactiveBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.*;
import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenPropagatorBlockEntity;
import com.st0x0ef.stellaris.common.entities.*;
import com.st0x0ef.stellaris.common.entities.alien.Alien;
import com.st0x0ef.stellaris.common.entities.pygro.Pygro;
import com.st0x0ef.stellaris.common.entities.pygro.PygroMobsSensor;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.block.entity.BlockEntityType;

//For everything related to entities
public class EntityRegistry {
    //Block entity type
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = DeferredRegister.create(Stellaris.MODID, Registries.BLOCK_ENTITY_TYPE);
    public static final RegistrySupplier<BlockEntityType<RocketStationEntity>> ROCKET_STATION = BLOCK_ENTITY_TYPE.register("rocket_station",
            () -> BlockEntityType.Builder.of(RocketStationEntity::new, BlocksRegistry.ROCKET_STATION.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<RadioactiveBlockEntity>> RADIOACTIVE_BLOCK = BLOCK_ENTITY_TYPE.register("radioactive_block",
            () -> BlockEntityType.Builder.of(RadioactiveBlockEntity::new,
                    BlocksRegistry.URANIUM_BLOCK.get(),
                    BlocksRegistry.RAW_URANIUM_BLOCK.get(),
                    BlocksRegistry.MERCURY_URANIUM_ORE.get(),
                    BlocksRegistry.PLUTONIUM_BLOCK.get(),
                    BlocksRegistry.NEPTUNIUM_BLOCK.get()
                    ).build(null));
    public static final RegistrySupplier<BlockEntityType<GlobeTileEntity>> GLOBE_BLOCK_ENTITY = BLOCK_ENTITY_TYPE.register("globe", () -> BlockEntityType.Builder.of(GlobeTileEntity::new,
            BlocksRegistry.EARTH_GLOBE_BLOCK.get(),
            BlocksRegistry.MOON_GLOBE_BLOCK.get(),
            BlocksRegistry.MARS_GLOBE_BLOCK.get(),
            BlocksRegistry.MERCURY_GLOBE_BLOCK.get(),
            BlocksRegistry.VENUS_GLOBE_BLOCK.get())
            .build(null));

    public static final RegistrySupplier<BlockEntityType<BaseEnergyBlockEntity>> TEST_BLOCK = BLOCK_ENTITY_TYPE.register("test_block",
            () -> BlockEntityType.Builder.of(BaseEnergyBlockEntity::new, BlocksRegistry.TEST_BLOCK.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SOLAR_PANEL = BLOCK_ENTITY_TYPE.register("solar_panel",
            () -> BlockEntityType.Builder.of(SolarPanelEntity::new, BlocksRegistry.SOLAR_PANEL.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CoalGeneratorEntity>> COAL_GENERATOR = BLOCK_ENTITY_TYPE.register("coal_generator",
            () -> BlockEntityType.Builder.of(CoalGeneratorEntity::new, BlocksRegistry.COAL_GENERATOR.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<RadioactiveGeneratorEntity>> RADIOACTIVE_GENERATOR = BLOCK_ENTITY_TYPE.register("radioactive_generator",
            () -> BlockEntityType.Builder.of(RadioactiveGeneratorEntity::new, BlocksRegistry.RADIOACTIVE_GENERATOR.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CableBlockEntity>> CABLE_ENTITY = BLOCK_ENTITY_TYPE.register("cable",
            () -> BlockEntityType.Builder.of(CableBlockEntity::new, BlocksRegistry.CABLE.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<VacumatorBlockEntity>> VACUMATOR_ENTITY = BLOCK_ENTITY_TYPE.register("vacumator",
            () -> BlockEntityType.Builder.of(VacumatorBlockEntity::new, BlocksRegistry.VACUMATOR.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<WaterSeparatorBlockEntity>> WATER_SEPARATOR_ENTITY = BLOCK_ENTITY_TYPE.register("water_separator",
            () -> BlockEntityType.Builder.of(WaterSeparatorBlockEntity::new, BlocksRegistry.WATER_SEPARATOR.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<OxygenPropagatorBlockEntity>> OXYGEN_PROPAGATOR = BLOCK_ENTITY_TYPE.register("oxygen_propagator",
            () -> BlockEntityType.Builder.of(OxygenPropagatorBlockEntity::new, BlocksRegistry.OXYGEN_PROPAGATOR.get()).build(null));

    //Entity type

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(Stellaris.MODID, Registries.ENTITY_TYPE);
    public static final RegistrySupplier<EntityType<Alien>> ALIEN = ENTITY_TYPE.register("alien",
            () -> EntityType.Builder.of(Alien::new, MobCategory.MONSTER).sized(0.75f, 2.5f).build(new ResourceLocation(Stellaris.MODID, "alien").toString()));
    public static final RegistrySupplier<EntityType<AlienZombie>> ALIEN_ZOMBIE = ENTITY_TYPE.register("alien_zombie",
            () -> EntityType.Builder.of(AlienZombie::new, MobCategory.MONSTER).sized(0.6f, 2.4f).build(new ResourceLocation(Stellaris.MODID, "alien_zombie").toString()));
    public static final RegistrySupplier<EntityType<MartianRaptor>> MARTIAN_RAPTOR = ENTITY_TYPE.register("martian_raptor",
        () -> EntityType.Builder.of(MartianRaptor::new, MobCategory.MONSTER).sized(0.75f, 2.0f).build(new ResourceLocation(Stellaris.MODID, "martian_raptor").toString()));
    public static final RegistrySupplier<EntityType<Pygro>> PYGRO = ENTITY_TYPE.register("pygro",
            () -> EntityType.Builder.of(Pygro::new, MobCategory.MONSTER).sized(0.6f, 1.8f).build(new ResourceLocation(Stellaris.MODID, "pygro").toString()));
    public static final RegistrySupplier<EntityType<PygroBrute>> PYGRO_BRUTE = ENTITY_TYPE.register("pygro_brute",
            () -> EntityType.Builder.of(PygroBrute::new, MobCategory.MONSTER).sized(0.6f, 1.8f).build(new ResourceLocation(Stellaris.MODID, "pygro_brute").toString()));
    public static final RegistrySupplier<EntityType<Mogler>> MOGLER = ENTITY_TYPE.register("mogler",
            () -> EntityType.Builder.of(Mogler::new, MobCategory.MONSTER).sized(1.4f, 1.4f).build(new ResourceLocation(Stellaris.MODID, "mogler").toString()));
    public static final RegistrySupplier<EntityType<StarCrawler>> STAR_CRAWLER = ENTITY_TYPE.register("star_crawler",
            () -> EntityType.Builder.of(StarCrawler::new, MobCategory.MONSTER).sized(1.3f, 1.0f).build(new ResourceLocation(Stellaris.MODID, "star_crawler").toString()));
    public static final RegistrySupplier<EntityType<? extends IceSpit>> ICE_SPIT = ENTITY_TYPE.register("ice_spit",
            () -> EntityType.Builder.of(IceSpit::new, MobCategory.MISC).sized(0.5f, 0.5f).build(new ResourceLocation(Stellaris.MODID, "ice_spit").toString()));
    public static final RegistrySupplier<EntityType<IceShardArrowEntity>> ICE_SHARD_ARROW = ENTITY_TYPE.register("ice_shard_arrow",
            () -> EntityType.Builder.<IceShardArrowEntity>of(IceShardArrowEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(new ResourceLocation(Stellaris.MODID, "ice_shard_arrow").toString()));

    /**
     * Vehicles
     */
    public static final RegistrySupplier<EntityType<RocketEntity>> ROCKET = ENTITY_TYPE.register("rocket",
            () -> EntityType.Builder.of(RocketEntity::new, MobCategory.MISC).sized(1.1f, 4.4f).build(new ResourceLocation(Stellaris.MODID, "rocket").toString()));
    public static final RegistrySupplier<EntityType<LanderEntity>> LANDER = ENTITY_TYPE.register("lander",
            () -> EntityType.Builder.<LanderEntity>of(LanderEntity::new, MobCategory.MISC).sized(2.5f, 1.0f).build(new ResourceLocation(Stellaris.MODID, "rocket").toString()));

    //Entity Attributes
    public static void registerAttributes() {
        EntityAttributeRegistry.register(EntityRegistry.ALIEN,  Alien::setCustomAttributes);
        EntityAttributeRegistry.register(EntityRegistry.ALIEN_ZOMBIE,  AlienZombie::setCustomAttributes);
        EntityAttributeRegistry.register(EntityRegistry.MARTIAN_RAPTOR,  MartianRaptor::CreateRaptorAttributes);
        EntityAttributeRegistry.register(EntityRegistry.MARTIAN_RAPTOR,  MartianRaptor::CreateRaptorAttributes);
        EntityAttributeRegistry.register(EntityRegistry.PYGRO_BRUTE, PygroBrute::setCustomAttributes);
        EntityAttributeRegistry.register(EntityRegistry.PYGRO, Pygro::setCustomAttributes);
        EntityAttributeRegistry.register(EntityRegistry.MOGLER, Mogler::setCustomAttributes);
        EntityAttributeRegistry.register(EntityRegistry.STAR_CRAWLER, StarCrawler::setCustomAttributes);
    }



    //Entity Sensor
    public static final DeferredRegister<SensorType<?>> SENSOR = DeferredRegister.create(Stellaris.MODID, Registries.SENSOR_TYPE);
    public static final RegistrySupplier<SensorType<PygroMobsSensor>> PYGRO_SENSOR = SENSOR.register("pygro_sensor", ()-> new SensorType<>(PygroMobsSensor::new));


}
