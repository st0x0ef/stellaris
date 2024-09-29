package com.st0x0ef.stellaris.neoforge.events;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.vehicles.base.AbstractRoverBase;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Stellaris.MODID,bus = EventBusSubscriber.Bus.GAME,value = Dist.CLIENT)
public class Events
{
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event)
    {
        Minecraft minecraft = Minecraft.getInstance();

        Player player = minecraft.player;

        if (player == null) {
            return;
        }

        Entity riding = player.getVehicle();

        if (!(riding instanceof AbstractRoverBase car)) {
            return;
        }

        if (player.equals(car.getDriver())) {
            car.updateControls(minecraft.options.keyUp.isDown(), minecraft.options.keyDown.isDown(), minecraft.options.keyLeft.isDown(), minecraft.options.keyRight.isDown(), player);
        }
    }
}
