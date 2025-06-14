package com.st0x0ef.stellaris.client.renderers.entities.cheeseboss;

import com.st0x0ef.stellaris.common.entities.mobs.cheese_boss.CheeseBoss;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static com.st0x0ef.stellaris.Stellaris.texture;

@Environment(EnvType.CLIENT)
public class CheeseBossRenderer extends MobRenderer<CheeseBoss, CheeseBossModel<CheeseBoss>> {
    public CheeseBossRenderer(EntityRendererProvider.Context context) {
        super(context, new CheeseBossModel<>(context.bakeLayer(CheeseBossModel.LAYER_LOCATION)), 1f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(CheeseBoss entity) {
        return switch (entity.getCurrentPhase()) {
            case "cheddar" -> texture("entity/cheese_boss_cheddar");
            case "camembert" -> texture("entity/cheese_boss_camembert");
            case "babybel" -> texture("entity/cheese_boss_babybel");
            default -> throw new IllegalStateException("Unexpected value: " + entity.getCurrentPhase());
        };
    }

    @Override
    public boolean shouldRender(CheeseBoss livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return livingEntity != null && camera.isVisible(livingEntity.getBoundingBoxForCulling());
    }
}
