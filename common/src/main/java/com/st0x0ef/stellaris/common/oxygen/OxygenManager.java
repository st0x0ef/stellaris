package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenContainerBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenDistributorBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenPropagatorBlockEntity;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class OxygenManager {
    private static final Map<Level, List<OxygenContainerBlockEntity>> oxygenBlocksPerLevel = new HashMap<>();
    private static final Map<Level, List<Integer[]>> oxygenInformationsPerLevel = new HashMap<>(); // 0 : verified, 1 : x coord, 2 : y coord, 3 : z coord

    public static void distributeOxygenForLevel(Level level) {
        if (!PlanetUtil.getPlanet(level.dimension().location()).oxygen()) {
            if (!oxygenInformationsPerLevel.containsKey(level)) {
                oxygenInformationsPerLevel.put(level, new ArrayList<>());
            }

            if (!oxygenBlocksPerLevel.containsKey(level)) {
                oxygenBlocksPerLevel.put(level, new ArrayList<>());
            }
        }

        oxygenInformationsPerLevel.get(level).clear();

        oxygenBlocksPerLevel.get(level).forEach((container) -> {
            if (container instanceof OxygenDistributorBlockEntity distributor) {
                spread(level, distributor.getBlockPos(), false);
            } else if (container instanceof OxygenPropagatorBlockEntity propagator) {
                spread(level, propagator.getBlockPos(), true);
            }
        });

        removeUnclosedRoom(level);
    }

    public static void spread(Level level, BlockPos oxygenBlockPos, boolean isPropagator) {
        List<Integer[]> unverified = new ArrayList<>();

        if (isPropagator) {
            AtomicBoolean stop = new AtomicBoolean();
            oxygenBlocksPerLevel.get(level).forEach((oxygenBlock) -> {
                if (Mth.abs(oxygenBlockPos.getX() - oxygenBlock.getBlockPosition().getX()) < 16 && Mth.abs(oxygenBlockPos.getY() - oxygenBlock.getBlockPosition().getY()) < 16 && Mth.abs(oxygenBlockPos.getZ() - oxygenBlock.getBlockPosition().getZ()) < 16 && !stop.get()) {
                    unverified.add(new Integer[] {0, oxygenBlockPos.getX() + 1, oxygenBlockPos.getY(), oxygenBlockPos.getZ()});
                    unverified.add(new Integer[] {0, oxygenBlockPos.getX() - 1, oxygenBlockPos.getY(), oxygenBlockPos.getZ()});
                    unverified.add(new Integer[] {0, oxygenBlockPos.getX(), oxygenBlockPos.getY() + 1, oxygenBlockPos.getZ()});
                    unverified.add(new Integer[] {0, oxygenBlockPos.getX(), oxygenBlockPos.getY() - 1, oxygenBlockPos.getZ()});
                    unverified.add(new Integer[] {0, oxygenBlockPos.getX(), oxygenBlockPos.getY(), oxygenBlockPos.getZ() + 1});
                    unverified.add(new Integer[] {0, oxygenBlockPos.getX(), oxygenBlockPos.getY(), oxygenBlockPos.getZ() - 1});
                    stop.set(true);
                }
            });
        } else {
            unverified.add(new Integer[] {0, oxygenBlockPos.getX() + 1, oxygenBlockPos.getY(), oxygenBlockPos.getZ()});
            unverified.add(new Integer[] {0, oxygenBlockPos.getX() - 1, oxygenBlockPos.getY(), oxygenBlockPos.getZ()});
            unverified.add(new Integer[] {0, oxygenBlockPos.getX(), oxygenBlockPos.getY() + 1, oxygenBlockPos.getZ()});
            unverified.add(new Integer[] {0, oxygenBlockPos.getX(), oxygenBlockPos.getY() - 1, oxygenBlockPos.getZ()});
            unverified.add(new Integer[] {0, oxygenBlockPos.getX(), oxygenBlockPos.getY(), oxygenBlockPos.getZ() + 1});
            unverified.add(new Integer[] {0, oxygenBlockPos.getX(), oxygenBlockPos.getY(), oxygenBlockPos.getZ() - 1});
        }

        while (!unverified.isEmpty()) {
            Integer[] base = unverified.getFirst();
            BlockPos basePos = new BlockPos(base[1], base[2], base[3]);

            if (Mth.abs(oxygenBlockPos.getX() - basePos.getX()) < 16 && Mth.abs(oxygenBlockPos.getY() - basePos.getY()) < 16 && Mth.abs(oxygenBlockPos.getZ() - basePos.getZ()) < 16) {
                if (oxygenInformationsPerLevel.get(level).contains(base)) {
                    oxygenInformationsPerLevel.get(level).remove(base);
                    unverified.remove(base);
                    base[0] = 1;
                    oxygenInformationsPerLevel.get(level).add(base);
                    continue;
                }

                if (level.isEmptyBlock(basePos.east()) && !hasOxygenAt(level, basePos.east())) unverified.add(new Integer[] {0, oxygenBlockPos.getX() + 1, oxygenBlockPos.getY(), oxygenBlockPos.getZ()});
                if (level.isEmptyBlock(basePos.west()) && !hasOxygenAt(level, basePos.west())) unverified.add(new Integer[] {0, oxygenBlockPos.getX() - 1, oxygenBlockPos.getY(), oxygenBlockPos.getZ()});
                if (level.isEmptyBlock(basePos.above()) && !hasOxygenAt(level, basePos.above())) unverified.add(new Integer[] {0, oxygenBlockPos.getX(), oxygenBlockPos.getY() + 1, oxygenBlockPos.getZ()});
                if (level.isEmptyBlock(basePos.below()) && !hasOxygenAt(level, basePos.below())) unverified.add(new Integer[] {0, oxygenBlockPos.getX(), oxygenBlockPos.getY() - 1, oxygenBlockPos.getZ()});
                if (level.isEmptyBlock(basePos.south()) && !hasOxygenAt(level, basePos.south())) unverified.add(new Integer[] {0, oxygenBlockPos.getX(), oxygenBlockPos.getY(), oxygenBlockPos.getZ() + 1});
                if (level.isEmptyBlock(basePos.north()) && !hasOxygenAt(level, basePos.north())) unverified.add(new Integer[] {0, oxygenBlockPos.getX(), oxygenBlockPos.getY(), oxygenBlockPos.getZ() - 1});
                unverified.remove(base);
                base[0] = 1;
                oxygenInformationsPerLevel.get(level).add(base);
            } else {
                unverified.remove(base);
            }
        }
    }

    public static void removeUnclosedRoom(Level level) {
        List<Integer[]> newOxygenInfo = new ArrayList<>();

        AtomicBoolean stop = new AtomicBoolean();
        while (!stop.get()) {
            List<Integer[]> newOxygenInfoCopy = new ArrayList<>(newOxygenInfo);

            oxygenInformationsPerLevel.get(level).forEach((oxygenData) -> {
                if (oxygenData[0] == 0) {
                    BlockPos basePos = new BlockPos(oxygenData[1], oxygenData[2], oxygenData[3]);
                    if (level.isEmptyBlock(basePos.east())) oxygenInformationsPerLevel.get(level).set(oxygenInformationsPerLevel.get(level).indexOf(new Integer[] {1, basePos.getX() + 1, basePos.getY(), basePos.getZ()}), new Integer[] {0, basePos.getX() + 1, basePos.getY(), basePos.getZ()});
                    if (level.isEmptyBlock(basePos.west())) oxygenInformationsPerLevel.get(level).set(oxygenInformationsPerLevel.get(level).indexOf(new Integer[] {1, basePos.getX() - 1, basePos.getY(), basePos.getZ()}), new Integer[] {0, basePos.getX() - 1, basePos.getY(), basePos.getZ()});
                    if (level.isEmptyBlock(basePos.above())) oxygenInformationsPerLevel.get(level).set(oxygenInformationsPerLevel.get(level).indexOf(new Integer[] {1, basePos.getX(), basePos.getY() + 1, basePos.getZ()}), new Integer[] {0, basePos.getX(), basePos.getY() + 1, basePos.getZ()});
                    if (level.isEmptyBlock(basePos.below())) oxygenInformationsPerLevel.get(level).set(oxygenInformationsPerLevel.get(level).indexOf(new Integer[] {1, basePos.getX(), basePos.getY() - 1, basePos.getZ()}), new Integer[] {0, basePos.getX(), basePos.getY() - 1, basePos.getZ()});
                    if (level.isEmptyBlock(basePos.south())) oxygenInformationsPerLevel.get(level).set(oxygenInformationsPerLevel.get(level).indexOf(new Integer[] {1, basePos.getX(), basePos.getY(), basePos.getZ() + 1}), new Integer[] {0, basePos.getX(), basePos.getY(), basePos.getZ() + 1});
                    if (level.isEmptyBlock(basePos.north())) oxygenInformationsPerLevel.get(level).set(oxygenInformationsPerLevel.get(level).indexOf(new Integer[] {1, basePos.getX(), basePos.getY(), basePos.getZ() - 1}), new Integer[] {0, basePos.getX(), basePos.getY(), basePos.getZ() - 1});
                } else {
                    newOxygenInfo.add(oxygenData);
                }
            });

            stop.set(newOxygenInfoCopy.equals(newOxygenInfo));
        }

        oxygenInformationsPerLevel.get(level).clear();
        oxygenInformationsPerLevel.get(level).addAll(newOxygenInfo);
    }

    public static void addOxygenBlocksPerLevel(Level level, OxygenContainerBlockEntity oxygenContainer) {
        if (!oxygenBlocksPerLevel.containsKey(level)) {
            oxygenBlocksPerLevel.put(level, new ArrayList<>());
        }

        if (!oxygenBlocksPerLevel.get(level).contains(oxygenContainer)) {
            oxygenBlocksPerLevel.get(level).add(oxygenContainer);
            distributeOxygenForLevel(level);
        }
    }

    public static void removeOxygenBlocksPerLevel(Level level, OxygenContainerBlockEntity oxygenContainer) {
        oxygenBlocksPerLevel.get(level).remove(oxygenContainer);
    }

    public static boolean hasOxygenAt(Level level, BlockPos pos) {
        if (!oxygenInformationsPerLevel.containsKey(level)) {
            oxygenInformationsPerLevel.put(level, new ArrayList<>());
        }

        return oxygenInformationsPerLevel.get(level).contains(new Integer[] {1, pos.getX(), pos.getY(), pos.getZ()}) || oxygenInformationsPerLevel.get(level).contains(new Integer[] {0, pos.getX(), pos.getY(), pos.getZ()});
    }
}
