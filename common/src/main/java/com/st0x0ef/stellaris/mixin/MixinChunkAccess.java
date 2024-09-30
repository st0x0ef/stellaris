package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.common.oil.ChunkOilLevelGetter;
import com.st0x0ef.stellaris.common.oil.OilUtils;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChunkAccess.class)
public class MixinChunkAccess implements ChunkOilLevelGetter {

    @Unique
    private int stellaris$oilLevel = -1;

    @Override
    public int stellaris$getChunkOilLevel() {
        if (stellaris$oilLevel == -1) {
            stellaris$setChunkOilLevel(OilUtils.getRandomOilLevel());
        }

        return stellaris$oilLevel;
    }

    @Override
    public void stellaris$setChunkOilLevel(int level) {
        stellaris$oilLevel = level;
    }
}
