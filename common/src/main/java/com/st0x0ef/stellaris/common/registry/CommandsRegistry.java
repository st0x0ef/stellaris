package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.common.commands.StellarisCommands;
import dev.architectury.event.events.common.CommandRegistrationEvent;

public class CommandsRegistry {

    public static void register() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            new StellarisCommands(dispatcher);
        });
    }

}
