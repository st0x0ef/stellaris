package com.st0x0ef.stellaris.neoforge.datagen.providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.tablet.TabletEntry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class TabletEntryProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;
    private String modid = Stellaris.MODID;
    private final Codec<TabletEntry> codec;
    private final ResourceKey<Registry<TabletEntry>> registry = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(modid, "renderer/tablet"));

    public TabletEntryProvider(PackOutput packOutput) {
        this(packOutput, PackOutput.Target.RESOURCE_PACK, Stellaris.MODID);
    }

    public TabletEntryProvider(PackOutput packOutput, String modid) {
        this(packOutput, PackOutput.Target.RESOURCE_PACK, modid);
    }

    public TabletEntryProvider(PackOutput packOutput, PackOutput.Target target, String modid) {
        this.modid = modid;
        this.pathProvider = packOutput.createPathProvider(target, registry.location().getPath());
        this.codec = TabletEntry.CODEC;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        build((key, value) ->
                futures.add(DataProvider.saveStable(
                        output,
                        codec.encodeStart(JsonOps.INSTANCE, value).getOrThrow(),
                        pathProvider.json(key)
                ))
        );

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public String getName() {
        return "Tablet Entry Provider";
    }

    protected void build(BiConsumer<ResourceLocation, TabletEntry> consumer) {
        consumer.accept(entryLocation("items"), new TabletEntry("items", "Items and their properties", this.entryLocation("textures/gui/tablet/book_page.png"), this.entryLocation("textures/gui/tablet/book_page_hover.png"), List.of(
                new TabletEntry.ItemInfo("oil_finder", "Oil Finder", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("Right Click on the ground to find oil."), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.OIL_FINDER), 32, Optional.empty())), Optional.empty())
                )),
                new TabletEntry.ItemInfo("cans", "Cans", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("Cans are items that can store food. There are two differents types of cans: [br] - Small: can contain maximum 10 nutrition [br] - Regular: can contain maximum 20 nutrition [br] [br] How to fill them: [br] In the [ref=items:vacuumator]Vacuumator, in the three slots at the top place a can, food, and an empty glass bottle. You will gain a can filled with your food and a water bottle."), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.BIG_BLUE_CAN), 32, Optional.empty())), Optional.empty()),
                        new TabletEntry.InfoComponent("image", Optional.empty(), Optional.of(new TabletEntry.ImageComponent(entryLocation("textures/gui/tablet/images/vacuumator_recipe"), 136, 97)), Optional.empty(), Optional.empty())
                )),
                new TabletEntry.ItemInfo("water_separator", "Water Separator", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("The water separator transform water into Oxygen and Hydrogen [br] [br] Need Energy ⚡ [br] [br] The machine works by putting water in the second slot and it will generate Hydrogen and Oxygen that you can gather by placing buckets under the tanks."), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.WATER_SEPARATOR), 32, Optional.empty())), Optional.empty()),
                        new TabletEntry.InfoComponent("image", Optional.empty(), Optional.of(new TabletEntry.ImageComponent(entryLocation("textures/gui/tablet/images/water_separator"), 128, 64)), Optional.empty(), Optional.empty())
                )),
                new TabletEntry.ItemInfo("vacuumator", "Vacuumator", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("The Vacuumator allow you to put food inside Cans that you can eat in space. [br] [br] For this machine to work you need to put a can in the first slot, food in the second and an empty glass bottle in the third. The machine will give you a can with food inside and a water bottle. [br] [br]"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.VACUMATOR), 32, Optional.empty())), Optional.empty()),
                        new TabletEntry.InfoComponent("image", Optional.empty(), Optional.of(new TabletEntry.ImageComponent(entryLocation("textures/gui/tablet/images/vacuumator"), 128, 64)), Optional.empty(), Optional.empty())

                )),
                new TabletEntry.ItemInfo("fuel_refinery", "Fuel Refinery", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("The Fuel [ref=mobs:alien2]Refinery is very important. It allow you to [ref=mobs:alien]create fuel [br] [br] Need Energy ⚡ [br] [br] To create Fuel, you need to input oil in the first slot and it will generate fuel that you can get by putting a bucket in the last slot"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.FUEL_REFINERY), 32, Optional.empty())), Optional.empty()),
                        new TabletEntry.InfoComponent("image", Optional.empty(), Optional.of(new TabletEntry.ImageComponent(entryLocation("textures/gui/tablet/images/fuel_refinery"), 128, 64)), Optional.empty(), Optional.empty())
                )),
                new TabletEntry.ItemInfo("solar_panel", "Solar Panel", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("The Solar Panel can generate energy by being under the sun during the day. [br] [br] Max Generation : [color=green]1Fe/Tick."), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.SOLAR_PANEL), 32, Optional.empty())), Optional.empty())

                )),
                new TabletEntry.ItemInfo("radioactive_generator", "Radioactive Generator", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("The Radioactive Generator can generate energy by burning [ref=items:radioactive]radioactive items like uranium ingot or radioactive block. [br] [br] Max Generation : [color=red]500Fe/Tick."), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.RADIOACTIVE_GENERATOR), 32, Optional.empty())), Optional.empty())

                )),
                new TabletEntry.ItemInfo("oxygen_distributor", "Oxygen Distributor", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("The Oxygen Distributor allow you to fill a room with oxygen. [br] For this, place a full an [ref=items:oxygen_tank]oxygen [ref=items:oxygen_tank]tank with oxygen with the [ref=items:water_separator]Water [ref=items:water_separator]Separator. [br] [br] Need Energy [color=yellow]⚡ ."), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.OXYGEN_DISTRIBUTOR), 32, Optional.empty())), Optional.empty())

                )),
                new TabletEntry.ItemInfo("pumpjack", "Pumpjack", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("The Pumpjack allow you to get the oil that is in the chunk. [br] First, check with the [ref=items:oil_finder]Oil [ref=items:oil_finder]Finder if oil is in the chunk. Then place the Pumpjack and start pumping oil. [br] [br] Need Energy [color=yellow]⚡ ."), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.PUMPJACK), 32, Optional.empty())), Optional.empty())
                )),
                new TabletEntry.ItemInfo("cable", "Cable", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("Cable transfers energy from one machine to another. [br] [br] Need Energy [color=yellow]⚡ "), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.T1_CABLE), 32, Optional.empty())), Optional.empty())
                ))
        )));


        consumer.accept(entryLocation("mobs"), new TabletEntry("mobs", "Discover all the mobs of Stellaris.", this.entryLocation("textures/gui/tablet/rocket_page.png"), this.entryLocation("textures/gui/tablet/rocket_page_hover.png"), List.of(
                new TabletEntry.ItemInfo("alien", "Alien", "entity", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("This is an [color=yellow]Alien. This passive mob can be found on the moon [br] [br] Stats : [br] - Health : 20 [br] - Speed : 0.5"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("entity", Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.EntityComponent(entryLocation("alien"), 25)))

                )),
                new TabletEntry.ItemInfo("alien_zombie", "Alien Zombie", "entity", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("This is an [color=yellow]Alien [color=yellow]Zombie. A ancient force corrupt this alien and now it's a zombie. This aggressive mob can be found on the moon [br] [br] Stats : [br] - Health : 20 [br] - Speed : 0.3 [br] - Damage : 3"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("entity", Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.EntityComponent(entryLocation("alien_zombie"), 25)))

                )),
                new TabletEntry.ItemInfo("star_crawler", "Star Crawler", "entity", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("This is a [color=yellow]Star [color=yellow]Crawler. This monster mob can be found on the moon [br] [br] Stats : [br] - Health : 40 [br] - Speed : 0.4 [br] - Damage : 9"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("entity", Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.EntityComponent(entryLocation("star_crawler"), 25)))

                )),
                new TabletEntry.ItemInfo("martian_raptor", "Martian Raptor", "entity", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("This is a [color=yellow]Martian [color=yellow]Raptor. This creature live on Mars [br] [br] Stats : [br] - Health : 20 [br] - Speed : 0.3 [br] - Damage : 3"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("entity", Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.EntityComponent(entryLocation("martian_raptor"), 25)))

                )),
                new TabletEntry.ItemInfo("pygro", "Pygro", "entity", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("This is a [color=yellow]Pygro. [br] [br] Stats : [br] - Health : 16 [br] - Speed : 0.35 [br]"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("entity", Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.EntityComponent(entryLocation("pygro"), 25)))

                )),
                new TabletEntry.ItemInfo("pygro_brute", "Pygro Brute", "entity", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("This is a [color=yellow]Pygro [color=yellow]Brute. [br] [br] Stats : [br] - Health : 16 [br] - Speed : 0.35 [br] - Damage : 5"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("entity", Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.EntityComponent(entryLocation("pygro_brute"), 25)))

                )),
                new TabletEntry.ItemInfo("mogler", "Mogler", "entity", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("This is a Mogler. This aggressive creature live on venus.  [br] [br] Stats : [br] - Health : 40 [br] - Speed : 0.3 [br] - Damage : 6"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("entity", Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.EntityComponent(entryLocation("mogler"), 25)))

                ))
        )));

        consumer.accept(entryLocation("planets"), new TabletEntry("planets", "Planets", this.entryLocation("textures/gui/tablet/planets_page.png"), this.entryLocation("textures/gui/tablet/planets_page_hover.png"), List.of(
                new TabletEntry.ItemInfo("mercury", "Mercury", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("Mercury is a planet that is very close to the sun. It is very hot and has a lot of lava. [br] [br] Temperature : [color=red]430.0°c [br] Gravity : [color=red]3.7 [br] Oxygen : [color=red]false [br] Distance From Earth : 92000km [br] [br] Mobs [br] - [ref=mobs:mogler]Mogler [br] [br] Resources : [br] - [ref=items:uranium]Uranium [br] - Iron"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.MERCURY_GLOBE_ITEM), 32, Optional.empty())), Optional.empty())

                )),
                new TabletEntry.ItemInfo("earth", "The Earth", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("The Earth AKA The planet Blue AKA The overworld. It is a very beautiful planet with a lot of life. [br] [br] Temperature : [color=green]15.0°c [br] Gravity : [color=green]9.8 [br] Oxygen : [color=green]true [br] Distance From Earth : 0km [br] [br] Moon : [ref=planets:moon]The [ref=planets:moon]Moon"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.EARTH_GLOBE_ITEM), 32, Optional.empty())), Optional.empty())
                )),
                new TabletEntry.ItemInfo("mars", "Mars", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("Mars is a planet that is very cold and has a lot of ice. [br] [br] Temperature : [color=blue]-63.0°c [br] Gravity : [color=blue]3.7 [br] Oxygen : [color=blue]false [br] Distance From Earth : 55700km [br] [br] Mobs : [br] - [ref=mobs:martian_raptor]Martian [ref=mobs:martian_raptor]Raptor [br] [br] Resources : [br] - [ref=items:plutonium]Plutonium [br] - Diamond [br] - [ref=items:ice_shard]Ice [ref=items:ice_shard]Shard [br] - Iron [br] - [ref=items:ostrum]Ostrum"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.MARS_GLOBE_ITEM), 32, Optional.empty())), Optional.empty())

                )),
                new TabletEntry.ItemInfo("venus", "Venus", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("Venus is a planet that is very hot and has a lot of lava. [br] [br] Temperature : [color=red]470.0°c [br] Gravity : [color=red]8.9 [br] Oxygen : [color=red]false [br] Distance From Earth : 41000km [br] [br] Mobs : [br] - [ref=mobs:pygro]Pygro [br] - [ref=mobs:mogler]Mogler [br] - [ref=mobs:pygro_brute]Pygro [ref=mobs:pygro_brute]Brute [br] [br] Resources : [br] - [ref=items:neptunium]Neptunium [br] - Coal [br] - Diamond"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.VENUS_GLOBE_ITEM), 32, Optional.empty())), Optional.empty())

                        )),
                new TabletEntry.ItemInfo("moon", "Moon", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("The Moon is a satellite of the Earth. It is a very cold place with empty desert of moon sand. Please don't steal it. [br] [br] Temperature : [color=blue]-63.0°c [br] Gravity : [color=blue]1.6 [br] Oxygen : [color=blue]false [br] Distance From Earth : 384km [br] [br] Mobs : [br] - [ref=mobs:alien]Alien [br] - [ref=mobs:alien_zombie]Alien [ref=mobs:alien_zombie]Zombie [br] - [ref=mobs:star_crawler]Star [ref=mobs:star_crawler]Crawler [br] [br] Resources : [br] - [ref=items:desh]Desh [br] - [ref=items:ice_shard]Ice [ref=items:ice_shard]Shard [br] - Iron"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.MOON_GLOBE_ITEM), 32, Optional.empty())), Optional.empty())

                ))
        )));

        consumer.accept(entryLocation("rocket"), new TabletEntry("rocket", "Learn about rockets and how to build them !", entryLocation("textures/gui/tablet/rocket_page.png"), entryLocation("textures/gui/tablet/rocket_page_hover.png"), List.of(
                new TabletEntry.ItemInfo("rocket_station", "Rocket Station", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("One of the first block you need, is the Rocket Station. This allow you to build the rocket with all the parts needed."), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.ROCKET_STATION), 32, Optional.empty())), Optional.empty()),
                        new TabletEntry.InfoComponent("image", Optional.empty(), Optional.of(new TabletEntry.ImageComponent(entryLocation("textures/gui/tablet/images/rocket_station"), 128, 64)), Optional.empty(), Optional.empty())
                )),
                new TabletEntry.ItemInfo("tiny_rocket", "Tiny Rocket Model", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("This is the basic rocket. [br] [br] Can transport 1 entity"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.TINY_ROCKET_UPGRADE), 32, Optional.empty())), Optional.empty()),
                        new TabletEntry.InfoComponent("entity", Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.EntityComponent(this.entryLocation("tiny_rocket"), 10)))
                )),
                new TabletEntry.ItemInfo("small_rocket", "Small Rocket Model", "item", List.of(
                        new TabletEntry.InfoComponent("text", Optional.of("This is the small rocket. [br] [br] Can transport 2 entity"), Optional.empty(), Optional.empty(), Optional.empty()),
                        new TabletEntry.InfoComponent("item", Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.ItemComponent(new ItemStack(ItemsRegistry.SMALL_ROCKET_UPGRADE), 32, Optional.empty())), Optional.empty()),
                        new TabletEntry.InfoComponent("entity", Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(new TabletEntry.EntityComponent(this.entryLocation("small_rocket"), 10)))
                ))
        )));

    }

    public ResourceLocation entryLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, path);
    }

}
