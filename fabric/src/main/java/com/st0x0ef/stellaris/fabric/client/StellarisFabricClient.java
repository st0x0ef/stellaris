package com.st0x0ef.stellaris.fabric.client;

import com.st0x0ef.stellaris.client.StellarisClient;
import com.st0x0ef.stellaris.client.renderers.armors.JetSuitModel;
import com.st0x0ef.stellaris.client.renderers.entities.alien.AlienModel;
import com.st0x0ef.stellaris.client.renderers.entities.alien.AlienRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.alienzombie.AlienZombieModel;
import com.st0x0ef.stellaris.client.renderers.entities.alienzombie.AlienZombieRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.cheeseboss.CheeseBossModel;
import com.st0x0ef.stellaris.client.renderers.entities.cheeseboss.CheeseBossRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.martianraptor.MartianRaptorModel;
import com.st0x0ef.stellaris.client.renderers.entities.martianraptor.MartianRaptorRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.mogler.MoglerModel;
import com.st0x0ef.stellaris.client.renderers.entities.mogler.MoglerRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.projectiles.IceShardArrowRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.pygro.PygroBruteRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.pygro.PygroModel;
import com.st0x0ef.stellaris.client.renderers.entities.pygro.PygroRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.starcrawler.StarCrawlerModel;
import com.st0x0ef.stellaris.client.renderers.entities.starcrawler.StarCrawlerRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.lander.LanderModel;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.lander.LanderRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.big.BigRocketModel;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.big.BigRocketRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.normal.NormalRocketModel;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.normal.NormalRocketRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.small.SmallRocketModel;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.small.SmallRocketRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.tiny.TinyRocketModel;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.tiny.TinyRocketRenderer;
import com.st0x0ef.stellaris.client.renderers.globe.GlobeBlockRenderer;
import com.st0x0ef.stellaris.client.renderers.globe.GlobeModel;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class StellarisFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        StellarisClient.initClient();
        StellarisClient.registerPacks();
        StellarisClient.registerScreen();
        registerEntityRenderer();
        registerEntityModelLayer();
    }

    public static void registerEntityRenderer() {
        EntityRendererRegistry.register(EntityRegistry.ALIEN.get(), AlienRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ALIEN_ZOMBIE.get(), AlienZombieRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MARTIAN_RAPTOR.get(), MartianRaptorRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.PYGRO.get(), PygroRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.PYGRO_BRUTE.get(), PygroBruteRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MOGLER.get(), MoglerRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.STAR_CRAWLER.get(), StarCrawlerRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CHEESE_BOSS.get(), CheeseBossRenderer::new);

        EntityRendererRegistry.register(EntityRegistry.ICE_SPIT.get(), renderManager -> new ThrownItemRenderer<>(renderManager, 1, true));
        EntityRendererRegistry.register(EntityRegistry.ICE_SHARD_ARROW.get(), IceShardArrowRenderer::new);

        EntityRendererRegistry.register(EntityRegistry.NORMAL_ROCKET.get(), NormalRocketRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.TINY_ROCKET.get(), TinyRocketRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SMALL_ROCKET.get(), SmallRocketRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BIG_ROCKET.get(), BigRocketRenderer::new);

        EntityRendererRegistry.register(EntityRegistry.LANDER.get(), LanderRenderer::new);

        BlockEntityRendererRegistry.register(BlockEntityRegistry.GLOBE_BLOCK_ENTITY.get(), GlobeBlockRenderer::new);
    }

    public static void registerEntityModelLayer() {
        EntityModelLayerRegistry.registerModelLayer(AlienModel.LAYER_LOCATION, AlienModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(AlienZombieModel.LAYER_LOCATION, AlienZombieModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(MartianRaptorModel.LAYER_LOCATION, MartianRaptorModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(PygroModel.LAYER_LOCATION, PygroModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(MoglerModel.LAYER_LOCATION, MoglerModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(StarCrawlerModel.LAYER_LOCATION, StarCrawlerModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(CheeseBossModel.LAYER_LOCATION, CheeseBossModel::createBodyLayer);

        EntityModelLayerRegistry.registerModelLayer(GlobeModel.LAYER_LOCATION, GlobeModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(LanderModel.LAYER_LOCATION, LanderModel::createBodyLayer);

        EntityModelLayerRegistry.registerModelLayer(TinyRocketModel.LAYER_LOCATION, TinyRocketModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(SmallRocketModel.LAYER_LOCATION, SmallRocketModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(NormalRocketModel.LAYER_LOCATION, NormalRocketModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(BigRocketModel.LAYER_LOCATION, BigRocketModel::createBodyLayer);

        EntityModelLayerRegistry.registerModelLayer(JetSuitModel.LAYER_LOCATION, JetSuitModel::createBodyLayer);
    }
}
