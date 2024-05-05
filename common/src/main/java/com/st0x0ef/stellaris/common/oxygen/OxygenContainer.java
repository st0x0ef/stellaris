package com.st0x0ef.stellaris.common.oxygen;

import net.minecraft.core.BlockPos;

public class OxygenContainer {
    private final int maxOxygen;
    private int[][][] oxygenRoom = new int[32][32][32];

    public OxygenContainer(int maxOxygen) {
        this.maxOxygen = maxOxygen;
    }

    public boolean removeOxygenAt(BlockPos pos, boolean simulate) {
        if (getOxygen(pos) > 0) {
            if (!simulate) {
                oxygenRoom[pos.getX()][pos.getY()][pos.getZ()]--;
            }
            return true;
        }

        return false;
    }

    public boolean addOxygenAt(BlockPos pos, boolean simulate) {
        if (getOxygen(pos) < maxOxygen) {
            if (!simulate) {
                oxygenRoom[pos.getX()][pos.getY()][pos.getZ()]++;
            }
            return true;
        }

        return false;
    }

    public void tick() {
        int[][][] oxygenCopy = oxygenRoom.clone();

        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {
                for (int z = 0; z < 32; z++) {
                    if (oxygenRoom[x][y][z] >= 1) {
                        if (oxygenRoom[x+1][y][z] < maxOxygen) { oxygenCopy[x+1][y][z]++; }
                        if (oxygenRoom[x-1][y][z] < maxOxygen) { oxygenCopy[x-1][y][z]++; }
                        if (oxygenRoom[x][y+1][z] < maxOxygen) { oxygenCopy[x][y+1][z]++; }
                        if (oxygenRoom[x][y-1][z] < maxOxygen) { oxygenCopy[x][y-1][z]++; }
                        if (oxygenRoom[x][y][z+1] < maxOxygen) { oxygenCopy[x][y][z+1]++; }
                        if (oxygenRoom[x][y][z-1] < maxOxygen) { oxygenCopy[x][y][z-1]++; }
                    }
                }
            }
        }

        oxygenRoom = oxygenCopy;
    }
    public int getOxygen(BlockPos pos) {
        return oxygenRoom[pos.getX()][pos.getY()][pos.getZ()];
    }
}
