package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.common.registry.TagRegistry;
import com.st0x0ef.stellaris.common.utils.OxygenUtils;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class DimensionOxygenManager {
    private final Set<OxygenRoom> oxygenRooms;
    private final Map<BlockPos, OxygenRoom> roomToCheckIfOpen;
    private final boolean planetHasOxygen;
    private final ServerLevel level;

    public DimensionOxygenManager(ServerLevel level) {
        this.oxygenRooms = new HashSet<>();
        this.roomToCheckIfOpen = new HashMap<>();
        this.planetHasOxygen = PlanetUtil.hasOxygen(level.dimension().location());
        this.level = level;
    }

    public void addOxygenRoom(OxygenRoom room) {
        oxygenRooms.add(room);
        this.setChanged();
    }

    public void removeOxygenRoom(BlockPos pos) {
        oxygenRooms.removeIf(room -> room.getDistributorPosition().equals(pos));
        this.setChanged();
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

    private void setChanged() {
        OxygenSavedData data = OxygenSavedData.getData(level);
        data.setDirty();
    }

    public void updateOxygen() {
        if (planetHasOxygen) return;

        oxygenRooms.forEach(OxygenRoom::updateOxygenRoom);
        roomToCheckIfOpen.values().forEach(OxygenRoom::removeOxygenInRoom);
        roomToCheckIfOpen.clear();
        this.setChanged();

    }

    public boolean canBreath(LivingEntity entity) {
        if (planetHasOxygen || entity.getType().is(TagRegistry.ENTITY_NO_OXYGEN_NEEDED_TAG)) return true;

        if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return true;
        }

        if (Utils.isLivingInJetSuit(entity) || Utils.isLivingInSpaceSuit(entity)) {
            return OxygenUtils.getOxygen(entity.getItemBySlot(EquipmentSlot.CHEST)) > 0;
        }

        AtomicBoolean canBreath = new AtomicBoolean(false);
        oxygenRooms.forEach(room -> {
            if (room.hasOxygenAt(entity.getOnPos().above())) {
                canBreath.set(true);
            }
        });

        return canBreath.get();
    }

    public Set<OxygenRoom> getOxygenRooms() {
        return oxygenRooms;
    }

    public OxygenRoom getOxygenRoom(BlockPos pos) {
        for (OxygenRoom room : oxygenRooms) {
            if (room.getDistributorPosition().equals(pos)) {
                return room;
            }
        }

        return null;
    }

    public void setOxygensRooms(Set<OxygenRoom> rooms) {
        this.oxygenRooms.clear();
        this.oxygenRooms.addAll(rooms);
    }
}
