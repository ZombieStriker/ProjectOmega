package com.projectomega.main.game.inventory;

import com.projectomega.main.game.Player;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.UnsignedByte;
import com.projectomega.main.packets.datatype.VarInt;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private ItemStack[] slots;
    private InventoryType type;
    private byte windowID;
    private List<Player> viewers = new ArrayList<>();

    public Inventory(InventoryType type, byte windowID){
        this.slots = new ItemStack[type.getSlots()];
        this.windowID = windowID;
        this.type = type;
    }

    public byte getWindowID() {
        return windowID;
    }
    public void setItem(int slot, ItemStack is){
        this.slots[slot]=is;
        sendItemChangePacket(slot);
    }
    public ItemStack getItem(int slot){
        return slots[slot];
    }

    private void sendItemRemovePacket(int slot) {
        OutboundPacket packet = new OutboundPacket(PacketType.WINDOW_ITEMS, new UnsignedByte(windowID),(short)1,false);
        for(Player player: viewers){
            player.sendPacket(packet);
        }
    }

    private void sendItemChangePacket(int slot) {
        ItemStack is = slots[slot];
        OutboundPacket packet = new OutboundPacket(PacketType.WINDOW_ITEMS, new UnsignedByte(windowID),(short)1,true,new VarInt(1), (byte)is.getAmount(),(byte)0);
        for(Player player: viewers){
            player.sendPacket(packet);
        }
    }

    public void removeViewer(Player player) {
        this.viewers.remove(player);
    }

    public void addViewer(Player player) {
        this.viewers.add(player);
    }

    public InventoryType getType() {
        return type;
    }
}
