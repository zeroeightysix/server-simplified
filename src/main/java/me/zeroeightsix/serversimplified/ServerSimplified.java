package me.zeroeightsix.serversimplified;

import me.zeroeightsix.serversimplified.commands.VanishCommand;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffects;

import java.io.IOException;

public class ServerSimplified implements ModInitializer {

    private static Configuration configuration;

    @Override
    public void onInitialize() {
        this.configuration = Configuration.load();
    }

    public static void shutdown() {
        try {
            configuration.save();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Server Simplified: couldn't save configuration!");
        }

        VanishCommand.getVanished().forEach(entity -> entity.removeStatusEffect(StatusEffects.INVISIBILITY));
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

}
