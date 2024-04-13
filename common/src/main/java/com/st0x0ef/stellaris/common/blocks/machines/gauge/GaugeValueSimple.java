package com.st0x0ef.stellaris.common.blocks.machines.gauge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NonnullDefault;

public class GaugeValueSimple implements IGaugeValue
{
    public static final int FALLBACK_COLOR = 0xA0FFFFFF;

    private ResourceLocation name;
    private int amount;
    private int capacity;
    private Component displayeName;
    private String unit;
    private int color;
    private boolean reverse;

    private Component displayNameCache;

    public GaugeValueSimple() {
        this(null);
    }

    public GaugeValueSimple(@NonnullDefault ResourceLocation name) {
        this(name, 0, 0);
    }

    public GaugeValueSimple(@NonnullDefault ResourceLocation name, int amount, int capacity) {
        this(name, amount, capacity, null);
    }

    public GaugeValueSimple(@NonnullDefault ResourceLocation name, int amount, int capacity, @Nullable Component displayeName) {
        this(name, amount, capacity, displayeName, "");
    }

    public GaugeValueSimple(@NonnullDefault ResourceLocation name, int amount, int capacity, @Nullable Component displayeName, @Nonnull String unit) {
        this(name, amount, capacity, displayeName, unit, FALLBACK_COLOR);
    }

    public GaugeValueSimple(@NonnullDefault ResourceLocation name, int amount, int capacity, @Nullable Component displayeName, @Nonnull String unit, int color) {
        this.name = name;
        this.amount = amount;
        this.capacity = capacity;
        this.displayeName = displayeName;
        this.unit = unit;
        this.color = color;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putString("name", this.getName().toString());
        compound.putInt("amount", this.getAmount());
        compound.putInt("capacity", this.getCapacity());

        if (this.getDisplayName() != null) {
            compound.putString("displayName", Component.Serializer.toJson(this.getDisplayName()));
        }

        compound.putString("unit", this.getUnit());
        compound.putInt("color", this.getColor());
        compound.putBoolean("reverse", this.isReverse());

        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        this.name(new ResourceLocation(compound.getString("name")));
        this.amount(compound.getInt("amount"));
        this.capacity(compound.getInt("capacity"));

        if (compound.contains("displayName")) {
            this.displayeName(Component.Serializer.fromJson(compound.getString("displayName")));
        }

        this.unit(compound.getString("unit"));
        this.color(compound.getInt("color"));
        this.reverse(compound.getBoolean("reverse"));
    }

    public ResourceLocation getName() {
        return name;
    }

    public GaugeValueSimple name(@NonnullDefault ResourceLocation name) {
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

    public GaugeValueSimple displayeName(@NonnullDefault Component displayeName) {
        this.displayeName = displayeName;
        return this;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    public GaugeValueSimple unit(@NonnullDefault String unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    public GaugeValueSimple amount(int amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public int getCapacity() {
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

}
