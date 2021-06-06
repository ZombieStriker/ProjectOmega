package com.projectomega.main.game.chat;

import com.projectomega.main.game.Player;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;

import java.util.UUID;

public class BossBar {

    private UUID bossbaruuid;
    private String title;
    private float health;
    private BossBarColor color;
    private BossBarDivisions divisions = BossBarDivisions.NONE;
    private boolean darkenSky = false;
    private boolean dragonBar = false;
    private boolean createFog = false;

    public BossBar(UUID uuid, String title, float health, BossBarColor color, BossBarDivisions divisions, boolean darkenSky, boolean dragonBar, boolean createFog) {
        bossbaruuid = uuid;
        this.title = title;
        this.health = health;
        this.color = color;
        this.divisions = divisions;
        this.darkenSky = darkenSky;
        this.dragonBar = dragonBar;
        this.createFog = createFog;
    }

    public void sendBossbarPacketToPlayer(Player player) {
        String titleJson = TextMessage.text(title);
        OutboundPacket outboundPacket = new OutboundPacket(PacketType.BOSS_BAR, bossbaruuid, new VarInt(0), titleJson, health, new VarInt(color.getId()), new VarInt(divisions.getId()), (byte) 0);
        player.sendPacket(outboundPacket);
    }

    public void removeBossbarFromPlayer(Player player) {
        OutboundPacket outboundPacket = new OutboundPacket(PacketType.BOSS_BAR, bossbaruuid, new VarInt(1));
        player.sendPacket(outboundPacket);
    }

    public boolean isCreateFog() {
        return createFog;
    }

    public boolean isDarkenSky() {
        return darkenSky;
    }

    public boolean isDragonBar() {
        return dragonBar;
    }

    public BossBarColor getColor() {
        return color;
    }

    public BossBarDivisions getDivisions() {
        return divisions;
    }

    public float getHealth() {
        return health;
    }

    public String getTitle() {
        return title;
    }

    public UUID getBossbaruuid() {
        return bossbaruuid;
    }
}
