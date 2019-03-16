package me.zeroeightsix.basicstaffmod.mixin;

import me.zeroeightsix.basicstaffmod.commands.MuteCommand;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.ChatMessageC2SPacket;
import net.minecraft.sortme.ChatMessageType;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onChatMessage(Lnet/minecraft/server/network/packet/ChatMessageC2SPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcastChatMessage(Lnet/minecraft/text/TextComponent;Z)V"), cancellable = true)
    public void broadcastChatMessage(ChatMessageC2SPacket packet, CallbackInfo info) {
        if (MuteCommand.mutedPlayers.contains(player.getUuidAsString())) {
            player.sendChatMessage(new StringTextComponent("You were muted! Could not send message.").applyFormat(TextFormat.RED), ChatMessageType.CHAT);
            info.cancel();
        }
    }

}
