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

    public OxygenRoom(ServerLevel level, BlockPos distributorPos) {
        this.distributorPos = distributorPos;
        this.oxygenatedPositions = new HashSet<>();
        this.level = level;
    }

    public BlockPos getDistributorPosition() {
        return distributorPos;
    }

    public OxygenDistributorBlockEntity getDistributorBlockEntity() {
        return level.getBlockEntity(distributorPos) instanceof OxygenDistributorBlockEntity distributor ? distributor : null;
    }

    public void tick() {
        OxygenDistributorBlockEntity distributor = getDistributorBlockEntity();
        if (distributor == null) {
            return;
        }

        Queue<BlockPos> toExplore = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>(oxygenatedPositions);
        Set<BlockPos> newPositions = new HashSet<>();

        for (Direction direction : Direction.values()) {
            BlockPos rel = distributorPos.relative(direction);
            if (!visited.contains(rel) && level.getBlockState(rel).isAir()) {
                toExplore.offer(rel);
                visited.add(rel);
            }
        }

        while (!toExplore.isEmpty()) {
            BlockPos currentPos = toExplore.poll();

            if (level.getBlockState(currentPos).isAir()) {
                newPositions.add(currentPos);

                for (Direction direction : Direction.values()) {
                    BlockPos rel = currentPos.relative(direction);

                    if (!visited.contains(rel)) {
                        visited.add(rel);

                        int HalfRoomSize = Stellaris.CONFIG.oxygenConfig.maxOxygenRoomSize / 2;
                        if (Math.abs(rel.getX() - distributorPos.getX()) > HalfRoomSize ||
                                Math.abs(rel.getY() - distributorPos.getY()) > HalfRoomSize ||
                                Math.abs(rel.getZ() - distributorPos.getZ()) > HalfRoomSize) {
                            GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).addRoomToCheckIfOpen(rel, this);
                        }
                        else if (level.getBlockState(rel).isAir()) {
                            toExplore.offer(rel);
                        }
                    }
                }
            }
        }

        for (BlockPos pos : newPositions) {
            if (!oxygenatedPositions.contains(pos) && distributor.useOxygenAndEnergy()) {
                oxygenatedPositions.add(pos);
                GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).removeRoomToCheckIfOpen(pos);
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