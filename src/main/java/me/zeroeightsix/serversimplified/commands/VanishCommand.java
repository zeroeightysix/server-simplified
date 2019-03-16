package me.zeroeightsix.serversimplified.commands;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashSet;
import java.util.Set;

public class VanishCommand extends PlayerActionCommand {

    static Set<ServerPlayerEntity> vanished = new HashSet<>();

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
        } else {
            vanished.add(p);
            p.addPotionEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, Integer.MAX_VALUE, 1, false, false, false));
        }
    }

    @Override
    protected String getName() {
        return "vanish";
    }
}
