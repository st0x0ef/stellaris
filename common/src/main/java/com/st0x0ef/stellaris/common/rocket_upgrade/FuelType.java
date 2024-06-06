package com.st0x0ef.stellaris.common.rocket_upgrade;

import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FuelType {
    public static int getConsumptionBy100Kilometers(Type type, Type.Radioactive radioactiveElement) {
        return switch (type) {
            case Type.FUEL -> 1000;
            case Type.HYDROGEN -> 800;
            case Type.RADIOACTIVE -> switch (radioactiveElement) {
                case Type.Radioactive.URANIUM -> 500;
                case Type.Radioactive.NEPTUNIUM -> 450;
                case Type.Radioactive.PLUTONIUM -> 400;
            };
        }; // TODO : change these value
    }

    public static Item getFuelItem(Type type, Type.Radioactive radioactiveElement) {
        return switch (type) {
            case Type.FUEL -> ItemsRegistry.FUEL_BUCKET.get();
            case Type.HYDROGEN -> ItemsRegistry.HYDROGEN_BUCKET.get();
            case Type.RADIOACTIVE -> switch (radioactiveElement) {
                case Type.Radioactive.URANIUM -> ItemsRegistry.URANIUM_INGOT.get();
                case Type.Radioactive.NEPTUNIUM -> ItemsRegistry.NEPTUNIUM_INGOT.get();
                case Type.Radioactive.PLUTONIUM -> ItemsRegistry.PLUTONIUM_INGOT.get();
            };
        };
    }

    public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

    public enum Type implements StringRepresentable {
        FUEL,
        HYDROGEN,
        RADIOACTIVE;

        public enum Radioactive implements StringRepresentable {
            URANIUM,
            NEPTUNIUM,
            PLUTONIUM;

            public static Radioactive getTypeBasedOnItem(ItemStack item) {
                if (item.is(ItemsRegistry.URANIUM_INGOT)) {
                    return URANIUM;
                } else if (item.is(ItemsRegistry.NEPTUNIUM_INGOT)) {
                    return NEPTUNIUM;
                } else if (item.is(ItemsRegistry.PLUTONIUM_INGOT)) {
                    return PLUTONIUM;
                }

                return null;
            }

            @Override
            public String getSerializedName() {
                return name().toLowerCase();
            }
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}

