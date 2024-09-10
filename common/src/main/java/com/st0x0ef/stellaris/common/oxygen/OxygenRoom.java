package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.common.blocks.entities.machines.OxygenDistributorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

import java.util.*;

public class OxygenRoom {
    private final BlockPos distributorPos;
    private final Set<BlockPos> oxygenatedPositions;
    private final Queue<BlockPos> positionsToCheck;

    private static final int HALF_ROOM_SIZE = 16;

    public OxygenRoom(BlockPos distributorPos) {
        this.distributorPos = distributorPos;
        this.oxygenatedPositions = new HashSet<>();
        this.positionsToCheck = new LinkedList<>();
    }

    public BlockPos getGeneratorPosition() {
        return distributorPos;
    }

    public void updateOxygenRoom(ServerLevel level) {
        if (oxygenatedPositions.contains(distributorPos) ||
                GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level.dimension()).doesPlanetHasOxygen() ||
                getOxygenDistributor(level).takeOxygenFromTank()) {
            return;
        }

        BlockPos abovePos = distributorPos.above();
        if (isAirBlock(level, abovePos)) {
            propagateOxygenInRoom(level, abovePos);
        }
    }

    private void propagateOxygenInRoom(ServerLevel level, BlockPos startPos) {
        positionsToCheck.clear();
        positionsToCheck.offer(startPos);
        Set<BlockPos> visited = new HashSet<>();
        OxygenDistributorBlockEntity distributor = getOxygenDistributor(level);
        DimensionOxygenManager dimensionManager = GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level.dimension());

        while (!positionsToCheck.isEmpty()) {
            BlockPos currentPos = positionsToCheck.poll();
            if (visited.add(currentPos) && isAirBlock(level, currentPos) && !distributor.takeOxygenFromTank()) {
                oxygenatedPositions.add(currentPos);
                if (isOnBorderBox(currentPos)) {
                    dimensionManager.addRoomToCheckIfOpen(currentPos, this);
                }

                for (Direction direction : Direction.values()) {
                    positionsToCheck.offer(currentPos.relative(direction));
                }
            }
        }
    }

    public void removeOxygenInRoom() {
        oxygenatedPositions.clear();
    }

    private boolean isOnBorderBox(BlockPos pos) {
        int dx = Math.abs(pos.getX() - distributorPos.getX());
        int dy = Math.abs(pos.getY() - distributorPos.getY());
        int dz = Math.abs(pos.getZ() - distributorPos.getZ());
        return dx == HALF_ROOM_SIZE || dy == HALF_ROOM_SIZE || dz == HALF_ROOM_SIZE;
    }

    public boolean hasOxygenAt(BlockPos pos) {
        return oxygenatedPositions.contains(pos);
    }
    private List<BlockPos> getAdjacentPositions(BlockPos pos) {
        List<BlockPos> adjacent = new ArrayList<>();
        adjacent.add(pos.above());
        adjacent.add(pos.below());
        adjacent.add(pos.north());
        adjacent.add(pos.south());
        adjacent.add(pos.east());
        adjacent.add(pos.west());

        return adjacent;
    }

    private boolean isAirBlock(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).isAir();
    }

    private OxygenDistributorBlockEntity getOxygenDistributor(ServerLevel level) {
        return (OxygenDistributorBlockEntity) level.getBlockEntity(distributorPos);
    }
}
