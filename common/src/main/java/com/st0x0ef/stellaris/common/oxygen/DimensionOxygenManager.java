package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.common.armors.JetSuit;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DimensionOxygenManager {
    private final List<OxygenRoom> oxygenRooms;
    private final Map<BlockPos, OxygenRoom> roomToCheckIfOpen;

    public DimensionOxygenManager() {
        this.oxygenRooms = new ArrayList<>();
        this.roomToCheckIfOpen = new HashMap<>();
    }

    public List<OxygenRoom> getOxygenSystems() {
        return oxygenRooms;
    }

    public void addOxygenSystem(OxygenRoom system) {
        oxygenRooms.add(system);
    }

    public void addRoomToCheckIfOpen(BlockPos pos, OxygenRoom room) {
        if (roomToCheckIfOpen.containsKey(pos)) {
            roomToCheckIfOpen.remove(pos);
        } else {
            roomToCheckIfOpen.put(pos, room);
        }
    }

    public void updateOxygen(ServerLevel level) {
        for (OxygenRoom system : oxygenRooms) {
            system.updateOxygenRoom(level);
        }

        roomToCheckIfOpen.forEach((pos, room) -> {
            room.removeOxygenInRoom();
        });
    }

    public boolean entityHasOxygen(LivingEntity entity) {
        if (PlanetUtil.hasOxygen(entity.level().dimension().location()) || entity.getType().is(TagRegistry.ENTITY_NO_OXYGEN_NEEDED_TAG)) return true;

        if (entity instanceof LivingEntity livingEntity && Utils.isLivingInJetSuit(livingEntity)) {
            JetSuit.Suit suit = (JetSuit.Suit) livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem();
            if (suit.getOxygen(livingEntity.getItemBySlot(EquipmentSlot.CHEST)) <= 0) {
                return false;
            }
        } else {
            for (OxygenRoom system : getOxygenSystems()) {
                if (!system.hasOxygenAt(entity.getOnPos())) {
                    return false;
                }
            }
        }

        return true;
    }
}
