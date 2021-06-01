package com.projectomega.main.events.types;

import com.projectomega.main.events.Cancellable;
import com.projectomega.main.events.Event;
import com.projectomega.main.game.OfflinePlayer;
import com.projectomega.main.game.Omega;
import io.netty.channel.Channel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Cancellable
public class PingServerEvent extends Event {

    private Channel channel;
    private String MOTD;
    private int versionID;
    private BufferedImage icon;
    private int playercount= Omega.getPlayers().size();
    private int maxplayercount = 100;
    private List<OfflinePlayer> players = new ArrayList<>();

    public PingServerEvent(Channel channel, String MOTD, int id, BufferedImage icon){
        this.channel = channel;
        this.MOTD = MOTD;
        this.versionID = id;
        this.icon = icon;
    }

    public int getPlayercount() {
        return playercount;
    }

    public void setPlayercount(int playercount) {
        this.playercount = playercount;
    }

    public void setMaxplayercount(int maxplayercount) {
        this.maxplayercount = maxplayercount;
    }

    public int getMaxplayercount() {
        return maxplayercount;
    }
    public void addPlayerToList(OfflinePlayer player){
        players.add(player);
    }
    public void removePlayerFromList(OfflinePlayer player) {
        players.remove(player);
    }

    public List<OfflinePlayer> getPlayers() {
        return players;
    }

    public String getMOTD(){
        return MOTD;
    }

    public void setMOTD(String MOTD) {
        this.MOTD = MOTD;
    }

    public void setVersionID(int versionID) {
        this.versionID = versionID;
    }

    public int getVersionID() {
        return versionID;
    }

    public void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    public BufferedImage getIcon() {
        return icon;
    }
}
