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

import static me.zeroeightsix.serversimplified.Util.isHuman;

public abstract class PlayerActionCommand {

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
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected abstract int action(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players);

    protected abstract String getName();

}
