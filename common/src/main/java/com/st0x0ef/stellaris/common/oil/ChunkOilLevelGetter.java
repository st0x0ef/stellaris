package com.st0x0ef.stellaris.common.oil;

public interface ChunkOilLevelGetter {

    default int stellaris$getChunkOilLevel() {
        return 0;
    }

    void stellaris$setChunkOilLevel(int level);


}