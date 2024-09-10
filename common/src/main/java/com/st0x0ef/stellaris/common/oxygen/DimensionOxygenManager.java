package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.common.registry.TagRegistry;
import com.st0x0ef.stellaris.common.utils.OxygenUtils;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.*;

public class DimensionOxygenManager {
    private final Set<OxygenRoom> oxygenRooms;
    private final Map<BlockPos, OxygenRoom> roomToCheckIfOpen;
    private final boolean planetHasOxygen;

    public DimensionOxygenManager(ResourceKey<Level> level) {
        this.oxygenRooms = new HashSet<>();
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

        oxygenRooms.parallelStream().forEach(room -> room.updateOxygenRoom(level));
        roomToCheckIfOpen.values().forEach(OxygenRoom::removeOxygenInRoom);
        roomToCheckIfOpen.clear();
    }

    public boolean canBreath(LivingEntity entity) {
        if (planetHasOxygen || entity.getType().is(TagRegistry.ENTITY_NO_OXYGEN_NEEDED_TAG)) return true;

        if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return true;
        }

        if (Utils.isLivingInJetSuit(entity)) {
            return OxygenUtils.getOxygen(entity.getItemBySlot(EquipmentSlot.CHEST)) > 0;
        }

        BlockPos entityPos = entity.getOnPos();
        return oxygenRooms.stream().anyMatch(room -> room.hasOxygenAt(entityPos));
    }

    public boolean doesPlanetHasOxygen() {
        return planetHasOxygen;
    }
}
