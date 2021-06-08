package com.projectomega.main.packets.types;

import com.projectomega.main.game.Location;
import com.projectomega.main.game.Player;
import com.projectomega.main.game.sound.Sound;
import com.projectomega.main.game.sound.SoundCategory;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.VarInt;

public class PacketSoundEffect extends OutboundPacket {
    public PacketSoundEffect(Player player, Sound sound, SoundCategory category, Location location, float volume, float pitch) {
        super(PacketType.SOUND_EFFECT, player.getProtocolVersion(),  new VarInt(sound.getId()), new VarInt(category.getId()), location.getBlockX() * 8, location.getBlockY() * 8, location.getBlockZ() * 8, volume, pitch);
    }
}
