package com.st0x0ef.stellaris.common.config.types;

import java.io.Serializable;

public class RangedInt implements Serializable {
    public int value;
    public int min;
    public int max;

    public RangedInt(int value, int min, int max) {
        this.min = min;
        this.max = max;
        this.value = value;

        if(value < min) {
            this.value = min;
        } else if(value > max) {
            this.value = max;
        }

    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }
}
