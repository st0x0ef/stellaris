package com.st0x0ef.stellaris.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.st0x0ef.stellaris.common.menus.PlanetSelectionMenu;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
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
                        .executes((Command) -> {
                            return openPlanetsScreen(Command.getSource());
                        }))
        );

    }

    private int openPlanetsScreen(CommandSourceStack source) {
        Player player = source.getPlayer();

        // Open the planet selection menu
        if (player instanceof ServerPlayer serverPlayer) {
            MenuRegistry.openMenu(serverPlayer, getPlanetMenuProvider());

        }
        return 1;

    }

    //TODO: Create a MethodsClass
    public MenuProvider getPlanetMenuProvider() {
        return new MenuProvider() {

            @Override
            public Component getDisplayName() {
                return Component.literal("Planets !!");
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                return PlanetSelectionMenu.create(syncId, inv);
            }
        };
    }

}
