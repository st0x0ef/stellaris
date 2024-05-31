package com.st0x0ef.stellaris.common.energy;

import com.mojang.datafixers.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EnergyMain {
    public static final String STELLARIS_DATA = "StellarisData";

    public static <T, U> Map<T, U> finalizeRegistration(Map<Supplier<T>, U> unfinalized, @Nullable Map<T, U> finalized) {
        if (finalized == null) {
            Map<T, U> collected = unfinalized.entrySet().stream().map(entry -> Pair.of(entry.getKey().get(), entry.getValue())).collect(Collectors.toUnmodifiableMap(Pair::getFirst, Pair::getSecond));
            unfinalized.clear();
            return collected;
        }

        return finalized;
    }

}