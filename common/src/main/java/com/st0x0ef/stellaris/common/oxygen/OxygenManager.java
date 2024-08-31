package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenContainerBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenDistributorBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenPropagatorBlockEntity;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class OxygenManager {
    private static final Map<Level, List<OxygenContainerBlockEntity>> oxygenBlocksPerLevel = new HashMap<>();
    private static final Map<Level, List<Integer[]>> oxygenInformationsPerLevel = new HashMap<>();
    private static final Set<Level> activeLevels = Collections.synchronizedSet(new HashSet<>());

    private static final Set<Level> closingLevels = Collections.synchronizedSet(new HashSet<>());

    public static void distributeOxygenForLevel(Level level) {
        if (!activeLevels.contains(level)) {
            return;
        }

        if (closingLevels.contains(level)) {
            return;
        }

        if (!PlanetUtil.getPlanet(level.dimension().location()).oxygen()) {
            oxygenInformationsPerLevel.putIfAbsent(level, new ArrayList<>());
            oxygenBlocksPerLevel.putIfAbsent(level, new ArrayList<>());
        }

        List<Integer[]> oxygenInfo = oxygenInformationsPerLevel.get(level);
        if (oxygenInfo != null) {
            oxygenInfo.clear();
        } else {
            return;
        }

        for (OxygenContainerBlockEntity container : new ArrayList<>(oxygenBlocksPerLevel.get(level))) {
            if (container instanceof OxygenDistributorBlockEntity distributor) {
                spread(level, distributor.getBlockPos(), false);
            } else if (container instanceof OxygenPropagatorBlockEntity propagator) {
                spread(level, propagator.getBlockPos(), true);
            }
        }

        removeUnclosedRoom(level);
    }

    public static void spread(Level level, BlockPos oxygenBlockPos, boolean isPropagator) {
        List<Integer[]> unverified = new LinkedList<>();
        addAdjacentPositions(unverified, oxygenBlockPos);

        if (isPropagator) {
            AtomicBoolean stop = new AtomicBoolean(false);
            for (OxygenContainerBlockEntity oxygenBlock : oxygenBlocksPerLevel.get(level)) {
                if (withinRange(oxygenBlockPos, oxygenBlock.getBlockPosition())) {
                    stop.set(true);
                    break;
                }
            }
        }

        while (!unverified.isEmpty()) {
            Integer[] base = unverified.remove(0);
            BlockPos basePos = new BlockPos(base[1], base[2], base[3]);

            if (withinRange(oxygenBlockPos, basePos)) {
                if (oxygenInformationsPerLevel.get(level).contains(base)) {
                    continue;
                }

                exploreAdjacentPositions(unverified, basePos, level);
                base[0] = 1;
                oxygenInformationsPerLevel.get(level).add(base);
            }
        }
    }

    private static boolean withinRange(BlockPos pos1, BlockPos pos2) {
        return Mth.abs(pos1.getX() - pos2.getX()) < 16 &&
                Mth.abs(pos1.getY() - pos2.getY()) < 16 &&
                Mth.abs(pos1.getZ() - pos2.getZ()) < 16;
    }

    private static void addAdjacentPositions(List<Integer[]> positions, BlockPos basePos) {
        positions.add(new Integer[]{0, basePos.getX() + 1, basePos.getY(), basePos.getZ()});
        positions.add(new Integer[]{0, basePos.getX() - 1, basePos.getY(), basePos.getZ()});
        positions.add(new Integer[]{0, basePos.getX(), basePos.getY() + 1, basePos.getZ()});
        positions.add(new Integer[]{0, basePos.getX(), basePos.getY() - 1, basePos.getZ()});
        positions.add(new Integer[]{0, basePos.getX(), basePos.getY(), basePos.getZ() + 1});
        positions.add(new Integer[]{0, basePos.getX(), basePos.getY(), basePos.getZ() - 1});
    }

    private static void exploreAdjacentPositions(List<Integer[]> unverified, BlockPos basePos, Level level) {
        if (level.isEmptyBlock(basePos.east()) && !hasOxygenAt(level, basePos.east()))
            unverified.add(new Integer[]{0, basePos.getX() + 1, basePos.getY(), basePos.getZ()});
        if (level.isEmptyBlock(basePos.west()) && !hasOxygenAt(level, basePos.west()))
            unverified.add(new Integer[]{0, basePos.getX() - 1, basePos.getY(), basePos.getZ()});
        if (level.isEmptyBlock(basePos.above()) && !hasOxygenAt(level, basePos.above()))
            unverified.add(new Integer[]{0, basePos.getX(), basePos.getY() + 1, basePos.getZ()});
        if (level.isEmptyBlock(basePos.below()) && !hasOxygenAt(level, basePos.below()))
            unverified.add(new Integer[]{0, basePos.getX(), basePos.getY() - 1, basePos.getZ()});
        if (level.isEmptyBlock(basePos.south()) && !hasOxygenAt(level, basePos.south()))
            unverified.add(new Integer[]{0, basePos.getX(), basePos.getY(), basePos.getZ() + 1});
        if (level.isEmptyBlock(basePos.north()) && !hasOxygenAt(level, basePos.north()))
            unverified.add(new Integer[]{0, basePos.getX(), basePos.getY(), basePos.getZ() - 1});
    }

    public static void removeUnclosedRoom(Level level) {
        List<Integer[]> newOxygenInfo = new ArrayList<>();
        AtomicBoolean stop = new AtomicBoolean(false);

        while (!stop.get()) {
            List<Integer[]> newOxygenInfoCopy = new ArrayList<>(newOxygenInfo);

            for (Integer[] oxygenData : oxygenInformationsPerLevel.get(level)) {
                if (oxygenData[0] == 0) {
                    BlockPos basePos = new BlockPos(oxygenData[1], oxygenData[2], oxygenData[3]);
                    updateOxygenInformation(level, basePos);
                } else {
                    newOxygenInfo.add(oxygenData);
                }
            }

            stop.set(newOxygenInfoCopy.equals(newOxygenInfo));
        }

        oxygenInformationsPerLevel.get(level).clear();
        oxygenInformationsPerLevel.get(level).addAll(newOxygenInfo);
    }

    private static void updateOxygenInformation(Level level, BlockPos basePos) {
        if (level.isEmptyBlock(basePos.east()))
            updateOxygenStatus(level, basePos.east());
        if (level.isEmptyBlock(basePos.west()))
            updateOxygenStatus(level, basePos.west());
        if (level.isEmptyBlock(basePos.above()))
            updateOxygenStatus(level, basePos.above());
        if (level.isEmptyBlock(basePos.below()))
            updateOxygenStatus(level, basePos.below());
        if (level.isEmptyBlock(basePos.south()))
            updateOxygenStatus(level, basePos.south());
        if (level.isEmptyBlock(basePos.north()))
            updateOxygenStatus(level, basePos.north());
    }

    private static void updateOxygenStatus(Level level, BlockPos pos) {
        int index = oxygenInformationsPerLevel.get(level).indexOf(new Integer[]{1, pos.getX(), pos.getY(), pos.getZ()});
        if (index != -1) {
            oxygenInformationsPerLevel.get(level).set(index, new Integer[]{0, pos.getX(), pos.getY(), pos.getZ()});
        }
    }

    public static void addOxygenBlocksPerLevel(Level level, OxygenContainerBlockEntity oxygenContainer) {
        activeLevels.add(level);

        oxygenBlocksPerLevel.computeIfAbsent(level, k -> new ArrayList<>());

        if (!oxygenBlocksPerLevel.get(level).contains(oxygenContainer)) {
            oxygenBlocksPerLevel.get(level).add(oxygenContainer);
            distributeOxygenForLevel(level);
        }
    }

    public static void removeOxygenBlocksPerLevel(Level level, OxygenContainerBlockEntity oxygenContainer) {
        oxygenBlocksPerLevel.get(level).remove(oxygenContainer);

        if (oxygenBlocksPerLevel.get(level).isEmpty()) {
            activeLevels.remove(level);
        }
    }

    public static boolean hasOxygenAt(Level level, BlockPos pos) {
        if (!oxygenInformationsPerLevel.containsKey(level)) {
            return false;
        }

        List<Integer[]> oxygenInfo = oxygenInformationsPerLevel.get(level);
        return oxygenInfo != null && (oxygenInfo.contains(new Integer[]{1, pos.getX(), pos.getY(), pos.getZ()}) ||
                oxygenInfo.contains(new Integer[]{0, pos.getX(), pos.getY(), pos.getZ()}));
    }

    public static void markLevelAsClosing(Level level) {
        closingLevels.add(level);
    }

    public static void unmarkLevelAsClosing(Level level) {
        closingLevels.remove(level);
    }
}
