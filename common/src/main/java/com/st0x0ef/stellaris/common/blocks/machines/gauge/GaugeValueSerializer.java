package com.st0x0ef.stellaris.common.blocks.machines.gauge;

import com.st0x0ef.stellaris.Stellaris;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class GaugeValueSerializer<T extends INBTSerializable<CompoundTag>> {
    public static final GaugeValueSerializer<IGaugeValue> Serializer = new GaugeValueSerializer<>();

    static {
        Serializer.addCodec(new ResourceLocation(Stellaris.MODID, "fluidstack"), GaugeValueFluidStack.class);
        Serializer.addCodec(new ResourceLocation(Stellaris.MODID, "simple"), GaugeValueSimple.class);
    }

    private final Map<ResourceLocation, Class<? extends T>> location_class_map = new LinkedHashMap<>();
    private final Map<Class<? extends T>, ResourceLocation> class_location_map = new LinkedHashMap<>();

    private GaugeValueSerializer() {

    }

    public T deserialize(CompoundTag compound) {
        String locationToString = compound.getString("location");
        CompoundTag valueNBT = compound.getCompound("value");
        Class<? extends T> clazz = this.location_class_map.get(new ResourceLocation(locationToString));

        T format = null;

        try {
            format = clazz.getConstructor().newInstance();
            format.deserializeNBT(valueNBT);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return format;
    }

    public T read(FriendlyByteBuf buffer) {
        return deserialize(buffer.readNbt());
    }

    public CompoundTag serialize(T format) {
        CompoundTag compound = new CompoundTag();
        compound.putString("location", this.class_location_map.get(format.getClass()).toString());
        compound.put("value", format.serializeNBT());
        return compound;
    }

    public void write(T format, FriendlyByteBuf buffer) {
        buffer.writeNbt(this.serialize(format));
    }

    public void addCodec(ResourceLocation location, Class<? extends T> clazz) {
        this.location_class_map.put(location, clazz);
        this.class_location_map.put(clazz, location);
    }
}
