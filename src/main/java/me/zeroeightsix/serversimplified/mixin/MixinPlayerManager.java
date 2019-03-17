package me.zeroeightsix.serversimplified.mixin;

import me.zeroeightsix.serversimplified.commands.VanishCommand;
import net.minecraft.client.network.packet.PlayerListS2CPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {

    @Inject(method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V",
            at = @At("RETURN"))
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity entity, CallbackInfo info) {
        connection.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Type.REMOVE, VanishCommand.getVanished()));
    }

}
