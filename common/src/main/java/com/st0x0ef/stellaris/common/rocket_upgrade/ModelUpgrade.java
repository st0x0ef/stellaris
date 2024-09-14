package com.st0x0ef.stellaris.common.rocket_upgrade;

import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.RocketModel;

public class ModelUpgrade extends RocketUpgrade {
    final private RocketModel model;
    final private int maxPlayer;

    public ModelUpgrade(RocketModel model, int maxPlayer) {
        this.model = model;
        this.maxPlayer = maxPlayer;
    }

    public RocketModel getModel() {
        return model;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public static ModelUpgrade getBasic() {
        return new ModelUpgrade(RocketModel.NORMAL, 2);
    }
}
