package com.st0x0ef.stellaris.client.renderers.entities.alien;


import com.st0x0ef.stellaris.common.entities.mobs.alien.Alien;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;

@Environment(EnvType.CLIENT)
public class AlienRenderer extends MobRenderer<Alien, AlienModel<Alien>> {

    /** TEXTURES */
    public static final ResourceLocation ALIEN = ResourceLocationUtils.texture("entity/alien/alien");

    public static final ResourceLocation FARMER = ResourceLocationUtils.texture("entity/alien/farmer");
    public static final ResourceLocation FISHERMAN = ResourceLocationUtils.texture("entity/alien/fisherman");
    public static final ResourceLocation SHEPHERD = ResourceLocationUtils.texture("entity/alien/shepherd");
    public static final ResourceLocation FLETCHER = ResourceLocationUtils.texture("entity/alien/fletcher");
    public static final ResourceLocation LIBRARIAN = ResourceLocationUtils.texture("entity/alien/librarian");
    public static final ResourceLocation CARTOGRAPHER = ResourceLocationUtils.texture("entity/alien/cartographer");
    public static final ResourceLocation CLERIC = ResourceLocationUtils.texture("entity/alien/cleric");
    public static final ResourceLocation ARMORER = ResourceLocationUtils.texture("entity/alien/armorer");
    public static final ResourceLocation WEAPON_SMITH = ResourceLocationUtils.texture("entity/alien/weapon_smith");
    public static final ResourceLocation TOOL_SMITH = ResourceLocationUtils.texture("entity/alien/tool_smith");
    public static final ResourceLocation BUTCHER = ResourceLocationUtils.texture("entity/alien/butcher");
    public static final ResourceLocation LEATHER_WORKER = ResourceLocationUtils.texture("entity/alien/leather_worker");
    public static final ResourceLocation MASON = ResourceLocationUtils.texture("entity/alien/mason");


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