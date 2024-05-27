package com.st0x0ef.stellaris.common.utils;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.entities.LanderEntity;
import com.st0x0ef.stellaris.common.entities.RocketEntity;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import com.st0x0ef.stellaris.platform.TeleportUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Utils {
    /** Transfert the rocket inventory inside the Lander inventory */
    public static void transfertInventory(RocketEntity rocket, LanderEntity lander) {
        Container rocketContainer = rocket.getInventory();
        Container landerContainer = lander.getInventory();
        /** We set the rocket in the first slot */
        ItemStack rocketStack = new ItemStack(ItemsRegistry.ROCKET.get());
        rocketContainer.setItem(13, rocketStack);

        /** We start at two because we don"t want the oil inputs */
        for (int i = 0; i <= lander.getInventory().getContainerSize() - 1; i++) {
            landerContainer.setItem(i, rocketContainer.getItem(i));
        }
    }

    /** Should be call after teleporting the player */
    public static LanderEntity createLanderFromRocket(Player player, RocketEntity rocket, int yPos) {

        LanderEntity lander = new LanderEntity(player.level());
        lander.setPos(player.getX(), yPos, player.getZ());
        transfertInventory(rocket, lander);

        rocket.discard();

        return lander;

    }

    /** Teleport an entity to the planet wanted */
    public static void teleportEntity(Entity entity, Planet destination, int yPos, boolean orbit) {

        if(entity.level().isClientSide()) return;
        entity.setNoGravity(false);

        ServerLevel nextLevel;
        if(orbit) {

            nextLevel = entity.level().getServer().getLevel(destination.orbit());
        } else {

            nextLevel = entity.level().getServer().getLevel(destination.dimension());
        }

        if (!entity.canChangeDimensions()) return;

        //entity.changeDimension(nextLevel);

        TeleportUtil.teleportToPlanet(entity, nextLevel, yPos);
        entity.setPos(entity.getX(), yPos, entity.getZ());

    }

    /** To use with the planetSelection menu */
    public static void changeDimension(Player player, Planet destination, boolean orbit) {

        if(player.level().isClientSide()) return;

        Entity vehicle = player.getVehicle();
        if (vehicle instanceof RocketEntity rocket) {

            /** We create the lander */
            LanderEntity lander = createLanderFromRocket(player, rocket, 600);

            /** We remove the player from the Rocket */
            player.stopRiding();

            teleportEntity(player, destination, 600, orbit);

            player.closeContainer();

            player.level().addFreshEntity(lander);
            player.startRiding(lander);
        } else {
            player.closeContainer();
            teleportEntity(player, destination, 600, orbit);
        }


    }

    public static double changeLastDigitToEven(double number) {
        String numberStr = Double.toString(number);
        int decimalIndex = numberStr.indexOf('.');

        if (decimalIndex != -1) {
            String beforeDecimal = numberStr.substring(0, decimalIndex + 1);
            String afterDecimal = numberStr.substring(decimalIndex + 1);

            char lastChar = afterDecimal.charAt(afterDecimal.length() - 1);

            if ((lastChar - '0') % 2 != 0) {
                afterDecimal = afterDecimal.substring(0, afterDecimal.length() - 1) + '2';
            }

            numberStr = beforeDecimal + afterDecimal;
        }

        return Double.parseDouble(numberStr);
    }

    /** COLOR!!! */
    public static int getColorHexCode(String colorName) {
        switch (colorName.toLowerCase()) {
            case "black":
                return 0x000000;
            case "white":
                return 0xFFFFFF;
            case "red":
                return 0xFF0000;
            case "green":
                return 0x008000;
            case "blue":
                return 0x0000FF;
            case "yellow":
                return 0xFFFF00;
            case "cyan":
                return 0x00FFFF;
            case "magenta":
                return 0xFF00FF;
            case "gray":
                return 0x808080;
            case "maroon":
                return 0x800000;
            case "olive":
                return 0x808000;
            case "purple":
                return 0x800080;
            case "teal":
                return 0x008080;
            case "navy":
                return 0x000080;
            case "orange":
                return 0xFFA500;
            case "brown":
                return 0xA52A2A;
            case "lime":
                return 0x00FF00;
            case "pink":
                return 0xFFC0CB;
            case "coral":
                return 0xFF7F50;
            case "gold":
                return 0xFFD700;
            case "silver":
                return 0xC0C0C0;
            case "beige":
                return 0xF5F5DC;
            case "lavender":
                return 0xE6E6FA;
            case "turquoise":
                return 0x40E0D0;
            case "salmon":
                return 0xFA8072;
            case "khaki":
                return 0xF0E68C;
            case "darkred":
                return 0x8B0000;
            case "dark_red":
                return 0x8B0000;
            default:
                throw new IllegalArgumentException("Unknown color : " + colorName);
        }
    }
}
