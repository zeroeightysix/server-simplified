package me.zeroeightsix.basicstaffmod;

import com.google.gson.*;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.zeroeightsix.basicstaffmod.Util.isHuman;

public class Permissions {

    private HashMap<String, List<String>> permissions;

    private Permissions(HashMap<String, List<String>> permissions) {
        this.permissions = permissions;
    }

    /**
     * Check if this ServerCommandSource has permission to use commands linked to given node.
     * @param source    The ServerCommandSource to check permissions for
     * @param node      The permission node to check for
     * @return true if the ServerCommandSource has permission level 2 or explicit access to given node.
     */
    public boolean checkPermissions(ServerCommandSource source, String node) {
        return source.hasPermissionLevel(2) || (isHuman(source) && hasPermission(source.getEntity(), node));
    }

    private boolean hasPermission(Entity entity, String node) {
        if (entity == null) return false;
        String uuid = entity.getUuidAsString();
        if (!permissions.containsKey(uuid)) return false;
        return permissions.get(uuid).contains(node);
    }

    /**
     * Attempts to parse permissions from json and returns the constructed permissions object.
     * @param players   The JSON object representing this permissions object
     * @return          A empty permissions object, if the object was null or malformed. Otherwise, the permissions object constructed from the path.
     */
    public static Permissions loadFromJson(JsonArray players) {
        HashMap<String, List<String>> permissionMap = new HashMap<>();

        try {
            for (int i = 0; i < players.size(); i++) {
                JsonObject player = players.get(i).getAsJsonObject();
                String uuid = player.get("uuid").getAsString();
                JsonArray permissions = player.getAsJsonArray("permissions");

                List<String> permissionList = new ArrayList<>();
                for (JsonElement element : permissions) {
                    permissionList.add(element.getAsString());
                }

                permissionMap.put(uuid, permissionList);
            }
        } catch (IllegalStateException e) {
            return create(); // Format malformed
        }

        return new Permissions(permissionMap);
    }

    public JsonElement toJson() {
        JsonArray array = new JsonArray();
        for (Map.Entry<String, List<String>> entry : permissions.entrySet()) {
            String uuid = entry.getKey();
            JsonArray permissionArray = new JsonArray();
            for (String s : entry.getValue()) {
                permissionArray.add(new JsonPrimitive(s));
            }

            JsonObject full = new JsonObject();
            full.add("uuid", new JsonPrimitive(uuid));
            full.add("permissions", permissionArray);
            array.add(full);
        }
        return array;
    }

    private static Permissions create() {
        return new Permissions(new HashMap<>());
    }

}
