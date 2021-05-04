package me.zeroeightsix.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public interface Registrable {

    void register(CommandDispatcher<ServerCommandSource> dispatcher);

}
