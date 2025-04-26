package com.st0x0ef.stellaris.common.oil;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.Stellaris;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.*;

public final  class OilSavedData extends SavedData {

    private final List<ChunkPos> chunkPos = new ArrayList<>();
    private final List<Integer> amounts = new ArrayList<>();

    public static final SavedDataType<OilSavedData> TYPE = new SavedDataType<>(
            Stellaris.MODID+"-oil",
            OilSavedData::new,
            ctx -> RecordCodecBuilder.create(instance -> instance.group(
                    ChunkPos.CODEC.listOf().fieldOf("chunkPos").forGetter(OilSavedData::getChunkPos),
                    Codec.INT.listOf().fieldOf("amount").forGetter(OilSavedData::getAmounts)
            ).apply(instance, OilSavedData::new)), DataFixTypes.LEVEL);

    public OilSavedData(List<ChunkPos> chunkPos, List<Integer> amounts) {
        this.chunkPos.clear();
        this.amounts.clear();

        this.chunkPos.addAll(chunkPos);
        this.amounts.addAll(amounts);
        this.setDirty();
    }

    public static OilSavedData getData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    private OilSavedData(Context ctx) {}

    public List<Integer> getAmounts() {
        return amounts;
    }

    public List<ChunkPos> getChunkPos() {
        return chunkPos;
    }

    public Integer getAmount(ChunkPos pos) {
        for (int i = 0; i < chunkPos.size(); i++) {
            if (chunkPos.get(i).equals(pos)) {
                return amounts.get(i);
            }
        }

        int oil = OilUtils.getRandomOilLevel();
        chunkPos.add(pos);
        amounts.add(oil);
        setDirty();

        return oil;
    }

    public void setAmount(ChunkPos pos, int amount) {
        for (int i = 0; i < chunkPos.size(); i++) {
            if (chunkPos.get(i).equals(pos)) {
                amounts.set(i, amount);
                setDirty();
                return;
            }
        }

        chunkPos.add(pos);
        amounts.add(amount);
        setDirty();
    }
}
