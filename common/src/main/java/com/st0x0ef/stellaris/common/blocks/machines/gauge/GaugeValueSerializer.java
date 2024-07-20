package com.st0x0ef.stellaris.common.blocks.machines.gauge;

import com.st0x0ef.stellaris.Stellaris;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class GaugeValueSerializer<T extends GaugeSerializable> {
    public static final GaugeValueSerializer Serializer = new GaugeValueSerializer<>();

    static {
        Serializer.addCodec(new ResourceLocation(Stellaris.MODID, "fluidstack"), GaugeValueFluidStack.class);
        Serializer.addCodec(new ResourceLocation(Stellaris.MODID, "simple"), GaugeValueSimple.class);
    }

    private final Map<ResourceLocation, Class<? extends T>> locationClassMap = new LinkedHashMap<>();
    private final Map<Class<? extends T>, ResourceLocation> classLocationMap = new LinkedHashMap<>();

    private GaugeValueSerializer() {}

    public T deserialize(CompoundTag compound, HolderLookup.Provider provider) {
        String locationToString = compound.getString("location");
        CompoundTag valueNBT = compound.getCompound("value");
        Class<? extends T> clazz = locationClassMap.get(new ResourceLocation(locationToString));

        if (clazz == null) {
            throw new IllegalArgumentException("Unknown class for location: " + locationToString);
        }

        try {
            T format = clazz.getDeclaredConstructor().newInstance();
            format.deserialize(valueNBT);
            return format;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalArgumentException("Failed to deserialize object", e);
        }
    }

    public void write(T format, FriendlyByteBuf buffer, HolderLookup.Provider provider) {
        buffer.writeNbt(serialize(format, provider));
    }

    public CompoundTag serialize(T format, HolderLookup.Provider provider) {
        CompoundTag compound = new CompoundTag();
        compound.putString("location", classLocationMap.get(format.getClass()).toString());
        compound.put("value", format.serialize(new CompoundTag())); // Using the Serializable interface method
        return compound;
    }
    public IGaugeValue read(FriendlyByteBuf buffer, HolderLookup.Provider provider) {
        CompoundTag compound = buffer.readNbt();
        if (compound == null) {
            throw new IllegalArgumentException("Received null compound tag while reading from buffer.");
        }
        return (IGaugeValue) deserialize(compound, provider);
    }


    public void addCodec(ResourceLocation location, Class<? extends T> clazz) {
        locationClassMap.put(location, clazz);
        classLocationMap.put(clazz, location);
    }
}