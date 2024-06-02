package com.st0x0ef.stellaris.common.rocket_upgrade;

import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.RocketModel;

public class ModelUpgrade extends RocketUpgrade {
    final private RocketModel model;

    public ModelUpgrade(RocketModel model) {
        this.model = model;
    }

    public RocketModel getModel() {
        return model;
    }

    public static ModelUpgrade getBasic() {
        return new ModelUpgrade(RocketModel.NORMAL);
    }
}
