package me.zeroeightsix.basicstaffmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.zeroeightsix.basicstaffmod.BasicStaffMod;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.StringTextComponent;

import java.util.*;

public class MuteCommand {

    public static final Set<String> mutedPlayers = new HashSet<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> muteBuilder = ServerCommandManager
                .literal("mute")
                .requires(
                        (commandSource) ->
                                BasicStaffMod.getConfiguration().getPermissions().checkPermissions(commandSource, "mute")
                                        || commandSource.hasPermissionLevel(2))
                .then(
                        ServerCommandManager.argument("target", EntityArgumentType.multiplePlayer())
                                .executes((context) -> mutePlayer(context, EntityArgumentType.method_9312(context, "target"), true))
                )
                .executes(context -> {
                    context.getSource().sendError(new StringTextComponent("You must specify at least one player."));
                    return 1;
                });

        LiteralArgumentBuilder<ServerCommandSource> unmuteBuilder = ServerCommandManager
                .literal("unmute")
                .requires(
                        (commandSource) ->
                                BasicStaffMod.getConfiguration().getPermissions().checkPermissions(commandSource, "unmute")
                                        || commandSource.hasPermissionLevel(2))
                .then(
                        ServerCommandManager.argument("target", EntityArgumentType.multiplePlayer())
                                .executes((context) -> mutePlayer(context, EntityArgumentType.method_9312(context, "target"), false))
                )
                .executes(context -> {
                    context.getSource().sendError(new StringTextComponent("You must specify at least one player."));
                    return 1;
                });

        dispatcher.register(muteBuilder);
        dispatcher.register(unmuteBuilder);
    }

    private static int mutePlayer(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> target, boolean muted) {
        int i = 0;
        for (ServerPlayerEntity entity : target) {
            i++;
            if (muted)
                mutedPlayers.add(entity.getUuidAsString());
            else
                mutedPlayers.remove(entity.getUuidAsString());
        }

        if (i == 1) {
            context.getSource().sendFeedback(new StringTextComponent((muted ? "Muted" : "Unmuted") +  " ").append(target.iterator().next().getName()).append("."), false);
        } else {
            context.getSource().sendFeedback(new StringTextComponent((muted ? "Muted" : "Unmuted") +  i + " players."), false);
        }
        return i;
    }

}
