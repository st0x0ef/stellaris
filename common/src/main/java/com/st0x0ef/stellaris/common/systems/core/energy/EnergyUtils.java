package com.st0x0ef.stellaris.common.systems.core.energy;

import com.st0x0ef.stellaris.common.systems.core.storage.base.ValueStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EnergyUtils {

    public static <T extends BlockEntity & EnergyProvider.BlockEntity> void distributeEnergyNearby(T blockEntity, long amount) {
        Direction.stream().forEach((direction)-> { /**this should work*/
            ValueStorage toEnergy = EnergyApi.BLOCK.find(blockEntity,direction);
            ValueStorage fromEnergy = blockEntity.getEnergy(null);
            if (toEnergy == null || fromEnergy == null) return;
            if (blockEntity.getEnergy(null).getStoredAmount()!=0) return;
            long simAmountInsert = toEnergy.insert(amount,true);
            long simAmountExtract = fromEnergy.extract(amount,true);

            if (simAmountInsert>=simAmountExtract) {
                fromEnergy.extract(simAmountExtract,false);
                toEnergy.insert(simAmountExtract,false);
            } else {
                fromEnergy.extract(simAmountInsert,false);
                toEnergy.insert(simAmountInsert,false);
            }
        });
    }
}
