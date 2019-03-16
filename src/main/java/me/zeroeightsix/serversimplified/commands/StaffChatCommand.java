package me.zeroeightsix.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.zeroeightsix.serversimplified.Permissions;
import me.zeroeightsix.serversimplified.ServerSimplified;
import net.minecraft.command.arguments.MessageArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.sortme.ChatMessageType;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

import static me.zeroeightsix.serversimplified.Util.isHuman;

public class StaffChatCommand {

    private static Set<String> staffChat = new HashSet<>();
    static boolean consoleInSChat = false;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = ServerCommandManager
                .literal("staffchat")
                .requires(
                        (commandSource) ->
                                ServerSimplified.getConfiguration().getPermissions().checkPermissions(commandSource, "staffchat")
                                        || commandSource.hasPermissionLevel(2))
                .then(ServerCommandManager.argument("message", MessageArgumentType.create())
                        .executes(context -> {
                            sendToStaffChat(generateStaffChatMessage(isHuman(context.getSource()) ? context.getSource().getEntity().getDisplayName().getFormattedText() : "Console", MessageArgumentType.getMessageArgument(context, "message").getFormattedText()), context.getSource().getMinecraftServer());
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

                    context.getSource().sendFeedback(new StringTextComponent(added ? "Moved into staff chat." : "Moved into global chat."), false);
                    return 1;
                });

        dispatcher.register(builder);
    }

    public static TextComponent generateStaffChatMessage(String name, String message) {
        message = StringUtils.normalizeSpace(message);
        TextComponent originalMessage = new TranslatableTextComponent("chat.type.text", new Object[]{name, message});
        return new StringTextComponent("[SC] ").applyFormat(TextFormat.RED).append(originalMessage.applyFormat(TextFormat.WHITE));
    }

    public static boolean isInStaffChat(String uuid) {
        return staffChat.contains(uuid);
    }

    public static void sendToStaffChat(TextComponent message, MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
            if (ServerSimplified.getConfiguration().getPermissions().hasPermission(serverPlayerEntity.getUuidAsString(), "staffchat")
                    || ServerSimplified.getConfiguration().getPermissions().hasPermission(serverPlayerEntity.getUuidAsString(), "staffchat.view")
                    || serverPlayerEntity.allowsPermissionLevel(2)) {
                serverPlayerEntity.sendChatMessage(message, ChatMessageType.CHAT);
            }
        });
    }

}
