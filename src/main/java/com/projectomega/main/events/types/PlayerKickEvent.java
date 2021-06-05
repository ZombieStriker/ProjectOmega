package com.projectomega.main.events.types;

import com.projectomega.main.events.Event;
import com.projectomega.main.game.Player;

public class PlayerKickEvent extends Event {

    private final Player playerKicked;
    private String leaveMessage;
    private String kickReason;

    public PlayerKickEvent(Player playerKicked, String kickReason, String leaveMessage) {
        this.playerKicked = playerKicked;
        this.kickReason = kickReason;
        this.leaveMessage = leaveMessage;
    }

    public Player getPlayer() {
        return playerKicked;
    }

    public String getReason() {
        return kickReason;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setReason(String kickReason) {
        this.kickReason = kickReason;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }
}
