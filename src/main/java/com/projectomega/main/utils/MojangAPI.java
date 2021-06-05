package com.projectomega.main.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.projectomega.main.game.player.GameProfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class MojangAPI {

    public static final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private static final String NAME_URL = "https://api.mojang.com/users/profiles/minecraft/";

    private static final Map<UUID, GameProfile> gameprofiles = new HashMap<>();

    public static String getUUIDFromName(String name) {
        try {
            URL url = new URL(NAME_URL + name);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            List<String> list = new ArrayList<>();
            String input = null;
            while ((input = br.readLine()) != null) {
                list.add(input);
            }
            br.close();

            for (String s : list) {
                System.out.println(s);
                for(String k : s.split(","))
                    if(k.startsWith("\"id\"")) {
                        String temp = k.split("\"id\":\"")[1].replaceAll("\"", "")
                                .trim().replaceAll("\\}", "").replaceAll("\\{", "");
                        StringBuilder sb = new StringBuilder();
                        for(int i = 0; i < temp.length();i++){
                            sb.append(temp.charAt(i));
                            if(i==7||i==11||i==15||i==19)
                                sb.append("-");
                        }
                        System.out.println(sb.toString());
                        return sb.toString();
                    }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getProfile(UUID uuid) {
        StringBuilder code = new StringBuilder();
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(new URL(
                    PROFILE_URL
                            + uuid.toString().replace("-", "")).openStream());
            int charI = 0;
            while ((charI = is.read()) != -1) {
                code.append(((char) charI));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code.toString();
    }

    public static JsonObject getProfileJson(UUID uuid) {
        String profileJson = getProfile(uuid);
        return JsonParser.parseString(profileJson).getAsJsonObject();
    }

    public static GameProfile getGameProfile(UUID uuid) {
        if (gameprofiles.containsKey(uuid))
            gameprofiles.get(uuid);
        JsonObject json = getProfileJson(uuid);
        //GameProfile gameProfile = new GameProfile(json.getString(Jsoner.mintJsonKey("name",null)),json.getString(Jsoner.mintJsonKey("value",null)));
        //gameprofiles.put(uuid,gameProfile);
        //return gameProfile;
        return null;
        //TODO: Figure out how to get the keys from json
    }
}
