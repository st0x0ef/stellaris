package com.st0x0ef.stellaris.platform.systems.resources.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class CodecUtils {
    public static <T> MapCodec<T> aliasedFieldOf(final Codec<T> codec, final String... names) {
        if (names.length == 0)
            throw new IllegalArgumentException("Must have at least one name!");
        MapCodec<T> mapCodec = codec.fieldOf(names[0]);
        for (int i = 1; i < names.length; i++)
            mapCodec = mapWithAlternative(mapCodec, codec.fieldOf(names[i]));
        return mapCodec;
    }

    /**
     * Similar to {@link Codec#optionalFieldOf(String, Object)}, except that the default value is always written.
     */
    public static <T> MapCodec<T> optionalFieldAlwaysWrite(Codec<T> codec, String name, T defaultValue) {
        return codec.optionalFieldOf(name).xmap(o -> o.orElse(defaultValue), Optional::of);
    }

    public static <T> MapCodec<T> mapWithAlternative(final MapCodec<T> mapCodec, final MapCodec<? extends T> alternative) {
        return Codec.mapEither(mapCodec, alternative).xmap(either -> either.map(Function.identity(), Function.identity()), Either::left);
    }

    public static <A> Codec<List<A>> listAndObjectCodec(Codec<A> codec) {
        return Codec.either(codec, codec.listOf()).xmap(either -> either.map(List::of, Function.identity()), list -> list.size() == 1 ? Either.left(list.getFirst()) : Either.right(list));
    }

    public static <F, S> MapCodec<Either<F, S>> xor(MapCodec<F> first, MapCodec<S> second) {
        return new XorMapCodec<>(first, second);
    }

    final static class XorMapCodec<F, S> extends MapCodec<Either<F, S>> {
        private final MapCodec<F> first;
        private final MapCodec<S> second;

        private XorMapCodec(MapCodec<F> first, MapCodec<S> second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public <T> Stream<T> keys(DynamicOps<T> ops) {
            return Stream.concat(first.keys(ops), second.keys(ops)).distinct();
        }

        @Override
        public <T> DataResult<Either<F, S>> decode(DynamicOps<T> ops, MapLike<T> input) {
            DataResult<Either<F, S>> firstResult = first.decode(ops, input).map(Either::left);
            DataResult<Either<F, S>> secondResult = second.decode(ops, input).map(Either::right);
            var firstValue = firstResult.result();
            var secondValue = secondResult.result();
            if (firstValue.isPresent() && secondValue.isPresent()) {
                return DataResult.error(
                        () -> "Both alternatives read successfully, cannot pick the correct one; first: " + firstValue.get() + " second: "
                                + secondValue.get(),
                        firstValue.get());
            } else if (firstValue.isPresent()) {
                return firstResult;
            } else if (secondValue.isPresent()) {
                return secondResult;
            } else {
                return firstResult.apply2((x, y) -> y, secondResult);
            }
        }

        @Override
        public <T> RecordBuilder<T> encode(Either<F, S> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            return input.map(x -> first.encode(x, ops, prefix), x -> second.encode(x, ops, prefix));
        }

        @Override
        public String toString() {
            return "XorMapCodec[" + first + ", " + second + "]";
        }
    }
}
