package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.common.blocks.entities.machines.OxygenDistributorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

import java.util.*;

public class OxygenRoom {
    private final BlockPos distributorPos;
    public final List<BlockPos> oxygenatedPositions;
    private final ServerLevel level;
    private boolean isClosed;

    private static final int HALF_ROOM_SIZE = 16; // TODO : make this value configurable

    public OxygenRoom(ServerLevel level, BlockPos distributorPos) {
        this.distributorPos = distributorPos;
        this.oxygenatedPositions = new ArrayList<>();
        this.level = level;
        isClosed = false;
    }

    public OxygenRoom(ServerLevel level, BlockPos distributorPos, List<BlockPos> oxygenatedPositions) {
        this.distributorPos = distributorPos;
        this.oxygenatedPositions = oxygenatedPositions;
        this.level = level;
        isClosed = false;
    }

    public BlockPos getDistributorPosition() {
        return distributorPos;
    }

    public OxygenDistributorBlockEntity getDistributorBlockEntity() {
        return level.getBlockEntity(distributorPos) instanceof OxygenDistributorBlockEntity distributor ? distributor : null;
    }

    public void tick() {
        OxygenDistributorBlockEntity distributor = getDistributorBlockEntity();
        if (distributor == null) return;

        for (Direction direction : Direction.values()) {
            BlockPos rel = distributorPos.relative(direction);
            if (!oxygenatedPositions.contains(rel) && level.getBlockState(rel).isAir()) {
                if (distributor.useOxygenAndEnergy()) {
                    oxygenatedPositions.add(rel);
                    GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).removeRoomToCheckIfOpen(rel);
                }
            }
        }

        List<BlockPos> oxygenatedPositionsCopy = new ArrayList<>(oxygenatedPositions);

        for (BlockPos pos : oxygenatedPositionsCopy) {
            for (Direction direction : Direction.values()) {
                BlockPos rel = pos.relative(direction);

                if (Math.abs(rel.getX() - distributorPos.getX()) > HALF_ROOM_SIZE ||
                        Math.abs(rel.getY() - distributorPos.getY()) > HALF_ROOM_SIZE ||
                        Math.abs(rel.getZ() - distributorPos.getZ()) > HALF_ROOM_SIZE) {
                    GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).addRoomToCheckIfOpen(rel, this);
                } else if (!oxygenatedPositions.contains(rel) && level.getBlockState(rel).isAir()) {
                    if (distributor.useOxygenAndEnergy()) {
                        oxygenatedPositions.add(rel);
                        GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).removeRoomToCheckIfOpen(rel);
                    }
                }
            }
        }

        // We don't know yet, but if true, then the room is closed
        isClosed = oxygenatedPositions.size() == oxygenatedPositionsCopy.size();
    }

    public void removeOxygenInRoom() {
        if (!isClosed) oxygenatedPositions.clear();
    }

    public boolean hasOxygenAt(BlockPos pos) {
        return oxygenatedPositions.contains(pos);
    }


    public boolean breathOxygenAt(BlockPos pos) {
        if (hasOxygenAt(pos) && isClosed) {
            OxygenDistributorBlockEntity distributor = getDistributorBlockEntity();
            if (getDistributorBlockEntity() == null || !distributor.useOxygenAndEnergy()) {
                oxygenatedPositions.remove(pos);
            }
            return true;
        }
        return false;
    }
}