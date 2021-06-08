package com.projectomega.main.packets.handlers;

import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.packets.PacketHandler;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.PacketUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class PacketClientSettingsHandler extends PacketHandler {
    public PacketClientSettingsHandler() {
        super(PacketType.CLIENT_SETTINGS);
    }

    @Override
    public void call(ByteBuf bytebuf, int i, Channel channel) {
        String locale = PacketUtil.buildString(bytebuf);
        byte viewDistance = bytebuf.readByte();
        int chatEnabled = PacketUtil.readVarInt(bytebuf);
        boolean chatColors = PacketUtil.readBoolean(bytebuf);
        byte displayedSkinParts = PacketUtil.readUnsignedByte(bytebuf);
        int mainhand = PacketUtil.readVarInt(bytebuf);

        Player player = Omega.getPlayerByChannel(channel);
        if(player!=null){
            player.setLocale(locale);
            player.setRenderDistance(viewDistance);
            player.setChatMode(chatEnabled);
            player.setChatColors(chatColors);
        }
    }
}
