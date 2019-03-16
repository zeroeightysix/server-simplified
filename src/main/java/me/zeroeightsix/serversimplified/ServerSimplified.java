package me.zeroeightsix.serversimplified;

import net.fabricmc.api.ModInitializer;

import java.io.IOException;

public class ServerSimplified implements ModInitializer {

    private static Configuration configuration;

    @Override
    public void onInitialize() {
        this.configuration = Configuration.load();
    }

    public static void save() {
        try {
            configuration.save();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Server Simplified: couldn't save configuration!");
        }
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

}
