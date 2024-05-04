package com.st0x0ef.stellaris.common.blocks.machines.gauge;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class GaugeValueSimple implements IGaugeValue
{
    public static final int FALLBACK_COLOR = 0xA0FFFFFF;

    private ResourceLocation name;
    private long amount;
    private long capacity;
    private Component displayeName;
    private String unit;
    private int color;
    private boolean reverse;

    private Component displayNameCache;

    public GaugeValueSimple() {
        this(null);
    }

    public GaugeValueSimple(ResourceLocation name) {
        this(name, 0, 0);
    }

    public GaugeValueSimple(ResourceLocation name, long amount, long capacity) {
        this(name, amount, capacity, null);
    }

    public GaugeValueSimple(ResourceLocation name, long amount, long capacity, @Nullable Component displayeName) {
        this(name, amount, capacity, displayeName, "");
    }

    public GaugeValueSimple(ResourceLocation name, long amount, long capacity, @Nullable Component displayeName, String unit) {
        this(name, amount, capacity, displayeName, unit, FALLBACK_COLOR);
    }

    public GaugeValueSimple(ResourceLocation name, long amount, long capacity, @Nullable Component displayeName,String unit, int color) {
        this.name = name;
        this.amount = amount;
        this.capacity = capacity;
        this.displayeName = displayeName;
        this.unit = unit;
        this.color = color;
    }


    public ResourceLocation getName() {
        return name;
    }

    public GaugeValueSimple name(ResourceLocation name) {
        this.name = name;
        return this;
    }

    @Override
    @Nullable
    public Component getDisplayName() {
        if (this.displayeName != null) {
            return this.displayeName;
        } else if (this.displayNameCache == null) {
            this.displayNameCache = this.createDefaultTextComponent();
        }

        return this.displayNameCache;
    }

    protected Component createDefaultTextComponent() {
        return Component.translatable(GaugeValueHelper.makeTranslationKey(this.getName()));
    }

    public GaugeValueSimple displayeName(Component displayeName) {
        this.displayeName = displayeName;
        return this;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    public GaugeValueSimple unit(String unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    public GaugeValueSimple amount(long amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    public GaugeValueSimple capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    @Override
    public int getColor() {
        return color;
    }

    public GaugeValueSimple color(int color) {
        this.color = color;
        return this;
    }

    @Override
    public boolean isReverse() {
        return this.reverse;
    }

    public GaugeValueSimple reverse(boolean reverse) {
        this.reverse = reverse;
        return this;
    }
    @Override
    public CompoundTag serialize(CompoundTag nbt, HolderLookup.Provider provider) {
        CompoundTag compound = new CompoundTag();
        compound.putString("name", this.getName().toString());
        compound.putLong("amount", this.getAmount());
        compound.putLong("capacity", this.getCapacity());

        if (this.getDisplayName() != null) {
            compound.putString("displayName", Component.Serializer.toJson(this.getDisplayName(), provider));
        }

        compound.putString("unit", this.getUnit());
        compound.putInt("color", this.getColor());
        compound.putBoolean("reverse", this.isReverse());
        return compound;
    }
    @Override
    public void deserialize(CompoundTag nbt, HolderLookup.Provider provider) {
        this.name(new ResourceLocation(nbt.getString("name")));
        this.amount(nbt.getInt("amount"));
        this.capacity(nbt.getInt("capacity"));

        if (nbt.contains("displayName")) {
            this.displayeName(Component.Serializer.fromJson(nbt.getString("displayName"), provider));
        }

        this.unit(nbt.getString("unit"));
        this.color(nbt.getInt("color"));
        this.reverse(nbt.getBoolean("reverse"));
    }


}
