package com.st0x0ef.stellaris.client.screens.info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.screens.record.PSystemRecord;
import net.minecraft.network.chat.Component;

import java.util.List;

public class PSystemInfo {
    public final String name;
    public final String translatable;
    public final String parent;
    public final String id;
    public final List<PSystemRecord.StarPosition> stars;
    public final float centerX;
    public final float centerY;

    public PSystemInfo(String name, String translatable, String parent, String id, List<PSystemRecord.StarPosition> stars, float centerX, float centerY) {
        this.name = name;
        this.translatable = translatable;
        this.parent = parent;
        this.id = id;
        this.stars = stars;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public String getParent() {
        return this.parent;
    }
}
