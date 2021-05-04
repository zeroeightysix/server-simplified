package me.zeroeightsix.serversimplified;

import me.zeroeightsix.serversimplified.commands.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.effect.StatusEffects;

import java.util.Arrays;
import java.util.List;

public class ServerSimplified implements ModInitializer {

    private static Configuration configuration;

    @Override
    public void onInitialize() {
        System.out.println("SERVER SIMPLIFIED RAN!");
        final List<Registrable> commands = Arrays.asList(new FeedCommand());
        configuration = Configuration.load();

        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            commands.forEach(registrable -> registrable.register(dispatcher));

            PlayerActionCommand.register(dispatcher, HealCommand.class);
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
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Server Simplified: couldn't save configuration!");
        }

        VanishCommand.getVanished().forEach(entity -> entity.removeStatusEffect(StatusEffects.INVISIBILITY));
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

}
