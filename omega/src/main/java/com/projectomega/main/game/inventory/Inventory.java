package com.projectomega.main.game.inventory;

import com.projectomega.main.game.Material;
import com.projectomega.main.game.Player;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.datatype.Slot;
import com.projectomega.main.packets.datatype.UnsignedByte;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.versions.ProtocolManager;
import me.nullicorn.nedit.type.NBTCompound;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    protected ItemStack[] slots;
    private InventoryType type;
    private byte windowID;
    private List<Player> viewers = new ArrayList<>();

    public Inventory(InventoryType type, byte windowID) {
        this.slots = new ItemStack[type.getSlots()];
        this.windowID = windowID;
        this.type = type;
    }

    public byte getWindowID() {
        return windowID;
    }

    public void setItem(int slot, ItemStack is) {
        this.slots[slot] = is;
        sendItemChangePacket(slot);
    }

    public ItemStack getItem(int slot) {
        return slots[slot];
    }

    private void sendItemRemovePacket(int slot) {
        OutboundPacket packet = new OutboundPacket(PacketType.WINDOW_ITEMS, windowID, (short) 1, false);
        for (Player player : viewers) {
            player.sendPacket(packet);
        }
    }

    protected void sendItemChangePacket(int slot) {
        for (Player player : viewers) {
            Slot[] sendSlots = new Slot[slots.length];
            for (int i = 0; i < slots.length; i++) {
                ItemStack is = slots[i];
                if (is == null) {
                    sendSlots[i] = new Slot((short) (int) (ProtocolManager.getMaterialIDFromType(player.getProtocolVersion(), Material.AIR)), (byte) 0, (short) 0, new NBTCompound());
                } else {
                    sendSlots[i] = new Slot((short) (int) (ProtocolManager.getMaterialIDFromType(player.getProtocolVersion(), is.getMaterial())), (byte) (is.getAmount()), (short) 0, new NBTCompound());
                }
            }
            OutboundPacket packet = new OutboundPacket(PacketType.WINDOW_ITEMS, windowID, (short) sendSlots.length, sendSlots);
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
