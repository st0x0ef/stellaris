package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;


import java.util.function.Consumer;


public class AdvancementsRegister implements AdvancementSubProvider {


    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> writer) {
        AdvancementHolder moonAdvancement = Builder.advancement()
                .display((ItemLike) BlocksRegistry.MOON_SAND, Component.translatable("advancements.moon.root.title"), Component.translatable("advancements.moon.root.description"),
                        new ResourceLocation("textures/gui/advancements/backgrounds/stellaris.png"), AdvancementType.TASK, true, true, false)
                .addCriterion("land_on_the_moon", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Utils.getPlanetLevel(new ResourceLocation("moon"))))
                .save(writer, "moon/root");

        AdvancementHolder marsAdvancement = Builder.advancement()
                .display((ItemLike) BlocksRegistry.MARS_SAND, Component.translatable("advancements.mars.root.title"), Component.translatable("advancements.mars.root.description"),
                        null, AdvancementType.TASK, true, true, false)
                .addCriterion("land_on_the_mars", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Utils.getPlanetLevel(new ResourceLocation("mars"))))
                .save(writer, "mars/root");

        AdvancementHolder venusAdvancement = Builder.advancement()
                .display((ItemLike) BlocksRegistry.VENUS_SAND, Component.translatable("advancements.venus.root.title"), Component.translatable("advancements.venus.root.description"),
                        null, AdvancementType.TASK, true, true, false)
                .addCriterion("land_on_the_mars", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Utils.getPlanetLevel(new ResourceLocation("venus"))))
                .save(writer, "venus/root");







    }
}
