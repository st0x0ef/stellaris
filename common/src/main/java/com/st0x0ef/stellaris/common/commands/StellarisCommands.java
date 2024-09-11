package com.st0x0ef.stellaris.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.chunk.ChunkAccess;

public class StellarisCommands {

    public StellarisCommands(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("stellaris")
                .then(Commands.literal("planetScreen")
                        .requires(c -> c.hasPermission(2))
                        .executes((CommandContext<CommandSourceStack> context) -> {
                            PlanetUtil.openPlanetSelectionMenu(context.getSource().getPlayer(), true);
                            return 0;
                        }))
                .then(Commands.literal("oil")
                        .requires(c -> c.hasPermission(2))
                        .executes((CommandContext<CommandSourceStack> context) -> {
                            ChunkAccess access = context.getSource().getPlayer().level().getChunk(context.getSource().getPlayer().getOnPos());
                            context.getSource().getPlayer().sendSystemMessage(Component.literal("Oil Level : " + access.stellaris$getChunkOilLevel()));
                            return 0;
                        }))
                .then(Commands.literal("galaxyScreen")
                        .requires(c -> c.hasPermission(2))
                        .executes((CommandContext<CommandSourceStack> context) -> {
                            PlanetUtil.openMilkyWayMenu(context.getSource().getPlayer());
                            return 0;
                        })));
    }
}
