package me.zeroeightsix.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.zeroeightsix.serversimplified.ServerSimplified;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.command.arguments.MessageArgumentType;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.StringTextComponent;

import java.util.Set;

public class PermissionCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = ServerCommandManager
                .literal("permission")
                .then(
                        ServerCommandManager.argument("target", EntityArgumentType.onePlayer())
                                .then(
                                        ServerCommandManager.literal("add")
                                                .then(
                                                        ServerCommandManager.argument("permission", MessageArgumentType.create())
                                                                .executes(context -> {
                                                                    String permission = MessageArgumentType.getMessageArgument(context, "permission").getFormattedText();
                                                                    ServerPlayerEntity playerEntity = EntityArgumentType.method_9312(context, "target").iterator().next();
                                                                    ServerSimplified.getConfiguration().getPermissions().addPermission(playerEntity.getUuidAsString(), permission);
                                                                    context.getSource().sendFeedback(new StringTextComponent("Added permission " + permission + " to ").append(playerEntity.getDisplayName()), false);
                                                                    return 1;
                                                                })
                                                )
                                                .executes(context -> {
                                                    context.getSource().sendError(new StringTextComponent("You must specify a permission to add!"));
                                                    return 1;
                                                }))
                                .then(
                                        ServerCommandManager.literal("remove")
                                                .then(
                                                        ServerCommandManager.argument("permission", MessageArgumentType.create())
                                                                .executes(context -> {
                                                                    String permission = MessageArgumentType.getMessageArgument(context, "permission").getFormattedText();
                                                                    ServerPlayerEntity playerEntity = EntityArgumentType.method_9312(context, "target").iterator().next();
                                                                    ServerSimplified.getConfiguration().getPermissions().removePermission(playerEntity.getUuidAsString(), permission);
                                                                    context.getSource().sendFeedback(new StringTextComponent("Removed permission " + permission + " from ").append(playerEntity.getDisplayName()), false);
                                                                    return 1;
                                                                })
                                                )
                                                .executes(context -> {
                                                    context.getSource().sendError(new StringTextComponent("You must specify a permission to remove!"));
                                                    return 1;
                                                })
                                )
                                .executes(context -> {
                                    ServerPlayerEntity playerEntity = EntityArgumentType.method_9312(context, "target").iterator().next();
                                    Set<String> permissions = ServerSimplified.getConfiguration().getPermissions().getPermissions().get(playerEntity.getUuidAsString());
                                    if (permissions == null) {
                                        context.getSource().sendError(new StringTextComponent("That player doesn't have any permissions."));
                                    } else {
                                        context.getSource().sendFeedback(playerEntity.getDisplayName().append(" has the following permissions:"), false);
                                        for (String s : permissions) {
                                            context.getSource().sendFeedback(new StringTextComponent(" - " + s), false);
                                        }
                                    }
                                    return 1;
                                })
                );
        dispatcher.register(builder);
    }

}
