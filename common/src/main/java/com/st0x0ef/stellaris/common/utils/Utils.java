package com.st0x0ef.stellaris.common.utils;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.entities.LanderEntity;
import com.st0x0ef.stellaris.common.entities.RocketEntity;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Utils {

    /** Transfert the rocket inventory inside the Lander inventory */
    public static void transfertInventory(RocketEntity rocket, LanderEntity lander) {
        Container rocketContainer = rocket.getInventory();
        Container landerContainer = lander.getInventory();
        /** We set the rocket in the first slot */
        ItemStack rocketStack = new ItemStack(ItemsRegistry.ROCKET.get());
        rocketContainer.setItem(14, rocketStack);

        /** We start at two because we don"t want the oil inputs */
        for (int i = 0; i <= lander.getInventory().getContainerSize() - 1; i++) {
            landerContainer.setItem(i, rocketContainer.getItem(i));
        }
    }

    /** Should be call after teleporting the player */
    public static LanderEntity createLanderFromRocket(Player player, RocketEntity rocket) {
        LanderEntity lander = new LanderEntity(player.level());
        lander.setPos(player.getX(), player.getY(), player.getZ());
        transfertInventory(rocket, lander);

        rocket.discard();

        return lander;

    }

    /** Teleport an entity to the planet wanted */
    public static void teleportEntity(Entity entity, Planet destination, double yPos, boolean orbit) {

        ServerLevel nextLevel;
        if(orbit) {
            nextLevel = entity.getServer().getLevel(destination.orbit());
        } else {
            nextLevel = entity.getServer().getLevel(destination.dimension());
        }

        if (!entity.canChangeDimensions()) return;

        entity.changeDimension(nextLevel);
        entity.setPos(entity.getX(), yPos, entity.getZ());
    }

    /** To use with the planetSelection menu */
    public static void changeDimension(Player player, Planet destination, boolean orbit) {
        Entity vehicle = player.getVehicle();
        if (vehicle instanceof RocketEntity rocket) {

            /** We create the lander */
            LanderEntity lander = createLanderFromRocket(player, rocket);

            /** We remove the player from the Rocket */
            player.stopRiding();

            teleportEntity(player, destination, 600, orbit);

            player.level().addFreshEntity(lander);
            player.startRiding(lander);


        }

    }

}
