package me.zeroeightsix.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.StringTextComponent;

import java.util.Collection;


public class HealCommand extends PlayerActionCommand {

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

    @Override
    protected int action(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
        return healPlayers(context, players);
    }

    @Override
    protected String getName() {
        return "heal";
    }

}
