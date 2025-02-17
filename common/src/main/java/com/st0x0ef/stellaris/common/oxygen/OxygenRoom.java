package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.entities.machines.OxygenDistributorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

import java.util.*;

public class OxygenRoom {
    private final BlockPos distributorPos;
    public final Set<BlockPos> oxygenatedPositions;
    private final ServerLevel level;

    private static final int HALF_ROOM_SIZE = 16; // TODO : make this value configurable

    public OxygenRoom(ServerLevel level, BlockPos distributorPos) {
        this.distributorPos = distributorPos;
        this.oxygenatedPositions = new LinkedHashSet<>();
        this.level = level;
    }

    public BlockPos getDistributorPosition() {
        return distributorPos;
    }

    public OxygenDistributorBlockEntity getDistributorBlockEntity() {
        return level.getBlockEntity(distributorPos) instanceof OxygenDistributorBlockEntity distributor ? distributor : null;
    }

    public void updateOxygenRoom() {
        OxygenDistributorBlockEntity distributor = getDistributorBlockEntity();

        if (distributor == null) return;

        oxygenatedPositions.clear();

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> positionsToCheck = new LinkedList<>();

        for (Direction direction : Direction.values()) {
            positionsToCheck.offer(distributorPos.relative(direction));
        }

        Stellaris.LOG.error("size 1 : {}", positionsToCheck.size());

        while (!positionsToCheck.isEmpty()) {
            Stellaris.LOG.error("size : {}", positionsToCheck.size());

            BlockPos currentPos = positionsToCheck.poll();
            visited.add(currentPos);
            if (level.getBlockState(currentPos).isAir()) {
                if (distributor.useOxygenAndEnergy()) {
                    oxygenatedPositions.add(currentPos);
                    Stellaris.LOG.error(currentPos.toString());

                    if (Math.abs(currentPos.getX() - distributorPos.getX()) > HALF_ROOM_SIZE ||
                            Math.abs(currentPos.getY() - distributorPos.getY()) > HALF_ROOM_SIZE ||
                            Math.abs(currentPos.getZ() - distributorPos.getZ()) > HALF_ROOM_SIZE) {
                        GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).addRoomToCheckIfOpen(currentPos, this);
                    }

                    for (Direction direction : Direction.values()) {
                        BlockPos relativePos = currentPos.relative(direction);
                        if (Math.abs(relativePos.getX() - distributorPos.getX()) <= HALF_ROOM_SIZE &&
                                Math.abs(relativePos.getY() - distributorPos.getY()) <= HALF_ROOM_SIZE &&
                                Math.abs(relativePos.getZ() - distributorPos.getZ()) <= HALF_ROOM_SIZE) {
                            if (!visited.contains(relativePos)) {
                                positionsToCheck.offer(relativePos);
                            }
                        }
                    }
                }
            }
        }
    }

    public void removeOxygenInRoom() {
        oxygenatedPositions.clear();
    }

    public boolean hasOxygenAt(BlockPos pos) {
        return oxygenatedPositions.contains(pos);
    }


    public boolean breathOxygenAt(BlockPos pos) {
        if (hasOxygenAt(pos)) {
            OxygenDistributorBlockEntity distributor = getDistributorBlockEntity();
            if (getDistributorBlockEntity() == null || !distributor.useOxygenAndEnergy()) {
                oxygenatedPositions.remove(pos);
            }
            return true;
        }
        return false;
    }
}