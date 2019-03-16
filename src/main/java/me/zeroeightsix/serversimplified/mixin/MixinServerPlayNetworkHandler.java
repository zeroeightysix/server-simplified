package me.zeroeightsix.serversimplified.mixin;

import me.zeroeightsix.serversimplified.commands.MuteCommand;
import me.zeroeightsix.serversimplified.commands.StaffChatCommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.ChatMessageC2SPacket;
import net.minecraft.sortme.ChatMessageType;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import org.apache.commons.lang3.StringUtils;
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

    @Inject(method = "onChatMessage(Lnet/minecraft/server/network/packet/ChatMessageC2SPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcastChatMessage(Lnet/minecraft/text/TextComponent;Z)V"), cancellable = true)
    public void broadcastChatMessage(ChatMessageC2SPacket packet, CallbackInfo info) {
        if (MuteCommand.isMuted(player.getUuidAsString())) {
            player.sendChatMessage(new StringTextComponent("You were muted! Could not send message.").applyFormat(TextFormat.RED), ChatMessageType.CHAT);
            info.cancel();
        } else if (StaffChatCommand.isInStaffChat(player.getUuidAsString())) {
            String message = packet.getChatMessage();
            StaffChatCommand.sendToStaffChat(StaffChatCommand.generateStaffChatMessage(player.getDisplayName().getFormattedText(), message), server);
            info.cancel();
        }
    }

}
