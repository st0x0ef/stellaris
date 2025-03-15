package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.RocketModel;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;

public class EntityData {


    public static EntityDataSerializer<RocketModel> ROCKET_MODEL;

    public static void register() {
    }
}
