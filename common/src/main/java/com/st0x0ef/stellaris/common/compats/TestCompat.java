package com.st0x0ef.stellaris.common.compats;

import com.st0x0ef.stellaris.Stellaris;

public class TestCompat implements ModCompat {

    @Override
    public void init() {
        Stellaris.LOG.error("Enabled TestCompat!");
    }
}
