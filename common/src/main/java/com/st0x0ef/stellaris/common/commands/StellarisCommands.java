package com.st0x0ef.stellaris.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.st0x0ef.stellaris.common.menus.PlanetSelectionMenu;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import dev.architectury.registry.menu.MenuRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public class StellarisCommands {

    public StellarisCommands(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("stellaris")
                .then(Commands.literal("planetScreen")
                        .requires(c -> c.hasPermission(2))
                        .executes((Command) -> openPlanetsScreen(Command.getSource())))
        );
    }

    private int openPlanetsScreen(CommandSourceStack source) {
        Player player = source.getPlayer();

        // Open the planet selection menu
        if (player instanceof ServerPlayer serverPlayer) {
            MenuRegistry.openMenu(serverPlayer, PlanetUtil.getPlanetMenuProvider());

        }

        return 1;
    }
}
