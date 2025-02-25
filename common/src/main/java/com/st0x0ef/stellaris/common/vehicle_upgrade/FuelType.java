package com.st0x0ef.stellaris.common.vehicle_upgrade;

import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.client.screens.GUISprites;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

public class FuelType {
    public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

    public static float getMegametersTraveled(int fuelQuantity, Item fuelItem) {
        Type type = Type.getTypeBasedOnItem(fuelItem);

        if (type != null) {
            return switch (type) {
                case FUEL -> 19.22f * fuelQuantity; // Need 20mb to go on Moon, 2133mb to go on Venus, 2900mb to go on Mars and 4786mb to go on Mercury (approx)
                case HYDROGEN -> 21.36f * fuelQuantity; // Need 18mb to go on Moon, 1920mb to go on Venus, 2610mb to go on Mars and 4307mb to go on Mercury (approx)
                case RADIOACTIVE, URANIUM -> 23.74f * fuelQuantity; // Need 16mb to go on Moon, 1728mb to go on Venus, 2349mb to go on Mars and 3876mb to go on Mercury (approx)
                case NEPTUNIUM -> 26.38f * fuelQuantity; // Need 15mb to go on Moon, 1555mb to go on Venus, 2114mb to go on Mars and 3488mb to go on Mercury (approx)
                case PLUTONIUM -> 29.3f * fuelQuantity; // Need 14mb to go on Moon, 1400mb to go on Venus, 1903mb to go on Mars and 3140mb to go on Mercury (approx)
                default -> throw new IllegalStateException("Unexpected value: " + type);
            };
        }

        return 0.0f;
    }

    public static float getFuelNeededToGoOnPlanet(Planet actual, Planet destination, Item fuelItem) {
        float distance = Mth.abs(actual.distanceFromEarth() - destination.distanceFromEarth());

        Type type = Type.getTypeBasedOnItem(fuelItem);

        if (type != null) {
            return switch (type) {
                case FUEL -> distance / 19.22f; // Need 20mb to go on Moon, 2133mb to go on Venus, 2900mb to go on Mars and 4786mb to go on Mercury (approx)
                case HYDROGEN -> distance / 21.36f; // Need 18mb to go on Moon, 1920mb to go on Venus, 2610mb to go on Mars and 4307mb to go on Mercury (approx)
                case RADIOACTIVE, URANIUM -> distance / 23.74f; // Need 16mb to go on Moon, 1728mb to go on Venus, 2349mb to go on Mars and 3876mb to go on Mercury (approx)
                case NEPTUNIUM -> distance / 26.38f; // Need 15mb to go on Moon, 1555mb to go on Venus, 2114mb to go on Mars and 3488mb to go on Mercury (approx)
                case PLUTONIUM -> distance / 29.3f; // Need 14mb to go on Moon, 1400mb to go on Venus, 1903mb to go on Mars and 3140mb to go on Mercury (approx)
                default -> throw new IllegalStateException("Unexpected value: " + type);
            };
        }

        return 0.0f;
    }

    public static Item getItemBasedOnTypeName(String name) {
        if (name.equals(Type.FUEL.getSerializedName())) {
            return ItemsRegistry.FUEL_BUCKET.get();
        } else if (name.equals(Type.HYDROGEN.getSerializedName())) {
            return ItemsRegistry.HYDROGEN_BUCKET.get();
        } else if (name.equals(Type.URANIUM.getSerializedName())) {
            return ItemsRegistry.URANIUM_INGOT.get();
        } else if (name.equals(Type.NEPTUNIUM.getSerializedName())) {
            return ItemsRegistry.NEPTUNIUM_INGOT.get();
        } else if (name.equals(Type.PLUTONIUM.getSerializedName())) {
            return ItemsRegistry.PLUTONIUM_INGOT.get();
        }

        return null;
    }

    public static Item getItemBasedOnLoacation(ResourceLocation location) {
        return ItemsRegistry.ITEMS.getRegistrar().get(location);
    }

    public enum Type implements StringRepresentable {
        FUEL(GUISprites.FUEL_OVERLAY, false),
        HYDROGEN(GUISprites.HYDROGEN_OVERLAY, false),
        URANIUM(GUISprites.ENERGY_FULL, true),
        NEPTUNIUM(GUISprites.ENERGY_FULL, true),
        PLUTONIUM(GUISprites.ENERGY_FULL, true),
        RADIOACTIVE(GUISprites.ENERGY_FULL, true);

        private final ResourceLocation fuelTexture;
        private final boolean isRadioactive;

        Type(ResourceLocation fuelTexture, boolean isRadioactive) {
            this.fuelTexture = fuelTexture;
            this.isRadioactive = isRadioactive;
        }

        public boolean isRadioactive() {
            return this.isRadioactive;
        }

        public static Type getTypeBasedOnItem(Item item) {
            if (item == null) return null;
            if (item.getDefaultInstance().is(ItemsRegistry.FUEL_BUCKET.get())) {
                return FUEL;
            } else if (item.getDefaultInstance().is(ItemsRegistry.HYDROGEN_BUCKET.get())) {
                return HYDROGEN;
            } else if (item.getDefaultInstance().is(ItemsRegistry.URANIUM_INGOT.get())) {
                return URANIUM;
            } else if (item.getDefaultInstance().is(ItemsRegistry.NEPTUNIUM_INGOT.get())) {
                return NEPTUNIUM;
            } else if (item.getDefaultInstance().is(ItemsRegistry.PLUTONIUM_INGOT.get())) {
                return PLUTONIUM;
            }

            return null;
        }

        public static Type fromString(String name) {
            return switch (name) {
                case "fuel" -> FUEL;
                case "hydrogen" -> HYDROGEN;
                case "uranium" -> URANIUM;
                case "neptunium" -> NEPTUNIUM;
                case "plutonium" -> PLUTONIUM;
                case "radioactive" -> RADIOACTIVE;
                default -> null;
            };
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }

        public ResourceLocation getFuelTexture() {
            return this.fuelTexture;
        }
    }
}

