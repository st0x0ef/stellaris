package com.st0x0ef.stellaris.platform.systems.core.energy;

import com.st0x0ef.stellaris.platform.systems.core.storage.base.ValueStorage;
import com.st0x0ef.stellaris.platform.systems.core.storage.util.TransferUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EnergyUtils {

    public static <T extends BlockEntity & EnergyProvider.BlockEntity> void distributeEnergyNearby(T blockEntity, long amount) {
        Direction.stream().forEach((direction)-> { /**this should work*/
            ValueStorage toEnergy = EnergyApi.BLOCK.find(blockEntity,direction);
            ValueStorage fromEnergy = blockEntity.getEnergy(null);
            if (fromEnergy!=null && toEnergy!=null) TransferUtil.moveValue(fromEnergy, toEnergy, amount,false);
        });
    }
}
