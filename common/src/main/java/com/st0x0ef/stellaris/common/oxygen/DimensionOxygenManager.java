package com.st0x0ef.stellaris.common.oxygen;

import com.fej1fun.potentials.capabilities.Capabilities;
import com.fej1fun.potentials.fluid.UniversalFluidStorage;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
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

public class DimensionOxygenManager {

    private final Set<OxygenRoom> oxygenRooms;
    private final Map<BlockPos, OxygenRoom> roomToCheckIfOpen;
    private final boolean planetHasOxygen;
    private final ServerLevel level;
    private int tickCount;

    public DimensionOxygenManager(ServerLevel level) {
        this.oxygenRooms = new HashSet<>();
        this.roomToCheckIfOpen = new HashMap<>();
        this.level = level;
        this.planetHasOxygen = PlanetUtil.hasOxygen(level);
    }

    public void addOxygenRoom(BlockPos pos) {
        oxygenRooms.add(new OxygenRoom(level, pos));
    }

    public void removeOxygenRoom(BlockPos pos) {
        oxygenRooms.removeIf(room -> room.getDistributorPosition().equals(pos));
    }

    public void addRoomToCheckIfOpen(BlockPos pos, OxygenRoom room) {
        if (!roomToCheckIfOpen.containsKey(pos)) {
            roomToCheckIfOpen.put(pos, room);
        }
    }

    public void removeRoomToCheckIfOpen(BlockPos pos) {
        roomToCheckIfOpen.remove(pos);
    }

    public void updateOxygenTick() {
        if (planetHasOxygen || tickCount < 20) {
            tickCount++;
            return;
        }

        oxygenRooms.forEach(OxygenRoom::tick);
        roomToCheckIfOpen.values().forEach(OxygenRoom::removeOxygenInRoom);
        roomToCheckIfOpen.clear();

        tickCount = 0;
    }

    public boolean breath(LivingEntity entity) {
        if (planetHasOxygen || entity.getType().is(TagRegistry.ENTITY_NO_OXYGEN_NEEDED_TAG)) {
            return true;
        }

        if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return true;
        }

        if (breathOxygenAt(entity.getOnPos().above())) {
            return true;
        }

        if(Utils.isLivingInOxygenatedArmor(entity)) {
            return true;
        }

        if (Utils.isLivingInJetSuit(entity) || Utils.isLivingInSpaceSuit(entity)) {
            UniversalFluidStorage storage = Capabilities.Fluid.ITEM.getCapability(entity.getItemBySlot(EquipmentSlot.CHEST));
            if (storage != null && !storage.getFluidInTank(0).isEmpty()) {
                storage.drain(storage.getFluidInTank(0).copyWithAmount(1), false);
                return true;
            }
        }

        return false;
    }

    public boolean breathOxygenAt(BlockPos pos) {
        return oxygenRooms.stream().anyMatch(room -> room.breathOxygenAt(pos));
    }

    public boolean hasOxygenAt(BlockPos pos) {
        return oxygenRooms.stream().anyMatch(room -> room.hasOxygenAt(pos));
    }

    public OxygenRoom getOxygenRoom(BlockPos distributorPos) {
        return oxygenRooms.stream()
                .filter(room -> room.getDistributorPosition().equals(distributorPos))
                .findFirst()
                .orElse(null);
    }

    public ServerLevel getLevel() {
        return level;
    }
}