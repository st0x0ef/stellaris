package com.st0x0ef.stellaris.client.renderers.entities.alien;


import com.st0x0ef.stellaris.common.entities.mobs.alien.Alien;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;

import static com.st0x0ef.stellaris.Stellaris.texture;

@Environment(EnvType.CLIENT)
public class AlienRenderer extends MobRenderer<Alien, AlienModel<Alien>> {

    /** TEXTURES */
    public static final ResourceLocation ALIEN = texture("entity/alien/alien");

    public static final ResourceLocation FARMER = texture("entity/alien/farmer");
    public static final ResourceLocation FISHERMAN = texture("entity/alien/fisherman");
    public static final ResourceLocation SHEPHERD = texture("entity/alien/shepherd");
    public static final ResourceLocation FLETCHER = texture("entity/alien/fletcher");
    public static final ResourceLocation LIBRARIAN = texture("entity/alien/librarian");
    public static final ResourceLocation CARTOGRAPHER = texture("entity/alien/cartographer");
    public static final ResourceLocation CLERIC = texture("entity/alien/cleric");
    public static final ResourceLocation ARMORER = texture("entity/alien/armorer");
    public static final ResourceLocation WEAPON_SMITH = texture("entity/alien/weapon_smith");
    public static final ResourceLocation TOOL_SMITH = texture("entity/alien/tool_smith");
    public static final ResourceLocation BUTCHER = texture("entity/alien/butcher");
    public static final ResourceLocation LEATHER_WORKER = texture("entity/alien/leather_worker");
    public static final ResourceLocation MASON = texture("entity/alien/mason");


    public AlienRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new AlienModel<>(renderManagerIn.bakeLayer(AlienModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Alien entity) {
        if (entity.getVillagerData().getProfession() == VillagerProfession.FARMER) {
            return FARMER;
        }
        else if (entity.getVillagerData().getProfession() == VillagerProfession.FISHERMAN) {
            return FISHERMAN;
        }
        else if (entity.getVillagerData().getProfession() == VillagerProfession.SHEPHERD) {
            return SHEPHERD;
        }
        else if (entity.getVillagerData().getProfession() == VillagerProfession.FLETCHER) {
            return FLETCHER;
        }
        else if (entity.getVillagerData().getProfession() == VillagerProfession.LIBRARIAN) {
            return LIBRARIAN;
        }
        else if (entity.getVillagerData().getProfession() == VillagerProfession.CARTOGRAPHER) {
            return CARTOGRAPHER;
        }
        else if (entity.getVillagerData().getProfession() == VillagerProfession.CLERIC) {
            return CLERIC;
        }
        else if (entity.getVillagerData().getProfession() == VillagerProfession.ARMORER) {
            return ARMORER;
        }
        else if (entity.getVillagerData().getProfession() == VillagerProfession.WEAPONSMITH) {
            return WEAPON_SMITH;
        }
        else if (entity.getVillagerData().getProfession() == VillagerProfession.TOOLSMITH) {
            return TOOL_SMITH;
        }
        else if (entity.getVillagerData().getProfession() == VillagerProfession.BUTCHER) {
            return BUTCHER;
        }
        else if (entity.getVillagerData().getProfession() == VillagerProfession.LEATHERWORKER) {
            return LEATHER_WORKER;
        }
        else if (entity.getVillagerData().getProfession() == VillagerProfession.MASON) {
            return MASON;
        }

        return ALIEN;
    }

    @Override
    public boolean shouldRender(Alien livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return livingEntity != null && camera.isVisible(livingEntity.getBoundingBoxForCulling());
    }
}