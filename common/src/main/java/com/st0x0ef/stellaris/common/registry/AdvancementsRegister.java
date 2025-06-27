package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class AdvancementsRegister implements AdvancementSubProvider {

    public static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocationUtils.guiTexture("advancements/backgrounds/stellaris");

    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> writer) {
        AdvancementHolder advancementHolder = Builder.advancement().display((ItemLike) BlocksRegistry.MOON_SAND, Component.translatable("advancements.moon.root.title"), Component.translatable("advancements.moon.root.description"), BACKGROUND_TEXTURE, AdvancementType.TASK, true, true, false).addCriterion("land on the moon", InventoryChangeTrigger.TriggerInstance.hasItems((ItemLike) ItemsRegistry.MOON_SAND_ITEM)).save(writer, "moon/root");

        AdvancementHolder advancementHolder2 = Builder.advancement().display((ItemLike) BlocksRegistry.MARS_SAND, Component.translatable("advancements.mars.root.title"), Component.translatable("advancements.mars.root.description"), BACKGROUND_TEXTURE, net.minecraft.advancements.AdvancementType.TASK, true, true, false).addCriterion("land on the mars", InventoryChangeTrigger.TriggerInstance.hasItems((ItemLike) ItemsRegistry.MARS_SAND)).save(writer, "mars/root");
    }

    private AdvancementsRegister() {}
}
