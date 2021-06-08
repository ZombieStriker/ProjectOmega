package com.projectomega.main.packets.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.UnsignedByte;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.utils.NBTTagUtil;
import me.nullicorn.nedit.type.NBTCompound;

public class PacketJoinGame extends OutboundPacket {

    public PacketJoinGame(Player player) {
        super(PacketType.JOIN_GAME, player.getProtocolVersion());
        NBTCompound dimensionCodec = NBTTagUtil.generateDimensionCodec();
        setData(player.getEntityID(), true, new UnsignedByte((byte) 0), (byte) -1, new VarInt(1), "overworld", dimensionCodec, NBTTagUtil.generateDimensionType(), "overworld", 0l, new VarInt(32), new VarInt(10), false, true, false, false);
    }
}
