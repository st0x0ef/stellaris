package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import com.st0x0ef.stellaris.common.utils.OxygenUtils;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DimensionOxygenManager {
    private final List<OxygenRoom> oxygenRooms;
    private final Map<BlockPos, OxygenRoom> roomToCheckIfOpen;
    private final boolean planetHasOxygen;

    public DimensionOxygenManager(ResourceKey<Level> level) {
        this.oxygenRooms = new ArrayList<>();
        this.roomToCheckIfOpen = new HashMap<>();
        this.planetHasOxygen = PlanetUtil.hasOxygen(level.location());
    }

    public void addOxygenRoom(OxygenRoom room) {
        oxygenRooms.add(room);
    }

    public void removeOxygenRoom(BlockPos pos) {
        oxygenRooms.removeIf(room -> room.getGeneratorPosition().equals(pos));
    }

    public void addRoomToCheckIfOpen(BlockPos pos, OxygenRoom room) {
        if (checkIfRoomOpen(pos)) {
            roomToCheckIfOpen.put(pos, room);
        }
    }

    public boolean checkIfRoomOpen(BlockPos pos) {
        if (roomToCheckIfOpen.containsKey(pos)) {
            roomToCheckIfOpen.remove(pos);
            return false;
        }

        return true;
    }

    public void updateOxygen(ServerLevel level) {
        if (planetHasOxygen) return;

        for (OxygenRoom system : oxygenRooms) {
            system.updateOxygenRoom(level);
        }

        roomToCheckIfOpen.forEach((pos, room) -> room.removeOxygenInRoom());
    }

    public boolean entityHasOxygen(LivingEntity entity) {
        if (planetHasOxygen || entity.getType().is(TagRegistry.ENTITY_NO_OXYGEN_NEEDED_TAG)) return true;

        if (entity instanceof LivingEntity livingEntity && Utils.isLivingInJetSuit(livingEntity)) {
            if (OxygenUtils.getOxygen(livingEntity.getItemBySlot(EquipmentSlot.CHEST)) <= 0) {
                return false;
            }
        } else {
            for (OxygenRoom system : oxygenRooms) {
                if (system.hasOxygenAt(entity.getOnPos())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean doesPlanetHasOxygen() {
        return planetHasOxygen;
    }
}
