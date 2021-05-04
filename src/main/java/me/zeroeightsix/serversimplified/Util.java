package me.zeroeightsix.serversimplified;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.Collection;

public class Util {

    public static boolean isHuman(ServerCommandSource source) {
        return source.getEntity() instanceof ServerPlayerEntity;
    }

    public static Text namePlayers(Collection<ServerPlayerEntity> players) {
        switch (players.size()) {
            case 0:
                return new LiteralText("no one");
            case 1:
                return players.iterator().next().getDisplayName();
            default:
                return new LiteralText(players.size() + " players");
        }
    }

}
