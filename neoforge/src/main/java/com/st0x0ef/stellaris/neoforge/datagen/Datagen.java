package com.st0x0ef.stellaris.neoforge.datagen;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.world.ModConfiguredFeature;
import com.st0x0ef.stellaris.common.world.ModPlacedFeatures;
import com.st0x0ef.stellaris.neoforge.datagen.providers.TabletEntryProvider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;

@EventBusSubscriber(modid = Stellaris.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Datagen {

    @SubscribeEvent
    static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

//        generator.addProvider(
//                event.includeServer(),
//                (DataProvider.Factory<DatapackBuiltinEntriesProvider>) packOutput -> new DatapackBuiltinEntriesProvider(
//                        packOutput,
//                        event.getLookupProvider(),
//                        new RegistrySetBuilder()
//                                .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeature::bootstrap)
//                                .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap),
//                        Set.of(Stellaris.MODID)
//                )
//        );

        generator.addProvider(
                event.includeClient(),
                new TabletEntryProvider(output)
        );



    }
}