package me.zeroeightsix.basicstaffmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.StringTextComponent;

import java.util.Collection;
import java.util.Collections;

import static me.zeroeightsix.basicstaffmod.Util.isHuman;

public class HealCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> argumentBuilder = ServerCommandManager.literal("heal").requires((serverCommandSource_1) -> serverCommandSource_1.hasPermissionLevel(2));

        argumentBuilder
                .then(
                        ServerCommandManager.argument("target", EntityArgumentType.multiplePlayer())
                                .executes((context) -> healPlayers(context, EntityArgumentType.method_9312(context, "target")))
                )
                .executes(context -> {
                    if (isHuman(context.getSource())) {
                        healPlayers(context, Collections.singleton((ServerPlayerEntity) context.getSource().getEntity()));
                    } else {
                        context.getSource().sendError(new StringTextComponent("You must specify at least one player."));
                    }
                    return 1;
                });

        dispatcher.register(argumentBuilder);
    }

    private static int healPlayers(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> entities) {
        for (ServerPlayerEntity entity : entities) {
            entity.setHealth(20f);
        }

        int listSize = entities.size();
        if (listSize == 1) {
            context.getSource().sendFeedback(new StringTextComponent("Healed ").append(entities.iterator().next().getName().append(".")), false); // TODO: Option for setting this to true?
        } else {
            context.getSource().sendFeedback(new StringTextComponent("Healed " + listSize + " players."), false);
        }

        return listSize;
    }

}