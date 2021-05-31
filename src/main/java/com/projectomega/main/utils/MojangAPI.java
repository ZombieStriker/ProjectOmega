package com.projectomega.main.utils;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import io.netty.handler.codec.json.JsonObjectDecoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MojangAPI {

    public static final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private static final String NAME_URL = "https://api.mojang.com/users/profiles/minecraft/";

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
                return s.split(",")[0].split("id\":")[1].replaceAll("\"", "")
                        .trim();

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
        JsonObject profile = new JsonObject();
        Jsoner.deserialize(profileJson, profile);
        return profile;
    }
}
