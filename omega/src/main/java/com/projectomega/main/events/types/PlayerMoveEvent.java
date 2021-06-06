package com.projectomega.main.events.types;


import com.projectomega.main.events.Cancellable;
import com.projectomega.main.events.Event;
import com.projectomega.main.game.Location;
import com.projectomega.main.game.Player;
import com.projectomega.main.game.chat.TranslatedComponent;

@Cancellable
public class PlayerMoveEvent extends Event {

    private final Location newLocation;
    private final Player player;

    public PlayerMoveEvent(Player player, Location newLocation) {
        this.player = player;
        this.newLocation = newLocation;
    }

    public Location getNewLocation(){return newLocation;}

    public Player getPlayer() {
        return player;
    }
}