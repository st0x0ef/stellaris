package com.st0x0ef.stellaris.common.registry;

import com.google.common.base.Suppliers;

import net.minecraft.tags.BlockTags;

import net.minecraft.tags.TagKey;

import net.minecraft.world.item.Tier;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;


import java.util.Objects;
import java.util.function.Supplier;


public enum ToolsRegister implements Tier {
    STEEL(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 9.0F, 4.0F, 15, () -> Ingredient.of(new ItemLike[]{ItemsRegistry.STEEL_INGOT.get()}));

        private final TagKey<Block> incorrectBlocksForDrops;
        private final int uses;
        private final float speed;
        private final float damage;
        private final int enchantmentValue;
        private final Supplier<Ingredient> repairIngredient;

         ToolsRegister(final TagKey incorrectBlockForDrops, final int uses, final float speed, final float damage, final int enchantmentValue, final Supplier repairIngredient) {
            this.incorrectBlocksForDrops = incorrectBlockForDrops;
            this.uses = uses;
            this.speed = speed;
            this.damage = damage;
            this.enchantmentValue = enchantmentValue;
            Objects.requireNonNull(repairIngredient);
            this.repairIngredient = (Supplier<Ingredient>) Suppliers.memoize(repairIngredient::get);
        }

        public int getUses() {
            return this.uses;
        }

        public float getSpeed() {
            return this.speed;
        }

        public float getAttackDamageBonus() {
            return this.damage;
        }

        public TagKey<Block> getIncorrectBlocksForDrops() {
            return this.incorrectBlocksForDrops;
        }

        public int getEnchantmentValue() {
            return this.enchantmentValue;
        }

        public Ingredient getRepairIngredient() {
            return (Ingredient)this.repairIngredient.get();
        }
    }

