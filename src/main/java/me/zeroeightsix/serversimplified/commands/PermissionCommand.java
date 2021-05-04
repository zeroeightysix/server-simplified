package me.zeroeightsix.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.zeroeightsix.serversimplified.ServerSimplified;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;

import java.util.Set;

public class PermissionCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager
                .literal("permission")
                .then(
                        CommandManager.argument("target", EntityArgumentType.player())
                                .then(
                                        CommandManager.literal("add")
                                                .then(
                                                        CommandManager.argument("permission", MessageArgumentType.message())
                                                                .executes(context -> {
                                                                    String permission = MessageArgumentType.getMessage(context, "permission").asString();
                                                                    ServerPlayerEntity playerEntity = EntityArgumentType.getPlayer(context, "target");
                                                                    ServerSimplified.getConfiguration().getPermissions().addPermission(playerEntity.getUuidAsString(), permission);
                                                                    context.getSource().sendFeedback(new LiteralText("Added permission " + permission + " to ").append(playerEntity.getDisplayName()), false);
                                                                    context.getSource().getMinecraftServer().getCommandManager().sendCommandTree(playerEntity);
                                                                    return 1;
                                                                })
                                                )
                                                .executes(context -> {
                                                    context.getSource().sendError(new LiteralText("You must specify a permission to add!"));
                                                    return 1;
                                                }))
                                .then(
                                        CommandManager.literal("remove")
                                                .then(
                                                        CommandManager.argument("permission", MessageArgumentType.message())
                                                                .executes(context -> {
                                                                    String permission = MessageArgumentType.getMessage(context, "permission").asString();
                                                                    ServerPlayerEntity playerEntity = EntityArgumentType.getPlayer(context, "target");
                                                                    ServerSimplified.getConfiguration().getPermissions().removePermission(playerEntity.getUuidAsString(), permission);
                                                                    context.getSource().sendFeedback(new LiteralText("Removed permission " + permission + " from ").append(playerEntity.getDisplayName()), false);
                                                                    context.getSource().getMinecraftServer().getCommandManager().sendCommandTree(playerEntity);
                                                                    return 1;
                                                                })
                                                )
                                                .executes(context -> {
                                                    context.getSource().sendError(new LiteralText("You must specify a permission to remove!"));
                                                    return 1;
                                                })
                                )
                                .executes(context -> {
                                    ServerPlayerEntity playerEntity = EntityArgumentType.getPlayer(context, "target");
                                    Set<String> permissions = ServerSimplified.getConfiguration().getPermissions().getPermissions().get(playerEntity.getUuidAsString());
                                    if (permissions == null) {
                                        context.getSource().sendError(new LiteralText("That player doesn't have any permissions."));
                                    } else {
                                        context.getSource().sendFeedback(((MutableText) playerEntity.getDisplayName()).append(" has the following permissions:"), false);
                                        for (String s : permissions) {
                                            context.getSource().sendFeedback(new LiteralText(" - " + s), false);
                                        }
                                    }
                                    return 1;
                                })
                );
        dispatcher.register(builder);
    }

}
