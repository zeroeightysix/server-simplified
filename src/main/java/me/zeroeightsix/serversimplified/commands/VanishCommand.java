package me.zeroeightsix.serversimplified.commands;

import net.minecraft.client.network.packet.PlayerListS2CPacket;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashSet;
import java.util.Set;

public class VanishCommand extends PlayerActionCommand {

    private static Set<ServerPlayerEntity> vanished = new HashSet<>();

    public VanishCommand() {
        super(VanishCommand::vanishPlayer, serverPlayerEntities -> isVanished(serverPlayerEntities.iterator().next()) ? "Vanished" : "Appeared");
    }

    private static boolean isVanished(ServerPlayerEntity next) {
        return vanished.contains(next);
    }

    private static void vanishPlayer(ServerPlayerEntity p) {
        if (isVanished(p)) {
            vanished.remove(p);
            p.removeStatusEffect(StatusEffects.INVISIBILITY);
            PlayerListS2CPacket packet = new PlayerListS2CPacket(PlayerListS2CPacket.Type.ADD, p);
            p.getServer().getPlayerManager().sendToAll(packet);
        } else {
            vanished.add(p);
            PlayerListS2CPacket packet = new PlayerListS2CPacket(PlayerListS2CPacket.Type.REMOVE, p);
            p.getServer().getPlayerManager().sendToAll(packet);
            p.addPotionEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, Integer.MAX_VALUE, 1, false, false, false));
        }
    }

    public static Set<ServerPlayerEntity> getVanished() {
        return vanished;
    }

    @Override
    protected String getName() {
        return "vanish";
    }
}
