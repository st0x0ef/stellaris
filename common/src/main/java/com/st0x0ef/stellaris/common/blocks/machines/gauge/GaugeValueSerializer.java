package com.st0x0ef.stellaris.common.blocks.machines.gauge;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.energy.util.Serializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.UnknownNullability;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class GaugeValueSerializer<T extends Serializable> {
    public static final GaugeValueSerializer Serializer = new GaugeValueSerializer<>();

    static {
        Serializer.addCodec(new ResourceLocation(Stellaris.MODID, "fluidstack"), GaugeValueFluidStack.class);
        Serializer.addCodec(new ResourceLocation(Stellaris.MODID, "simple"), GaugeValueSimple.class);
    }

    private final Map<ResourceLocation, Class<? extends T>> locationClassMap = new LinkedHashMap<>();
    private final Map<Class<? extends T>, ResourceLocation> classLocationMap = new LinkedHashMap<>();

    private GaugeValueSerializer() {}

    public T deserialize(CompoundTag compound) {
        String locationToString = compound.getString("location");
        CompoundTag valueNBT = compound.getCompound("value");
        Class<? extends T> clazz = locationClassMap.get(new ResourceLocation(locationToString));

        if (clazz == null) {
            throw new IllegalArgumentException("Unknown class for location: " + locationToString);
        }

        try {
            T format = clazz.getDeclaredConstructor().newInstance();
            format.deserialize(valueNBT); // Using the Serializable interface method
            return format;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalArgumentException("Failed to deserialize object", e);
        }
    }

    public void write(T format, FriendlyByteBuf buffer) {
        buffer.writeNbt(serialize(format));
    }

    public CompoundTag serialize(T format) {
        CompoundTag compound = new CompoundTag();
        compound.putString("location", classLocationMap.get(format.getClass()).toString());
        compound.put("value", format.serialize(new CompoundTag())); // Using the Serializable interface method
        return compound;
    }

    public void addCodec(ResourceLocation location, Class<? extends T> clazz) {
        locationClassMap.put(location, clazz);
        classLocationMap.put(clazz, location);
    }
}