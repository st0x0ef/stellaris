package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.common.blocks.entities.machines.OxygenDistributorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

import java.util.*;
import java.util.stream.IntStream;

public class OxygenRoom {
    private final BlockPos distributorPos;
    private final Set<BlockPos> oxygenatedPositions;
    private final Queue<BlockPos> positionsToCheck;

    private static final int HALF_ROOM_SIZE = 16;

    public OxygenRoom(BlockPos distributorPos) {
        this.distributorPos = distributorPos;
        this.oxygenatedPositions = new LinkedHashSet<>();
        this.positionsToCheck = new LinkedList<>();
    }

    public BlockPos getGeneratorPosition() {
        return distributorPos;
    }

    public void updateOxygenRoom(ServerLevel level) {


        if (GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).doesPlanetHasOxygen() || !getOxygenDistributor(level).takeOxygenFromTank()) {
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
        DimensionOxygenManager dimensionManager = GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level);

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

    private boolean isAirBlock(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).isAir();
    }

    private OxygenDistributorBlockEntity getOxygenDistributor(ServerLevel level) {
        return (OxygenDistributorBlockEntity) level.getBlockEntity(distributorPos);
    }

    public int[] toIntArray() {
        return oxygenatedPositions.stream()
                .flatMapToInt(pos -> IntStream.of(pos.getX(), pos.getY(), pos.getZ()))
                .toArray();
    }

    public Set<BlockPos> fromIntArray(int[] array) {
        Set<BlockPos> positions = new LinkedHashSet<>();
        for (int i = 0; i < array.length; i += 3) {
            int x = array[i];
            int y = array[i + 1];
            int z = array[i + 2];
            positions.add(new BlockPos(x, y, z));
        }
        return positions;
    }
}
