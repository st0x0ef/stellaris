package com.st0x0ef.stellaris.neoforge.compat;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.compats.ModCompat;

import java.util.HashMap;

public class MekanismCompat implements ModCompat {

    public static HashMap<String, Integer> RADIOACTIVE_ITEMS = new HashMap<String, Integer>();

    @Override
    public void init() {
        Stellaris.LOG.info("Loaded Compat: Mekanism");
        addRadioactiveItem();

    }

    public void addRadioactiveItem() {
        RADIOACTIVE_ITEMS.put("raw_uranium", 0);
        RADIOACTIVE_ITEMS.put("ore_uranium", 0);
        RADIOACTIVE_ITEMS.put("block_raw_uranium", 0);
        RADIOACTIVE_ITEMS.put("ingot_uranium", 1);
        RADIOACTIVE_ITEMS.put("block_uranium", 2);

    }
}
