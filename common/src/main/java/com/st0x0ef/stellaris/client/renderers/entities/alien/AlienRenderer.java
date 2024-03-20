package com.st0x0ef.stellaris.client.renderers.entities.alien;


import com.st0x0ef.stellaris.common.entities.alien.Alien;
import com.st0x0ef.stellaris.Stellaris;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;

public class AlienRenderer extends MobRenderer<Alien, EntityModel<Alien>> {

    /** TEXTURES */
    public static final ResourceLocation ALIEN = new ResourceLocation(Stellaris.MODID,"textures/entity/alien/alien.png");

    public static final ResourceLocation FARMER = new ResourceLocation(Stellaris.MODID,"textures/entity/alien/farmer.png");
    public static final ResourceLocation FISHERMAN = new ResourceLocation(Stellaris.MODID,"textures/entity/alien/fisherman.png");
    public static final ResourceLocation SHEPHERD = new ResourceLocation(Stellaris.MODID,"textures/entity/alien/shepherd.png");
    public static final ResourceLocation FLETCHER = new ResourceLocation(Stellaris.MODID,"textures/entity/alien/fletcher.png");
    public static final ResourceLocation LIBRARIAN = new ResourceLocation(Stellaris.MODID,"textures/entity/alien/librarian.png");
    public static final ResourceLocation CARTOGRAPHER = new ResourceLocation(Stellaris.MODID,"textures/entity/alien/cartographer.png");
    public static final ResourceLocation CLERIC = new ResourceLocation(Stellaris.MODID,"textures/entity/alien/cleric.png");
    public static final ResourceLocation ARMORER = new ResourceLocation(Stellaris.MODID,"textures/entity/alien/armorer.png");
    public static final ResourceLocation WEAPON_SMITH = new ResourceLocation(Stellaris.MODID,"textures/entity/alien/weapon_smith.png");
    public static final ResourceLocation TOOL_SMITH = new ResourceLocation(Stellaris.MODID,"textures/entity/alien/tool_smith.png");
    public static final ResourceLocation BUTCHER = new ResourceLocation(Stellaris.MODID,"textures/entity/alien/butcher.png");
    public static final ResourceLocation LEATHER_WORKER = new ResourceLocation(Stellaris.MODID,"textures/entity/alien/leather_worker.png");
    public static final ResourceLocation MASON = new ResourceLocation(Stellaris.MODID,"textures/entity/alien/mason.png");


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
}