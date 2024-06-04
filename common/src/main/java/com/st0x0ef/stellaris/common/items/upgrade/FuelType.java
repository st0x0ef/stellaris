package com.st0x0ef.stellaris.common.items.upgrade;

import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

public class FuelType {
    public static int getConsumptionBy100Kilometers(Type type) {
        return switch (type) {
            case Type.FUEL -> 1000;
            case Type.HYDROGEN -> 800;
            case Type.URANIUM -> 500;
            case Type.NEPTUNIUM -> 450;
            case Type.PLUTONIUM -> 400;
        }; // TODO : change these value
    }

    public static Item getFuelItem(Type type) {
        return switch (type) {
            case Type.FUEL -> ItemsRegistry.FUEL_BUCKET.get();
            case Type.HYDROGEN -> ItemsRegistry.HYDROGEN_BUCKET.get();
            case Type.URANIUM -> ItemsRegistry.URANIUM_INGOT.get();
            case Type.NEPTUNIUM -> ItemsRegistry.NEPTUNIUM_INGOT.get();
            case Type.PLUTONIUM -> ItemsRegistry.PLUTONIUM_INGOT.get();
        };
    }

    public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

    public enum Type implements StringRepresentable {
        FUEL,
        HYDROGEN,
        URANIUM,
        NEPTUNIUM,
        PLUTONIUM;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}

