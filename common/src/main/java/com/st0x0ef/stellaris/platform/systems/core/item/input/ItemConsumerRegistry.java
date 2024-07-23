package com.st0x0ef.stellaris.platform.systems.core.item.input;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.st0x0ef.stellaris.platform.systems.core.item.input.consumers.CompoundConsumer;
import com.st0x0ef.stellaris.platform.systems.core.item.input.consumers.EnergyConsumer;
import com.st0x0ef.stellaris.platform.systems.core.item.input.consumers.FluidConsumer;
import com.st0x0ef.stellaris.platform.systems.core.item.input.consumers.SizedConsumer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ItemConsumerRegistry {
    public static final Map<ResourceLocation, ConsumerType<?>> CONSUMER_TYPES = new HashMap<>();
    public static final Codec<ConsumerType<?>> TYPE_CODEC = ResourceLocation.CODEC.comapFlatMap(ItemConsumerRegistry::decode, ConsumerType::id);
    public static final StreamCodec<ByteBuf, ConsumerType<?>> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(CONSUMER_TYPES::get, ConsumerType::id);

    static {
        register(SizedConsumer.TYPE);
        register(CompoundConsumer.TYPE);
        register(EnergyConsumer.TYPE);
        register(FluidConsumer.TYPE);
    }

    public static void register(ConsumerType<?> type) {
        CONSUMER_TYPES.put(type.id(), type);
    }

    public static void init() {
        // Called from Botarium#init
    }

    private static DataResult<? extends ConsumerType<?>> decode(ResourceLocation id) {
        return Optional.ofNullable(CONSUMER_TYPES.get(id)).map(DataResult::success).orElse(DataResult.error(() -> "No ritual component type found."));
    }
}
