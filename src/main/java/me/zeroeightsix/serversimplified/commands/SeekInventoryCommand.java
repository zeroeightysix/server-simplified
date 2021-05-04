package me.zeroeightsix.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.zeroeightsix.serversimplified.ServerSimplified;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import static me.zeroeightsix.serversimplified.Util.isHuman;

public class SeekInventoryCommand implements Registrable {

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager
                .literal("seekinv")
                .requires(source -> ServerSimplified.getConfiguration().getPermissions().checkPermissions(source, "seekinv")
                        || source.hasPermissionLevel(2))
                .then(
                        CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> {
                                    try {
                                        if (!isHuman(context.getSource())) {
                                            context.getSource().sendError(new LiteralText("You must be a player to use this command!"));
                                            return 1;
                                        }
                                        ServerPlayerEntity sender = (ServerPlayerEntity) context.getSource().getEntity();
                                        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
                                        target.getInventory().onOpen(sender);
                                        sender.openHandledScreen(new NamedScreenHandlerFactory() {
                                            @Override
                                            public Text getDisplayName() {
                                                return target.getName();
                                            }

                                            @Override
                                            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                                                return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X4, syncId, inv, target.getInventory(), 4);
//                                                return GenericContainerScreenHandler.createGeneric9x4(syncId, inv);
                                            }
                                        });
                                        return 0;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return 0;
                                }))
                .executes(context -> {
                    context.getSource().sendError(new LiteralText("You must specify at least one player."));
                    return 1;
                });

        dispatcher.register(builder);
    }

}
