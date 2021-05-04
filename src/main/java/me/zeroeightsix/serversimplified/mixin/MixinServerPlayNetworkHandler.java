package me.zeroeightsix.serversimplified.mixin;

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

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {

    @Shadow
    public ServerPlayerEntity player;
    @Shadow
    private MinecraftServer server;

    @Inject(method = "onGameMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;method_31286(Lnet/minecraft/server/filter/TextStream$Message;)V"), cancellable = true)
    public void broadcastChatMessage(ChatMessageC2SPacket packet, CallbackInfo info) {
        if (MuteCommand.isMuted(player.getUuidAsString())) {
            player.sendMessage(new LiteralText("You were muted! Could not send message.").formatted(Formatting.RED), MessageType.CHAT, null);
            info.cancel();
        } else if (StaffChatCommand.isInStaffChat(player.getUuidAsString())) {
            String message = packet.getChatMessage();
            StaffChatCommand.sendToStaffChat(StaffChatCommand.generateStaffChatMessage(player.getDisplayName().asString(), message), server);
            info.cancel();
        }
    }

}
