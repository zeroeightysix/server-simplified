package me.zeroeightsix.serversimplified.mixin;

import me.zeroeightsix.serversimplified.ServerSimplified;
import me.zeroeightsix.serversimplified.commands.MuteCommand;
import me.zeroeightsix.serversimplified.commands.StaffChatCommand;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {

    @Shadow
    public ServerPlayerEntity player;
    @Shadow
    private MinecraftServer server;

    @Inject(method = "onGameMessage", at = @At(value = "INVOKE", target = "Ljava/lang/String;startsWith(Ljava/lang/String;)Z", shift = At.Shift.BEFORE), cancellable = true)
    public void broadcastChatMessage(ChatMessageC2SPacket packet, CallbackInfo info) {
        final UUID uuid = player.getUuid();
        String message = packet.getChatMessage();
        if (uuid != null && MuteCommand.isMuted(uuid.toString())) {
            if (!message.isEmpty() && !ServerSimplified.getConfiguration().getMuteWhitelist().contains(message.substring(1).split(" ")[0])) {
                player.sendMessage(new LiteralText("You were muted! Could not send message.").formatted(Formatting.RED), MessageType.CHAT, null);
                info.cancel();
            }
        } else if (!message.startsWith("/") && StaffChatCommand.isInStaffChat(player.getUuidAsString())) {
            StaffChatCommand.sendToStaffChat(StaffChatCommand.generateStaffChatMessage(player.getDisplayName().asString(), message), server);
            info.cancel();
        }
    }

}
