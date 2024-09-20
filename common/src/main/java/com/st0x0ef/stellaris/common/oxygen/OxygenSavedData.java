package com.st0x0ef.stellaris.common.oxygen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Set;

public final  class OxygenSavedData extends SavedData {

    private final Set<OxygenRoom> rooms;
    private final ServerLevel level;

    public static SavedData.Factory<OxygenSavedData> factory(ServerLevel level) {
        return new SavedData.Factory<>(() -> new OxygenSavedData(level), (compoundTag, provider) -> load(compoundTag, level), DataFixTypes.LEVEL);
    }

    public static OxygenSavedData getData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(factory(level), "oxygen-rooms");
    }

    public OxygenSavedData(ServerLevel level) {
        this.rooms = new HashSet<>();
        this.level = level;
        this.setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        DimensionOxygenManager dimensionOxygenManager = GlobalOxygenManager.getInstance().getOrCreateDimensionManager(this.level);

        int rooms = 0;

        if (dimensionOxygenManager.getOxygenRooms() != null && !dimensionOxygenManager.getOxygenRooms().isEmpty()) {
            for (OxygenRoom room : dimensionOxygenManager.getOxygenRooms()) {
                tag.putIntArray("oxygenDistributorPos" + rooms, new int[]{room.getDistributorPosition().getX(), room.getDistributorPosition().getY(), room.getDistributorPosition().getZ()});
                rooms++;
            }
        }

        tag.putInt("OxygenRooms", rooms);

        return tag;
    }

    public static OxygenSavedData load(CompoundTag tag, ServerLevel level) {
        OxygenSavedData data = new OxygenSavedData(level);

        int rooms = tag.getInt("OxygenRooms");

        if (rooms == 0) {
            return data;
        }

        for (int i = 0; i < rooms; i++) {
            int[] distributorPos = tag.getIntArray("oxygenDistributorPos" + i);
            OxygenRoom oxygenRoom = new OxygenRoom(level, new BlockPos(distributorPos[0], distributorPos[1], distributorPos[2]));
            data.rooms.add(oxygenRoom);
        }

        GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).setOxygensRooms(data.rooms);

        return data;
    }
}
