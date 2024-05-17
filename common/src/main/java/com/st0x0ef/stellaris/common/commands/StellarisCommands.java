package com.st0x0ef.stellaris.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class StellarisCommands {

    public StellarisCommands(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("stellaris")
                .then(Commands.literal("planetScreen")
                        .requires(c -> c.hasPermission(2))
                        .executes((Command) -> PlanetUtil.openPlanetSelectionMenu(Command.getSource().getPlayer())))
        );
    }
}
