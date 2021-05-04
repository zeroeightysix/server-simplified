package me.zeroeightsix.serversimplified.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.zeroeightsix.serversimplified.Util;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class VanishCommand implements Registrable {
    private static final Set<ServerPlayerEntity> vanished = new HashSet<>();

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        Util.registerPlayersCommand(dispatcher, "vanish", VanishCommand::toggleVanish);
    }

    private static int toggleVanish(CommandContext<ServerCommandSource> ctx, Collection<ServerPlayerEntity> players) {
        if (players.stream().map(VanishCommand::isVanished).distinct().limit(2).count() <= 1) {
            // All the players are either vanished or not vanished.
            boolean vanished = isVanished(players.iterator().next());
            players.forEach(VanishCommand::vanishPlayer);
            if (vanished) {
                // they are now not vanished:
                ctx.getSource().sendFeedback(Util.namePlayers(players).append((players.size() == 1 ? " is " : " are ") + "no longer vanished"), false);
            } else {
                // they now are vanished:
                ctx.getSource().sendFeedback(Util.namePlayers(players).append((players.size() == 1 ? " is " : " are ") + "now vanished"), false);
            }
        } else {
            ctx.getSource().sendFeedback(new LiteralText("All players supplied must be either vanished or not vanished, without mixing.").formatted(Formatting.RED), false);
        }
        return 0;
    }

    private static boolean isVanished(ServerPlayerEntity next) {
        return vanished.contains(next);
    }

    public static void vanishPlayer(ServerPlayerEntity p) {
        if (isVanished(p)) {
            vanished.remove(p);
            p.removeStatusEffect(StatusEffects.INVISIBILITY);
            PlayerListS2CPacket packet = new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER, p);
            p.getServer().getPlayerManager().sendToAll(packet);
        } else {
            vanished.add(p);
            PlayerListS2CPacket packet = new PlayerListS2CPacket(PlayerListS2CPacket.Action.REMOVE_PLAYER, p);
            p.getServer().getPlayerManager().sendToAll(packet);
            p.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, Integer.MAX_VALUE, 1, false, false, false));
        }
    }

    public static Set<ServerPlayerEntity> getVanished() {
        return vanished;
    }
}