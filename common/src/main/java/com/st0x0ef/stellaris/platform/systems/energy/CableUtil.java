package com.st0x0ef.stellaris.platform.systems.energy;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.NotImplementedException;

public class CableUtil {
    @ExpectPlatform
    public static boolean isEnergyContainer(BlockEntity blockEntity, Direction direction){
        throw new NotImplementedException();
    }
}
