package com.st0x0ef.stellaris.common.config;

import fr.tathan.tathanconfig.config.BaseConfig;
import fr.tathan.tathanconfig.config.ConfigFormat;
import fr.tathan.tathanconfig.config.ConfigSide;

public class StellarisConfig extends BaseConfig {

    public StellarisConfig() {
        super("stellaris", ConfigSide.COMMON, ConfigFormat.JSON);
    }

    @Override
    protected void addValues() {
        addValue("planetScreenGravityColor", "White");
        addCommentedValue("uraniumBurnTime", 8000, "Burn time for uranium ingot in Radioactive Generator");
        addCommentedValue("plutoniumBurnTime", 12000, "Burn time for plutonium ingot in Radioactive Generator");
        addCommentedValue("neptuniumBurnTime", 16000, "Burn time for neptunium ingot in Radioactive Generator");

    }
}
