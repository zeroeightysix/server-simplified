package me.zeroeightsix.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.zeroeightsix.serversimplified.ServerSimplified;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

import static me.zeroeightsix.serversimplified.Util.isHuman;

public class StaffChatCommand implements Registrable {

    private static final Set<String> staffChat = new HashSet<>();
    static boolean consoleInSChat = false;

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager
                .literal("staffchat")
                .requires(
                        (commandSource) ->
                                ServerSimplified.getConfiguration().getPermissions().checkPermissions(commandSource, "staffchat")
                                        || commandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("message", MessageArgumentType.message())
                        .executes(context -> {
                            sendToStaffChat(generateStaffChatMessage(isHuman(context.getSource()) ? context.getSource().getEntity().getDisplayName().asString() : "Console", MessageArgumentType.getMessage(context, "message").asString()), context.getSource().getMinecraftServer());
                            return 1;
                        }))
                .executes(context -> {
                    boolean added = false;
                    if (isHuman(context.getSource())) {
                        String uuid = context.getSource().getPlayer().getUuidAsString();
                        if (staffChat.contains(uuid)) {
                            staffChat.remove(uuid);
                        } else {
                            staffChat.add(uuid);
                            added = true;
                        }
                    } else {
                        consoleInSChat = !consoleInSChat;
                        added = consoleInSChat;
                    }

                    context.getSource().sendFeedback(new LiteralText(added ? "Moved into staff chat." : "Moved into global chat."), false);
                    return 1;
                });

        dispatcher.register(builder);
    }

    public static Text generateStaffChatMessage(String name, String message) {
        message = StringUtils.normalizeSpace(message);
        MutableText originalMessage = new TranslatableText("chat.type.text", name, message);
        return new LiteralText("[SC] ").formatted(Formatting.RED).append(originalMessage.formatted(Formatting.WHITE));
    }

    public static boolean isInStaffChat(String uuid) {
        return staffChat.contains(uuid);
    }

    public static void sendToStaffChat(Text message, MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach(player -> {
            if (ServerSimplified.getConfiguration().getPermissions().hasPermission(player.getUuidAsString(), "staffchat")
                    || ServerSimplified.getConfiguration().getPermissions().hasPermission(player.getUuidAsString(), "staffchat.view")
                    || player.hasPermissionLevel(2)) {
                player.sendMessage(message, MessageType.CHAT, null);
            }
        });
    }

}
