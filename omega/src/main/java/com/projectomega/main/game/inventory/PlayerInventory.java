package com.projectomega.main.game.inventory;

import com.projectomega.main.game.Player;

public class PlayerInventory extends Inventory{

    private Player player;

    public PlayerInventory(Player player) {
        super(InventoryType.PLAYER, (byte)0);
        this.player = player;
        addViewer(player);
    }
}
