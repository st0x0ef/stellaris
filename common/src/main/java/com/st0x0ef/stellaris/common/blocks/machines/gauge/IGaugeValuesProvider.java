package com.st0x0ef.stellaris.common.blocks.machines.gauge;

import java.util.List;

public interface IGaugeValuesProvider
{
    List<IGaugeValue> getDisplayGaugeValues();
}
