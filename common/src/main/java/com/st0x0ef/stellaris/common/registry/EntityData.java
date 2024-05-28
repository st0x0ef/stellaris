package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.RocketModel;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.player.Player;

public class EntityData {


    public static EntityDataAccessor<Boolean> DATA_PLANET_MENU_OPEN;
    public static EntityDataSerializer<RocketModel> ROCKET_MODEL;


    public static void register() {
        DATA_PLANET_MENU_OPEN = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
        ROCKET_MODEL = EntityDataSerializer.forValueType(RocketModel.STREAM_CODEC);

        EntityDataSerializers.registerSerializer(ROCKET_MODEL);
    }
}
