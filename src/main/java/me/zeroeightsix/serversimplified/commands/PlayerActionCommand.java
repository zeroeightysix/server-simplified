package me.zeroeightsix.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.zeroeightsix.serversimplified.ServerSimplified;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;

import static me.zeroeightsix.serversimplified.Util.isHuman;

public abstract class PlayerActionCommand {

    protected final Consumer<ServerPlayerEntity> entityConsumer;
    private final Function<Collection<ServerPlayerEntity>, String> verb;

    public PlayerActionCommand(Consumer<ServerPlayerEntity> entityConsumer, Function<Collection<ServerPlayerEntity>, String> verb) {
        this.entityConsumer = entityConsumer;
        this.verb = verb;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, Class<? extends PlayerActionCommand> commandClass) {
        try {
            PlayerActionCommand command = commandClass.newInstance();
            String name = command.getName();

            LiteralArgumentBuilder<ServerCommandSource> argumentBuilder = CommandManager
                    .literal(name)
                    .requires((commandSource) -> ServerSimplified.getConfiguration().getPermissions().checkPermissions(commandSource, name)
                            || commandSource.hasPermissionLevel(2))
                    .then(CommandManager.argument("target", EntityArgumentType.players())
                            .executes((context) -> command.action(context, EntityArgumentType.getPlayers(context, "target"))))
                    .executes(context -> {
                        if (isHuman(context.getSource())) {
                            command.action(context, Collections.singleton((ServerPlayerEntity) context.getSource().getEntity()));
                        } else {
                            context.getSource().sendError(new LiteralText("You must specify at least one player."));
                        }
                        return 1;
                    });

            dispatcher.register(argumentBuilder);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private int action(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
        players.forEach(entityConsumer);

        int listSize = players.size();
        if (listSize == 1) {
            context.getSource().sendFeedback(new LiteralText(verb.apply(players) + " ").append(players.iterator().next().getName().getString() + "."), false); // TODO: Option for setting this to true?
        } else {
            context.getSource().sendFeedback(new LiteralText(verb.apply(players) + " " + listSize + " players."), false);
        }

        return listSize;
    }

    protected abstract String getName();

}
