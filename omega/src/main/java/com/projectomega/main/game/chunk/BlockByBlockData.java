package com.projectomega.main.game.chunk;

import com.projectomega.main.game.Block;
import com.projectomega.main.game.Chunk;
import com.projectomega.main.game.Player;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.Position;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.versions.ProtocolManager;

public class BlockByBlockData {

    private byte[][][] sendPacket = new byte[16][256][16];
    private byte prioritylevel = 0;
    private Chunk chunk;
    protected static final byte TIMES_TO_SEND = 3;

    public BlockByBlockData(Chunk chunk){
        this.chunk = chunk;
    }

    public byte getPrioritylevel() {
        return prioritylevel;
    }
    public void setPrioritylevel(byte b){
        this.prioritylevel = b;
    }

    public byte timesSentPacket(int x , int y, int z){
        return sendPacket[x][y][z];
    }
    public void sendPacket(Player player, int x, int y, int z){
        Block block = chunk.getBlockAtChunkRelative(x,y,z);
        int blockid = ProtocolManager.getBlockIDByType(player.getProtocolVersion(),block.getType());
        if(blockid <= 0){
            sendPacket[x][y][z]= (byte) (sendPacket[x][y][z]+1);
            System.out.println("Failed to find blockid for "+block.getType());
            return;
        }
       //  OutboundPacket outboundPacket = new OutboundPacket(PacketType.BLOCK_CHANGE,new Position(x+(chunk.getX()*16),y,z+(chunk.getZ()*16)),new VarInt(blockid));
       // player.sendPacket(outboundPacket);
        sendPacket[x][y][z]= (byte) (sendPacket[x][y][z]+1);
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setAirAt(int x, int y, int z) {
        sendPacket[x][y][z]=TIMES_TO_SEND;
    }
}
