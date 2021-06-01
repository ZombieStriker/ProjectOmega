package com.projectomega.main.game;

import java.util.UUID;

public class OfflinePlayer {

    private String name;
    private UUID uuid;

    public OfflinePlayer(String name, UUID uuid){
        this.uuid = uuid;
        this.name  = name;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }
}
