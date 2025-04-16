package com.st0x0ef.stellaris.client.renderers.entities.alien;


import com.st0x0ef.stellaris.common.entities.mobs.alien.Alien;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;

import static com.st0x0ef.stellaris.Stellaris.id;

@Environment(EnvType.CLIENT)
public class AlienRenderer extends MobRenderer<Alien, AlienModel<Alien>> {

    /** TEXTURES */
    public static final ResourceLocation ALIEN = id("textures/entity/alien/alien.png");

    public static final ResourceLocation FARMER = id("textures/entity/alien/farmer.png");
    public static final ResourceLocation FISHERMAN = id("textures/entity/alien/fisherman.png");
    public static final ResourceLocation SHEPHERD = id("textures/entity/alien/shepherd.png");
    public static final ResourceLocation FLETCHER = id("textures/entity/alien/fletcher.png");
    public static final ResourceLocation LIBRARIAN = id("textures/entity/alien/librarian.png");
    public static final ResourceLocation CARTOGRAPHER = id("textures/entity/alien/cartographer.png");
    public static final ResourceLocation CLERIC = id("textures/entity/alien/cleric.png");
    public static final ResourceLocation ARMORER = id("textures/entity/alien/armorer.png");
    public static final ResourceLocation WEAPON_SMITH = id("textures/entity/alien/weapon_smith.png");
    public static final ResourceLocation TOOL_SMITH = id("textures/entity/alien/tool_smith.png");
    public static final ResourceLocation BUTCHER = id("textures/entity/alien/butcher.png");
    public static final ResourceLocation LEATHER_WORKER = id("textures/entity/alien/leather_worker.png");
    public static final ResourceLocation MASON = id("textures/entity/alien/mason.png");


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