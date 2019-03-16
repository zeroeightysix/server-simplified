package me.zeroeightsix.serversimplified.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.StringTextComponent;

import java.util.Collection;

public class FeedCommand extends PlayerActionCommand {
    @Override
    protected int action(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> entities) {
        for (ServerPlayerEntity entity : entities) {
            entity.getHungerManager().setFoodLevel(20);
        }

        int listSize = entities.size();
        if (listSize == 1) {
            context.getSource().sendFeedback(new StringTextComponent("Fed ").append(entities.iterator().next().getName().append(".")), false); // TODO: Option for setting this to true?
        } else {
            context.getSource().sendFeedback(new StringTextComponent("Fed " + listSize + " players."), false);
        }

        return listSize;

    }

    @Override
    protected String getName() {
        return "feed";
    }
}
