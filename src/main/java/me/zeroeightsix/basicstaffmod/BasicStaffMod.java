package me.zeroeightsix.basicstaffmod;

import net.fabricmc.api.ModInitializer;
import sun.security.krb5.Config;

import java.io.IOException;
import java.nio.file.Paths;

public class BasicStaffMod implements ModInitializer {

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
            System.err.println("Basic staff mod: couldn't save configuration!");
        }
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

}
