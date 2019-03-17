package me.zeroeightsix.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.zeroeightsix.serversimplified.ServerSimplified;
import net.minecraft.client.network.ClientDummyContainerProvider;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.container.Container;
import net.minecraft.container.GenericContainer;
import net.minecraft.container.NameableContainerProvider;
import net.minecraft.container.PlayerContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;

import java.util.Collection;

import static me.zeroeightsix.serversimplified.Util.isHuman;

public class SeekInventoryCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = ServerCommandManager
                .literal("seekinv")
                .requires(
                        source ->
                                ServerSimplified.getConfiguration().getPermissions().checkPermissions(source, "seekinv")
                                        || source.hasPermissionLevel(2)
                )
                .then(
                        ServerCommandManager.argument("target", EntityArgumentType.onePlayer())
                                .executes(context -> {
                                    try {
                                        if (!isHuman(context.getSource())) {
                                            context.getSource().sendError(new StringTextComponent("You must be a player to use this command!"));
                                            return 1;
                                        }
                                        ServerPlayerEntity sender = (ServerPlayerEntity) context.getSource().getEntity();
                                        Collection<ServerPlayerEntity> entities = EntityArgumentType.method_9312(context, "target");
                                        for (ServerPlayerEntity entity : entities) {
                                            entity.inventory.onInvOpen(sender);
                                            sender.openContainer(new NameableContainerProvider() {
                                                @Override
                                                public TextComponent getDisplayName() {
                                                    return entity.getName();
                                                }

                                                @Override
                                                public Container createMenu(int id, PlayerInventory inventory, PlayerEntity var3) {
                                                    return GenericContainer.method_19247(id, inventory, new DoubleInventory(entity.inventory, entity.getEnderChestInventory()));
                                                }
                                            });
                                        }
                                        return 1;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return 1;
                                }))
                .executes(context -> {
                            context.getSource().sendError(new StringTextComponent("You must specify at least one player."));
                            return 1;
                });

        dispatcher.register(builder);
    }

}
