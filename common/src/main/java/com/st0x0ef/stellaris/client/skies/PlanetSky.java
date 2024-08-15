package com.st0x0ef.stellaris.client.skies;

import com.st0x0ef.stellaris.client.skies.record.SkyProperties;
import com.st0x0ef.stellaris.client.skies.renderer.SkyRenderer;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PlanetSky extends DimensionSpecialEffects {
    private final SkyRenderer renderer;
    private final SkyProperties properties;

    public PlanetSky(SkyProperties properties) {
        super(properties.cloudHeight(), true, SkyType.valueOf(properties.skyType()), false, false);
        this.properties = properties;
        this.renderer = new SkyRenderer(properties);
    }

    @Override
    public @NotNull Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
        return getProperties().fog() ? fogColor.multiply(brightness * 0.94F + 0.06F, brightness * 0.94F + 0.06F, brightness * 0.91F + 0.09F) : fogColor;
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return false;
    }

    public SkyRenderer getRenderer() {
        return renderer;
    }

    public SkyProperties getProperties() {
        return properties;
    }
}
