package com.st0x0ef.stellaris.client.renderers.entities.vehicle;

import com.st0x0ef.stellaris.common.entities.vehicles.IVehicleEntity;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class VehicleRenderState extends EntityRenderState {
    public IVehicleEntity entity;
    public float partialTick;

    public VehicleRenderState() {}
}
