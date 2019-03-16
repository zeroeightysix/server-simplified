package me.zeroeightsix.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.zeroeightsix.serversimplified.ServerSimplified;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.StringTextComponent;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

import static me.zeroeightsix.serversimplified.Util.isHuman;

public abstract class PlayerActionCommand {

    protected final Consumer<ServerPlayerEntity> entityConsumer;
    private final String verb;

    public PlayerActionCommand(Consumer<ServerPlayerEntity> entityConsumer, String verb) {
        this.entityConsumer = entityConsumer;
        this.verb = verb;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, Class<? extends PlayerActionCommand> commandClass) {
        try {
            PlayerActionCommand command = commandClass.newInstance();
            String name = command.getName();

            LiteralArgumentBuilder<ServerCommandSource> argumentBuilder = ServerCommandManager
                    .literal(name)
                    .requires(
                            (commandSource) ->
                                    ServerSimplified.getConfiguration().getPermissions().checkPermissions(commandSource, name)
                                            || commandSource.hasPermissionLevel(2))
                    .then(
                            ServerCommandManager.argument("target", EntityArgumentType.multiplePlayer())
                                    .executes((context) -> command.action(context, EntityArgumentType.method_9312(context, "target")))
                    )
                    .executes(context -> {
                        if (isHuman(context.getSource())) {
                            command.action(context, Collections.singleton((ServerPlayerEntity) context.getSource().getEntity()));
                        } else {
                            context.getSource().sendError(new StringTextComponent("You must specify at least one player."));
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
            context.getSource().sendFeedback(new StringTextComponent(verb + " ").append(players.iterator().next().getName().append(".")), false); // TODO: Option for setting this to true?
        } else {
            context.getSource().sendFeedback(new StringTextComponent(verb + " " + listSize + " players."), false);
        }

        return listSize;
    }

    protected abstract String getName();

}
