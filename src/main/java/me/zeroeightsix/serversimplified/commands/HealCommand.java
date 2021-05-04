package me.zeroeightsix.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import me.zeroeightsix.serversimplified.Util;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Collection;

public class HealCommand implements Registrable {
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        Util.registerPlayersCommand(dispatcher, "heal", HealCommand::healPlayers);
    }

    private static int healPlayers(CommandContext<ServerCommandSource> ctx, Collection<ServerPlayerEntity> players) {
        players.forEach(player -> player.setHealth(20f));

        ctx.getSource().sendFeedback(new LiteralText("Healed ").append(Util.namePlayers(players)), false);
        return 0;
    }
}