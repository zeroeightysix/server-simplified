package me.zeroeightsix.serversimplified;

import me.zeroeightsix.serversimplified.commands.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.effect.StatusEffects;

import java.io.IOException;

public class ServerSimplified implements ModInitializer {

    private static Configuration configuration;

    @Override
    public void onInitialize() {
//        configuration = Configuration.load();

        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            PlayerActionCommand.register(dispatcher, HealCommand.class);
            PlayerActionCommand.register(dispatcher, FeedCommand.class);
            PlayerActionCommand.register(dispatcher, VanishCommand.class);
            MuteCommand.register(dispatcher);
            SeekInventoryCommand.register(dispatcher);
            StaffChatCommand.register(dispatcher);
            PermissionCommand.register(dispatcher);
        }));
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
