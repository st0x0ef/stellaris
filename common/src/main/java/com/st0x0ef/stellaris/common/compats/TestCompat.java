package com.st0x0ef.stellaris.common.compats;

import com.st0x0ef.stellaris.Stellaris;

@ModCompat.Compat(modid = "stellaris")
public class TestCompat implements ModCompat {

    @Override
    public void init() {
        Stellaris.LOG.error("Enabled TestCompat!");
    }
}
