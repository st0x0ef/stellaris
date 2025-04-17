package com.st0x0ef.stellaris.common.oxygen;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.Stellaris;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final  class OxygenSavedData extends SavedData {

    private final Set<OxygenRoom> rooms;

    public static final SavedDataType<OxygenSavedData> TYPE = new SavedDataType<>(
            Stellaris.MODID+"-oxygen",
            OxygenSavedData::new,
            ctx -> RecordCodecBuilder.create(instance -> instance.group(
                    RecordCodecBuilder.point(ctx.levelOrThrow()),
                    OxygenRoomRecord.CODEC.listOf().fieldOf("rooms").forGetter(OxygenSavedData::getRooms)
            ).apply(instance, OxygenSavedData::new)), DataFixTypes.LEVEL);

    public OxygenSavedData(ServerLevel level, List<OxygenRoomRecord> oxygenRoomRecords) {
        List<OxygenRoom> rooms = new ArrayList<>();

        oxygenRoomRecords.forEach(room -> rooms.add(new OxygenRoom(level, room.distributorPos(), room.oxygenatedPos())));

        this.rooms = new HashSet<>(rooms);
        this.setDirty();
    }

    public static OxygenSavedData getData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    private OxygenSavedData(SavedData.Context ctx) {
        this(ctx.levelOrThrow(), new ArrayList<>());
    }

    public List<OxygenRoomRecord> getRooms() {
        List<OxygenRoomRecord> list = new ArrayList<>();
        rooms.forEach((room) -> list.add(new OxygenRoomRecord(room.getDistributorPosition(), room.oxygenatedPositions)));
        return list;
    }
}
