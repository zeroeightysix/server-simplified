package me.zeroeightsix.serversimplified;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiFunction;

public class Util {

    public static final int PERMISSION_LEVEL = 2;

    public static boolean isHuman(ServerCommandSource source) {
        return source.getEntity() instanceof ServerPlayerEntity;
    }

    public static MutableText namePlayers(Collection<ServerPlayerEntity> players) {
        switch (players.size()) {
            case 0:
                return new LiteralText("no one");
            case 1:
                return ((MutableText) players.iterator().next().getDisplayName());
            default:
                return new LiteralText(players.size() + " players");
        }
    }

    public static void registerPlayersCommand(CommandDispatcher<ServerCommandSource> dispatcher,
                                        String commandName,
                                        BiFunction<CommandContext<ServerCommandSource>, Collection<ServerPlayerEntity>, Integer> consumer) {
        dispatcher.register(CommandManager.literal(commandName)
                .requires(source -> source.hasPermissionLevel(PERMISSION_LEVEL))
                .then(CommandManager.argument("players", EntityArgumentType.players())
                        .executes(ctx -> consumer.apply(ctx, EntityArgumentType.getPlayers(ctx, "players"))))
                .executes(ctx -> consumer.apply(ctx, Collections.singleton(ctx.getSource().getPlayer())))
        );
    }
    
}
