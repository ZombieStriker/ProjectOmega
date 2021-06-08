package com.projectomega.main.game.chat;

import com.projectomega.main.game.Player;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.packets.types.PacketBossBar;

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
        player.sendPacket(new PacketBossBar(player).setAddBossBar(bossbaruuid,title,health,color,divisions));
    }

    public void removeBossbarFromPlayer(Player player) {
        player.sendPacket(new PacketBossBar(player).setRemoveBossBar(bossbaruuid));
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
