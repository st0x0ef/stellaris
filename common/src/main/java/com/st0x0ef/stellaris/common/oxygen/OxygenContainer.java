package com.st0x0ef.stellaris.common.oxygen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class OxygenContainer {
    private final int maxOxygen;
    private int[][][] oxygenRoom = new int[32][32][32];

    public OxygenContainer(int maxOxygen) {
        this.maxOxygen = maxOxygen;
    }

    public boolean removeOxygenAt(BlockPos pos, boolean simulate) {
        if (getOxygenAt(pos) > 0) {
            if (!simulate) {
                oxygenRoom[pos.getX()][pos.getY()][pos.getZ()]--;
            }
            return true;
        }

        return false;
    }

    public boolean removeOxygenAt(int x, int y, int z, boolean simulate) {
        return removeOxygenAt(new BlockPos(x, y, z), simulate);
    }

    public boolean addOxygenAt(BlockPos pos, boolean simulate) {
        if (getOxygenAt(pos) < maxOxygen) {
            if (!simulate) {
                oxygenRoom[pos.getX()][pos.getY()][pos.getZ()]++;
            }
            return true;
        }

        return false;
    }

    public void tick(Level level) {
        int[][][] oxygenCopy = oxygenRoom.clone();

        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {
                for (int z = 0; z < 32; z++) {
                    if (oxygenRoom[x][y][z] >= 1) {
                        if (oxygenRoom[x+1][y][z] < maxOxygen && level.isEmptyBlock(new BlockPos(x + 1, y, z))) { oxygenCopy[x+1][y][z]++; }
                        if (oxygenRoom[x-1][y][z] < maxOxygen && level.isEmptyBlock(new BlockPos(x - 1, y, z))) { oxygenCopy[x-1][y][z]++; }
                        if (oxygenRoom[x][y+1][z] < maxOxygen && level.isEmptyBlock(new BlockPos(x, y + 1, z))) { oxygenCopy[x][y+1][z]++; }
                        if (oxygenRoom[x][y-1][z] < maxOxygen && level.isEmptyBlock(new BlockPos(x, y - 1, z))) { oxygenCopy[x][y-1][z]++; }
                        if (oxygenRoom[x][y][z+1] < maxOxygen && level.isEmptyBlock(new BlockPos(x, y, z + 1))) { oxygenCopy[x][y][z+1]++; }
                        if (oxygenRoom[x][y][z-1] < maxOxygen && level.isEmptyBlock(new BlockPos(x, y, z - 1))) { oxygenCopy[x][y][z-1]++; }
                    }
                }
            }
        }

        oxygenRoom = oxygenCopy;
    }

    public int getOxygenAt(BlockPos pos) {
        return oxygenRoom[pos.getX()][pos.getY()][pos.getZ()];
    }
}
