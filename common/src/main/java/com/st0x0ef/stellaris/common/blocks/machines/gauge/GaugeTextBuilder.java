package com.st0x0ef.stellaris.common.blocks.machines.gauge;

import com.llamalad7.mixinextras.lib.apache.commons.StringUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.*;

public class GaugeTextBuilder
{
    private final IGaugeValue value;
    private final String translationKey;
    private final List<Object> extraValues;
    private final Map<Integer, Style> extraStyles;

    private Style textStyle;
    private Style nameStyle;
    private Style amountStyle;
    private Style capacityStyle;
    private Style unitStyle;

    private String unitSuffix;

    public GaugeTextBuilder(IGaugeValue value, String translationKey, List<Object> extraValues) {
        this.value = value;
        this.translationKey = translationKey;
        this.extraValues = Collections.unmodifiableList(extraValues);
        this.extraStyles = new HashMap<>();

        this.setTextStyle(Style.EMPTY);
        this.setNameStyle(Style.EMPTY);
        this.setAmountStyle(Style.EMPTY);
        this.setCapacityStyle(Style.EMPTY);
        this.setUnitStyle(Style.EMPTY);

        this.setUnitSuffix("");
    }

    public MutableComponent build() {
        IGaugeValue value = this.getValue();
        Component displayName = value.getDisplayName();
        long amount = value.getAmount();
        long capacity = value.getCapacity();
        String unit = value.getUnit();

        List<Object> list = new ArrayList<>();
        list.add(this.format(displayName, this.getNameStyle()));
        list.add(this.format(String.valueOf(amount), this.getAmountStyle(), unit, this.getUnitStyle()));
        list.add(this.format(String.valueOf(capacity), this.getCapacityStyle(), unit, this.getUnitStyle()));

        for (int i = 0; i < this.getExtraValues().size(); i++) {
            Object extraValue = this.getExtraValues().get(i);
            Style extraStyle = this.getExtraStyle(i);
            list.add(Component.translatable("%s", extraValue).setStyle(extraStyle));
        }

        return Component.translatable(this.getTranslationKey(), list.toArray()).setStyle(this.getTextStyle());
    }

    public MutableComponent format(String valueText, Style valueStyle, String unitText, Style unitStyle) {
        if (!StringUtils.isEmpty(unitText)) {
            return this.format(valueText, valueStyle).append(" ").append(this.format(unitText + this.getUnitSuffix(), unitStyle));
        } else {
            return this.format(valueText, valueStyle);
        }
    }

    public MutableComponent format(String text, Style style) {
        return Component.literal(text).setStyle(style);
    }

    public MutableComponent format(Component text, Style style) {
        return Component.literal("").append(text).setStyle(style);
    }

    public final IGaugeValue getValue() {
        return this.value;
    }

    public final String getTranslationKey() {
        return this.translationKey;
    }

    public final List<Object> getExtraValues() {
        return extraValues;
    }

    public final Map<Integer, Style> getExtraStyles() {
        return extraStyles;
    }

    public Style getTextStyle() {
        return textStyle;
    }

    public GaugeTextBuilder setTextStyle(Style textStyle) {
        this.textStyle = textStyle;
        return this;
    }

    public Style getNameStyle() {
        return nameStyle;
    }

    public GaugeTextBuilder setNameStyle(Style nameStyle) {
        this.nameStyle = nameStyle;
        return this;
    }

    public Style getAmountStyle() {
        return amountStyle;
    }

    public GaugeTextBuilder setAmountStyle(Style amountStyle) {
        this.amountStyle = amountStyle;
        return this;
    }

    public Style getCapacityStyle() {
        return capacityStyle;
    }

    public GaugeTextBuilder setCapacityStyle(Style capacityStyle) {
        this.capacityStyle = capacityStyle;
        return this;
    }

    public Style getUnitStyle() {
        return unitStyle;
    }

    public GaugeTextBuilder setUnitStyle(Style unitStyle) {
        this.unitStyle = unitStyle;
        return this;
    }

    public String getUnitSuffix() {
        return unitSuffix;
    }

    public GaugeTextBuilder setUnitSuffix(String unitSuffix) {
        this.unitSuffix = unitSuffix;
        return this;
    }

    public GaugeTextBuilder setExtraStyle(int index, Style style) {
        this.getExtraStyles().put(index, style);
        return this;
    }

    public Style getExtraStyle(int index) {
        return this.getExtraStyles().getOrDefault(index, Style.EMPTY);
    }
}
