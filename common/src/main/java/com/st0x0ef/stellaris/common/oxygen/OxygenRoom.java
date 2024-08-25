package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.common.blocks.entities.machines.OxygenGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OxygenRoom {
    private final OxygenGeneratorBlockEntity generatorBlockEntity;
    private final Map<BlockPos, Boolean> oxygenMap;
    private final List<BlockPos> oxygenPosToCheck;

    public OxygenRoom(OxygenGeneratorBlockEntity generatorBlockEntity) {
        this.generatorBlockEntity = generatorBlockEntity;
        this.oxygenMap = new HashMap<>();
        this.oxygenPosToCheck = new ArrayList<>();
    }

    public BlockPos getGeneratorPosition() {
        return generatorBlockEntity.getBlockPos();
    }

    public void updateOxygenRoom(ServerLevel level) {
        if (!oxygenMap.getOrDefault(getGeneratorPosition(), false) || GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level.dimension()).doesPlanetHasOxygen() || ((OxygenGeneratorBlockEntity) level.getBlockEntity(getGeneratorPosition())).takeOxygenFromTank(50)) {
            return;
        }

        Map<BlockPos, Boolean> updatedOxygenMap = new HashMap<>(oxygenMap);

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos currentPos = getGeneratorPosition().offset(x, y, z);
                    propagateOxygenInRoom(level, currentPos, updatedOxygenMap);
                }
            }
        }

        oxygenMap.putAll(updatedOxygenMap);
    }

    private void propagateOxygenInRoom(ServerLevel level, BlockPos currentPos, Map<BlockPos, Boolean> updatedOxygenMap) {
        oxygenPosToCheck.remove(currentPos);

        if (level.getBlockState(currentPos).isAir() && !((OxygenGeneratorBlockEntity) level.getBlockEntity(getGeneratorPosition())).takeOxygenFromTank(50)) {
            updatedOxygenMap.put(currentPos, true);
            if (isOnBorderBox(currentPos)) {
                GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level.dimension()).addRoomToCheckIfOpen(currentPos, this);
            }

            for (int x = 0; x < 32; x++) {
                for (int y = 0; y < 32; y++) {
                    for (int z = 0; z < 32; z++) {
                        BlockPos nextPos = new BlockPos(getGeneratorPosition().getX() + x - 16, getGeneratorPosition().getY() + y - 16, getGeneratorPosition().getZ() + z - 16);
                        oxygenPosToCheck.add(nextPos);
                    }
                }
            }

            if (!oxygenPosToCheck.isEmpty() && !updatedOxygenMap.containsKey(oxygenPosToCheck.getFirst())) {
                propagateOxygenInRoom(level, oxygenPosToCheck.getFirst(), updatedOxygenMap);
            }
        }
    }

    public void removeOxygenInRoom() {
        oxygenMap.replaceAll((pos, hasOxygen) -> false);
    }

    private boolean isOnBorderBox(BlockPos pos) {
        int startX = getGeneratorPosition().getX() - 16;
        int endX = getGeneratorPosition().getX() + 16;
        int startY = getGeneratorPosition().getY() - 16;
        int endY = getGeneratorPosition().getY() + 16;
        int startZ = getGeneratorPosition().getZ() - 16;
        int endZ = getGeneratorPosition().getZ() + 16;

        return pos.getX() == startX || pos.getX() == endX || pos.getY() == startY || pos.getY() == endY || pos.getZ() == startZ || pos.getZ() == endZ;
    }

    public boolean hasOxygenAt(BlockPos pos) {
        return oxygenMap.get(pos);
    }
}
