package com.st0x0ef.stellaris.common.oxygen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.SimpleMapCodec;
import com.st0x0ef.stellaris.common.armors.JetSuit;
import com.st0x0ef.stellaris.common.blocks.machines.RadioactiveGeneratorBlock;
import com.st0x0ef.stellaris.common.items.oxygen.OxygenContainerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;

import static net.minecraft.world.level.block.state.BlockBehaviour.simpleCodec;

public class OxygenContainer {

    private final int maxOxygen;

    private int oxygenStored;

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

    public boolean addOxygenAtFromSource(BlockPos pos, boolean simulate, OxygenContainer container) {
        if (container.removeOxygenStored(true)) {
            if (addOxygenAt(pos, simulate)) {
                return container.removeOxygenStored(simulate);
            }
        }

        return false;
    }

    public boolean addOxygenAt(BlockPos pos, boolean simulate) {
        if (getOxygenAt(pos) < OxygenUtils.maxOxygenByBlock) {
            if (removeOxygenStored(simulate)) {
                if (!simulate) {
                    oxygenRoom[pos.getX()][pos.getY()][pos.getZ()]++;
                }
                return true;
            }
        }

        return false;
    }

    public boolean addOxygenAt(int x, int y, int z, boolean simulate) {
        return addOxygenAt(new BlockPos(x, y, z), simulate);
    }

    public void tick(Level level) {
        int[][][] oxygenCopy = oxygenRoom.clone();

        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {
                for (int z = 0; z < 32; z++) {
                    if (oxygenCopy[x][y][z] >= 1) {
                        if (oxygenCopy[x+1][y][z] < OxygenUtils.maxOxygenByBlock && level.isEmptyBlock(new BlockPos(x + 1, y, z))) { addOxygenAt(x + 1, y, z, false); }
                        if (oxygenCopy[x-1][y][z] < OxygenUtils.maxOxygenByBlock && level.isEmptyBlock(new BlockPos(x - 1, y, z))) { addOxygenAt(x - 1, y, z, false); }
                        if (oxygenCopy[x][y+1][z] < OxygenUtils.maxOxygenByBlock && level.isEmptyBlock(new BlockPos(x, y + 1, z))) { addOxygenAt(x, y + 1, z, false); }
                        if (oxygenCopy[x][y-1][z] < OxygenUtils.maxOxygenByBlock && level.isEmptyBlock(new BlockPos(x, y - 1, z))) { addOxygenAt(x, y - 1, z, false); }
                        if (oxygenCopy[x][y][z+1] < OxygenUtils.maxOxygenByBlock && level.isEmptyBlock(new BlockPos(x, y, z + 1))) { addOxygenAt(x, y, z + 1, false); }
                        if (oxygenCopy[x][y][z-1] < OxygenUtils.maxOxygenByBlock && level.isEmptyBlock(new BlockPos(x, y, z - 1))) { addOxygenAt(x, y, z - 1, false); }
                    }
                }
            }
        }

        oxygenRoom = oxygenCopy;
    }

    public int getOxygenAt(BlockPos pos) {
        try {
            return oxygenRoom[pos.getX()][pos.getY()][pos.getZ()];
        }
        catch (Exception e) {
            return 0;
        }
    }

    public int getOxygenStored() {
        return oxygenStored;
    }

    public void removeAllOxygenStored() {
        oxygenStored = 0;
    }

    public boolean removeOxygenStored(boolean simulate) {
        if (getOxygenStored() > 0) {
            if (!simulate) {
                oxygenStored--;
            }
            return true;
        }
        return false;
    }

    public boolean addOxygenStored(OxygenContainerItem container, boolean simulate) {
        int oxygenToAdd = container.getOxygenContainer().getOxygenStored();
        if (oxygenToAdd + getOxygenStored() <= maxOxygen) {
            if (!simulate) {
                oxygenStored += oxygenToAdd;
                container.getOxygenContainer().removeAllOxygenStored();
            }

            return true;
        }
        return false;
    }
}
