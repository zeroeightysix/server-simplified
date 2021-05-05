package me.zeroeightsix.serversimplified;

import com.google.gson.*;
import me.zeroeightsix.serversimplified.commands.MuteCommand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Configuration {

    public static final String PREFERRED_FILENAME = "serversimplified_configuration.json";

    private final Path origin;
    private final Permissions permissions;
    private final List<String> muteWhitelist;

    public Configuration(Path origin) {
        this(origin, new JsonArray(), new ArrayList<>());
    }

    public Configuration(Path origin, JsonArray permissions, List<String> muteWhitelist) {
        this.origin = origin;
        this.permissions = Permissions.loadFromJson(permissions);
        this.muteWhitelist = muteWhitelist;
    }

    /**
     * Load a configuration file
     *
     * @param path The path to read from
     * @return The constructed configuration object
     */
    public static Configuration loadFromPath(Path path) {
        try {
            String jsonString = new String(Files.readAllBytes(path));
            JsonObject object = new JsonParser().parse(jsonString).getAsJsonObject();
            JsonArray permissions = object.getAsJsonArray("permissions");
            JsonArray muteWhitelist = object.getAsJsonArray("mute_whitelist");
            JsonObject muted = object.getAsJsonObject("muted");
            MuteCommand.fromJson(muted);
            ArrayList<String> whitelist = new ArrayList<>();
            if (muteWhitelist != null) {
                muteWhitelist.forEach(jsonElement -> {
                    if (jsonElement.isJsonPrimitive()) {
                        whitelist.add(jsonElement.getAsString());
                    }
                });
            }
            return new Configuration(path, permissions, whitelist);
        } catch (Exception e) {
            e.printStackTrace();
            return new Configuration(path);
        }
    }

    /**
     * @return {@link #loadFromPath(Path)} with the default configuration filename
     */
    public static Configuration load() {
        return loadFromPath(Paths.get(PREFERRED_FILENAME));
    }

    public void save() throws IOException {
        JsonObject object = new JsonObject();

        object.add("permissions", getPermissions().toJson());
        object.add("muted", MuteCommand.toJson());
        final JsonArray muteWhitelist = new JsonArray();
        for (String s : this.muteWhitelist) {
            muteWhitelist.add(s);
        }
        object.add("mute_whitelist", muteWhitelist);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Files.write(origin, gson.toJson(object).getBytes());
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public List<String> getMuteWhitelist() {
        return muteWhitelist;
    }
}
