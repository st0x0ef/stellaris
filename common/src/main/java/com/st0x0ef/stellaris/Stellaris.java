package com.st0x0ef.stellaris;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.st0x0ef.stellaris.common.config.CustomConfig;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.data.screen.MoonPack;
import com.st0x0ef.stellaris.common.data.screen.PlanetPack;
import com.st0x0ef.stellaris.common.data.screen.StarPack;
import com.st0x0ef.stellaris.common.events.Events;
import com.st0x0ef.stellaris.common.handlers.GlobalExceptionHandler;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.network.packets.SyncPlanetsDatapack;
import com.st0x0ef.stellaris.common.registry.*;
import dev.architectury.registry.ReloadListenerRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stellaris {
    public static final String MODID = "stellaris";
    public static final Logger LOG = LoggerFactory.getLogger("Stellaris");
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .create();

    public static void init() {
        Minecraft.getInstance().execute(() -> {
            setupOpenGLDebugMessageCallback();
            Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        });

        CustomConfig.init();

        registerPacks();

        NetworkRegistry.register();


        DataComponentsRegistry.DATA_COMPONENT_TYPE.register();

        SoundRegistry.SOUNDS.register();
        EffectsRegistry.MOB_EFFECT.register();
        FluidRegistry.FLUIDS.register();
        ParticleRegistry.PARTICLES.register();
        BlocksRegistry.BLOCKS.register();
        ItemsRegistry.ITEMS.register();
        BlockEntityRegistry.BLOCK_ENTITY_TYPE.register();
        CreativeTabsRegistry.TABS.register();
        MenuTypesRegistry.MENU_TYPE.register();
        RecipesRegistry.register();
        FeaturesRegistry.FEATURES.register();
        CommandsRegistry.register();

        EntityRegistry.ENTITY_TYPE.register();
        EntityRegistry.SENSOR.register();
        EntityData.register();

        ReloadListenerRegistry.register(PackType.SERVER_DATA, new StellarisData());
        Events.registerEvents();
    }

    private static void setupOpenGLDebugMessageCallback() {
        if (GL.getCapabilities().GL_KHR_debug) {
            GL43.glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
                if (id == 1281) {
                    return;
                }
                String errorMessage = GLDebugMessageCallback.getMessage(length, message);
                Stellaris.LOG.error("OpenGL debug message: id={}, source={}, type={}, severity={}, message='{}'",
                        id, source, type, severity, errorMessage);
            }, 0);
            GL43.glEnable(GL43.GL_DEBUG_OUTPUT);
            GL43.glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);
        }
    }

    public static void onDatapackSyncEvent(ServerPlayer player) {
        StellarisData.PLANETS.forEach(((resourceKey, planet) -> {
            RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), player.registryAccess());
            NetworkRegistry.sendToPlayer(player, NetworkRegistry.SYNC_PLANET_DATAPACK_ID, SyncPlanetsDatapack.encode(new SyncPlanetsDatapack(resourceKey, planet), buffer));
        }));
    }

    public static void registerPacks() {
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new StarPack(GSON));
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new PlanetPack(GSON));
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new MoonPack(GSON));
    }

}
