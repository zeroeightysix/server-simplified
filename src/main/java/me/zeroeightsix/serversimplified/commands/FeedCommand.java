package me.zeroeightsix.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.zeroeightsix.serversimplified.Util;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Collection;

public class FeedCommand implements Registrable {
    private static final int PERMISSION_LEVEL = 2;

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("feed")
                .requires(source -> source.hasPermissionLevel(PERMISSION_LEVEL))
                .then(CommandManager.argument("players", EntityArgumentType.players()).executes(FeedCommand::feedPlayer))
        );
    }

    private static int feedPlayer(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        final Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(ctx, "player");
        players.forEach(player -> player.getHungerManager().setFoodLevel(20));

        ctx.getSource().sendFeedback(new LiteralText("Fed ").append(Util.namePlayers(players)), false);
        return 0;
    }
}
