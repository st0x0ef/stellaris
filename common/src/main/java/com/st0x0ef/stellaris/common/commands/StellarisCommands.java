package com.st0x0ef.stellaris.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.data.recipes.SpaceStationRecipesManager;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadLauncher;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadUtils;
import com.st0x0ef.stellaris.common.menus.TestMenu;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class StellarisCommands {

    public StellarisCommands(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("stellaris")
                .then(Commands.literal("oil")
                        .requires(c -> c.hasPermission(2))
                        .then(Commands.literal("set")
                                .then(Commands.argument("level", IntegerArgumentType.integer())
                                        .executes((CommandContext<CommandSourceStack> context) -> {
                                            ChunkAccess access = context.getSource().getPlayer().level().getChunk(context.getSource().getPlayer().getOnPos());
                                            access.stellaris$setChunkOilLevel(context.getArgument("level", Integer.class));
                                            context.getSource().getPlayer().sendSystemMessage(Component.literal("Oil Level : " + access.stellaris$getChunkOilLevel()));
                                            return 0;
                                        })))
                        .then(Commands.literal("get")
                                .executes((CommandContext<CommandSourceStack> context) -> {
                                    ChunkAccess access = context.getSource().getPlayer().level().getChunk(context.getSource().getPlayer().getOnPos());
                                    context.getSource().getPlayer().sendSystemMessage(Component.literal("Oil Level : " + access.stellaris$getChunkOilLevel()));
                                    return 0;
                                })))

                .then(Commands.literal("screen")
                        .requires(c -> c.hasPermission(2))
                        .then(Commands.literal("galaxyScreen")
                                .executes((CommandContext<CommandSourceStack> context) -> {
                                    PlanetUtil.openMilkyWayMenu(context.getSource().getPlayer());
                                    return 0;
                                }))
                        .then(Commands.literal("tablet")
                                .executes((CommandContext<CommandSourceStack> context) -> {
                                    PlanetUtil.openTabletMenu(context.getSource().getPlayer(), ResourceLocation.parse("null:null"));
                                    return 0;
                                }))
                        .then(Commands.literal("waitScreen")
                                .executes((CommandContext<CommandSourceStack> context) -> {
                                    PlanetUtil.openWaitMenu(context.getSource().getPlayer(), context.getSource().getPlayer().getDisplayName().getString());
                                    return 0;
                                }))
                        .then(Commands.literal("planetScreen")
                                .executes((CommandContext<CommandSourceStack> context) -> {
                                    PlanetUtil.openPlanetSelectionMenu(context.getSource().getPlayer(), true, "stellaris:milky_way");
                                    return 0;
                                }))
                )


                .then(Commands.literal("test")
                        .requires(c -> c.hasPermission(2))
                        .then(Commands.literal("placeSpaceStation")
                                .executes((CommandContext<CommandSourceStack> context) -> {
                                    if (context.getSource().getPlayer() != null) {
                                        Utils.placeSpaceStation(context.getSource().getPlayer(), (ServerLevel) context.getSource().getPlayer().level(), SpaceStationRecipesManager.SPACE_STATION_RECIPES.getLast());
                                    }
                                    return 0;
                                }))
                        .then(Commands.literal("dumpPlanetInfos")
                                .executes((CommandContext<CommandSourceStack> context) -> {

                                    for(Planet planet : StellarisData.getPlanets()) {
                                        Stellaris.LOG.info(planet.name());
                                        Stellaris.LOG.info("[br] [br] Temperature : [color=red]{}°c [br] Gravity : [color=red]{} [br] Oxygen : [color=red]{} [br] Distance From Earth : {}km", planet.temperature(), planet.gravity(), planet.oxygen(), planet.distanceFromEarth());
                                    }

                                    return 0;
                                }))
                        .then(Commands.literal("testScreen")
                                .executes((CommandContext<CommandSourceStack> context) -> {
                                    ExtendedMenuProvider provider = new ExtendedMenuProvider() {
                                        @Override
                                        public void saveExtraData(FriendlyByteBuf buffer) {
                                        }

                                        @Override
                                        public Component getDisplayName() {
                                            return Component.literal("Planets");
                                        }

                                        @Override
                                        public @NotNull AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                                            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
                                            return TestMenu.create(syncId, inv, buffer);
                                        }
                                    };

                                    MenuRegistry.openExtendedMenu(context.getSource().getPlayer(), provider);
                                    return 0;
                                }))
                        .then(Commands.literal("launchpads")
                                .then(Commands.literal("create")
                                        .then(Commands.argument("dimension", ResourceKeyArgument.key(Registries.DIMENSION))
                                                .then(Commands.argument("pos", Vec3Argument.vec3())
                                                        .then(Commands.argument("public", BoolArgumentType.bool())
                                                                .then(Commands.argument("name", StringArgumentType.string())
                                                                        .executes((CommandContext<CommandSourceStack> context) -> {

                                                                            LaunchPad launchPad = new LaunchPad(
                                                                                    LaunchPadLauncher.LAUNCH_PADS.launchPads().size(),
                                                                                    Utils.blockPosToVec3(Vec3Argument.getCoordinates(context, "pos").getBlockPos(context.getSource())),
                                                                                    context.getArgument("dimension", ResourceKey.class),
                                                                                    StringArgumentType.getString(context, "name"),
                                                                                    BoolArgumentType.getBool(context, "public"),
                                                                                    Objects.requireNonNull(context.getSource().getPlayer()).getDisplayName().getString(),
                                                                                    new ArrayList<>()

                                                                            );
                                                                            LaunchPadLauncher.addLaunchPad(launchPad, context.getSource().getServer());

                                                                            context.getSource().sendSuccess(() -> Component.literal("Space Station " + StringArgumentType.getString(context, "name") + " Created"), true);

                                                                            return Command.SINGLE_SUCCESS;

                                                                        })
                                                                )
                                                        )
                                                )
                                        )
                                ).then(Commands.literal("remove")
                                        .then(Commands.argument("dimension", ResourceKeyArgument.key(Registries.DIMENSION))
                                                .then(Commands.argument("name", StringArgumentType.string())
                                                        .executes((CommandContext<CommandSourceStack> context) -> {

                                                            LaunchPad pad = LaunchPadUtils.getPadByNameAndDim(StringArgumentType.getString(context, "name"), context.getArgument("dimension", ResourceKey.class));

                                                            if (pad != null && LaunchPadLauncher.removeLaunchpad(pad.id(),  context.getSource().getServer())) {
                                                                context.getSource().sendSuccess(() -> Component.literal("Space Station " + StringArgumentType.getString(context, "name") + " Deleted"), true);
                                                            } else {
                                                                context.getSource().sendFailure(Component.literal("Space Station " + StringArgumentType.getString(context, "name") + " Not Found"));
                                                            }
                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )
                                        )
                                        .then(Commands.argument("id", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                                                .executes((CommandContext<CommandSourceStack> context) -> {

                                                    LaunchPad pad = LaunchPadUtils.getPadById(IntegerArgumentType.getInteger(context, "id"));

                                                    if (pad != null && LaunchPadLauncher.removeLaunchpad(pad.id(),context.getSource().getServer())) {
                                                        context.getSource().sendSuccess(() -> Component.literal("Space Station " + pad.id() + " Deleted"), true);
                                                    } else {
                                                        context.getSource().sendFailure(Component.literal("Space Station " + StringArgumentType.getString(context, "name") + " Not Found"));
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                })

                                        )
                                )
                        )
                )
        );
    }
}
