package com.projectomega.main.packets.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.game.chat.BossBarColor;
import com.projectomega.main.game.chat.BossBarDivisions;
import com.projectomega.main.game.chat.TextMessage;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;

import java.util.UUID;

public class PacketBossBar extends OutboundPacket {
    public PacketBossBar(Player player) {
        super(PacketType.BOSS_BAR,player.getProtocolVersion());
    }
    public PacketBossBar setAddBossBar(UUID bossbaruuid, String title, float health, BossBarColor color, BossBarDivisions divisions){
        String titleJson = TextMessage.text(title);
        setData(bossbaruuid, new VarInt(0), titleJson, health, new VarInt(color.getId()), new VarInt(divisions.getId()), (byte) 0);
        return this;
    }
    public PacketBossBar setRemoveBossBar(UUID bossbaruuid){
        setData(bossbaruuid, new VarInt(1));
        return this;
    }
}
