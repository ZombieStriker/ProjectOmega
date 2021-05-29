package com.projectomega.main.game;

import com.projectomega.main.packets.OutboundPacket;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private Channel connection;

    private String locale = "en_GB";
    private int renderDistance = 10;
    private int chatmode = 0;
    private boolean chatColors = true;
    private byte displayedSkinParts = 127;
    private int mainHand = 1;

    public String getLocale(){
        return locale;
    }
    public int getRenderDistance(){
        return renderDistance;
    }
    public int getChatmode(){
        return chatmode;
    }
    public boolean getChatColorsEnabled(){
        return chatColors;
    }
    public boolean isMainhandRighthanded(){
        return mainHand == 1;
    }

    /**
     * DOES NOT ACTUALLY CHANGE THE PLAYER'S SETTINGS
     * @param displayedSkinParts
     */
    @Deprecated
    public void setRenderDistance(byte displayedSkinParts){
        this.displayedSkinParts=displayedSkinParts;
    }
    /**
     * DOES NOT ACTUALLY CHANGE THE PLAYER'S SETTINGS
     * @param renderDistance
     */
    @Deprecated
    public void setRenderDistance(int renderDistance){
        this.renderDistance = renderDistance;
    }
    /**
     * DOES NOT ACTUALLY CHANGE THE PLAYER'S SETTINGS
     * @param mode
     */
    @Deprecated
    public void setChatMode(int mode){
        chatmode = mode;
    }

    /**
     * DOES NOT ACTUALLY CHANGE THE PLAYER'S SETTINGS
     * @param chatcolors
     */
    @Deprecated
    public void setChatColors(boolean chatcolors){
        this.chatColors = chatcolors;
    }

    private List<OutboundPacket> outgoingPackets = new ArrayList<>();

    public List<OutboundPacket> getOutgoingPackets(){
        return new ArrayList<>(outgoingPackets);
    }
    public void sendPacket(OutboundPacket packet){
        this.outgoingPackets.add(packet);
    }
    public void cancelPacket(OutboundPacket packet){
        outgoingPackets.remove(packet);
    }

    public Player(Channel connection){
        this.connection = connection;
    }
    public Channel getConnection(){
        return connection;
    }

    public void clearPackets() {
        outgoingPackets.clear();
    }

    /**
     * DOES NOT ACTUALLY CHANGE THE PLAYER'S SETTINGS
     * @param locale
     */
    @Deprecated
    public void setLocale(String locale) {
        this.locale = locale;
    }
}
