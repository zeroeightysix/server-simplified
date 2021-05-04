package me.zeroeightsix.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import me.zeroeightsix.serversimplified.Util;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Collection;

public class FeedCommand implements Registrable {
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        Util.registerPlayersCommand(dispatcher, "feed", FeedCommand::feedPlayers);
    }

    private static int feedPlayers(CommandContext<ServerCommandSource> ctx, Collection<ServerPlayerEntity> players) {
        players.forEach(player -> player.getHungerManager().setFoodLevel(20));

        ctx.getSource().sendFeedback(new LiteralText("Fed ").append(Util.namePlayers(players)), false);
        return 0;
    }
}
