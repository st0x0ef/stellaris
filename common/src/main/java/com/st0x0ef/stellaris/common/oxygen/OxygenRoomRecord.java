package com.st0x0ef.stellaris.common.oxygen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Set;

public record OxygenRoomRecord(BlockPos distributorPos, List<BlockPos> oxygenatedPos) {
    public static final Codec<OxygenRoomRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("distributorPos").forGetter(OxygenRoomRecord::distributorPos),
            BlockPos.CODEC.listOf().fieldOf("oxygenatedPos").forGetter(OxygenRoomRecord::oxygenatedPos)
            ).apply(instance, OxygenRoomRecord::new));
}