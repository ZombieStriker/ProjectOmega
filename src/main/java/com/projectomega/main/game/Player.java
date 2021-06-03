package com.projectomega.main.game;

import com.projectomega.main.debugging.DebuggingUtil;
import com.projectomega.main.events.EventBus;
import com.projectomega.main.events.types.PlayerChatEvent;
import com.projectomega.main.game.chat.TextMessage;
import com.projectomega.main.game.entity.Entity;
import com.projectomega.main.game.entity.EntityType;
import com.projectomega.main.game.inventory.Inventory;
import com.projectomega.main.game.inventory.PlayerInventory;
import com.projectomega.main.game.player.GameProfile;
import com.projectomega.main.game.sound.Sound;
import com.projectomega.main.game.sound.SoundCategory;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketHandler;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.PacketUtil;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.utils.MojangAPI;
import com.projectomega.main.versions.ProtocolManager;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player extends OfflinePlayer implements CommandSender {

    private Channel connection;

    private Entity playerEntity;

    private int protocolVersion;

    private String locale = "en_GB";
    private int renderDistance = 10;
    private int chatmode = 0;
    private boolean chatColors = true;
    private byte displayedSkinParts = 127;
    private int mainHand = 1;
    private String displayname = "Notch";

    private int xp = 0;
    private float health = 20.0f;
    private int food = 20;
    private float foodSaturation = 0.0f;


    private Inventory viewedInventory = null;
    private Inventory playerInventory = new PlayerInventory();
    private int heldSlot = 0;
    private World world;
    private int viewdistance=-1;
    private boolean allowflight = false;
    private Location bedlocation;

    public String getLocale() {
        return locale;
    }

    public int getRenderDistance() {
        return renderDistance;
    }

    public int getChatmode() {
        return chatmode;
    }

    public boolean getChatColorsEnabled() {
        return chatColors;
    }

    public boolean isMainhandRighthanded() {
        return mainHand == 1;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Inventory getInventory() {
        return playerInventory;
    }

    /**
     * DOES NOT ACTUALLY CHANGE THE PLAYER'S SETTINGS
     *
     * @param displayedSkinParts
     */
    @Deprecated
    public void setRenderDistance(byte displayedSkinParts) {
        this.displayedSkinParts = displayedSkinParts;
    }

    /**
     * DOES NOT ACTUALLY CHANGE THE PLAYER'S SETTINGS
     *
     * @param renderDistance
     */
    @Deprecated
    public void setRenderDistance(int renderDistance) {
        this.renderDistance = renderDistance;
    }

    /**
     * DOES NOT ACTUALLY CHANGE THE PLAYER'S SETTINGS
     *
     * @param mode
     */
    @Deprecated
    public void setChatMode(int mode) {
        chatmode = mode;
    }

    /**
     * DOES NOT ACTUALLY CHANGE THE PLAYER'S SETTINGS
     *
     * @param chatcolors
     */
    @Deprecated
    public void setChatColors(boolean chatcolors) {
        this.chatColors = chatcolors;
    }

    private List<OutboundPacket> outgoingPackets = new ArrayList<>();

    public List<OutboundPacket> getOutgoingPackets() {
        return new ArrayList<>(outgoingPackets);
    }

    public void sendPacket(OutboundPacket packet) {
        this.outgoingPackets.add(packet);
    }

    public void cancelPacket(OutboundPacket packet) {
        outgoingPackets.remove(packet);
    }

    public Player(String name, UUID uuid, Channel connection, int protocolversion, Location location) {
        super(name, uuid);
        this.connection = connection;
        this.protocolVersion = protocolversion;
        this.world = location.getWorld();
        playerEntity = new Entity(world.getUnusedEID(), location, EntityType.PLAYER);
    }

    public void addPlayerToPlayerList(OfflinePlayer player) {
        GameProfile profile = MojangAPI.getGameProfile(player.getUuid());
        OutboundPacket packet = new OutboundPacket(PacketType.PLAYER_INFO, new VarInt(0), new VarInt(1), player.getUuid(), player.getName(),
                new VarInt(0),/* new VarInt(1), profile.getName(),profile.getValue(),profile.isSigned(),*/
                new VarInt(0), new VarInt(0), true, TextMessage.text(player.getName()));
        sendPacket(packet);
    }

    public Channel getConnection() {
        return connection;
    }

    public void clearPackets() {
        outgoingPackets.clear();
    }

    /**
     * DOES NOT ACTUALLY CHANGE THE PLAYER'S SETTINGS
     *
     * @param locale
     */
    @Deprecated
    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getName() {
        return displayname;
    }

    public void setName(String name) {
        this.displayname = name;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public void openInventory(Inventory newinv) {
        if (viewedInventory != null) {
            viewedInventory.removeViewer(this);
        }
        viewedInventory = newinv;
        viewedInventory.addViewer(this);
        OutboundPacket open = new OutboundPacket(PacketType.OPEN_WINDOW, new VarInt(viewedInventory.getWindowID()), new VarInt(viewedInventory.getType().getId()), TextMessage.text("inv"), 0);
        this.sendPacket(open);
    }

    public Inventory getViewedInventory() {
        return viewedInventory;
    }

    public void setHotbarSlot(int i) {
        heldSlot = i;
        OutboundPacket packet = new OutboundPacket(PacketType.HELD_ITEM_CHANGE, (byte) i);
        sendPacket(packet);
    }

    public void updateHeldItemSlot(int slot) {
        this.heldSlot = slot;
    }

    public int getHeldItemSlot() {
        return heldSlot;
    }

    public void setXP(float barLevel, int level, int totalXP) {
        this.xp = xp;
        OutboundPacket packet = new OutboundPacket(PacketType.SET_EXPERIENCE, barLevel, new VarInt(level), new VarInt(totalXP));
        sendPacket(packet);
    }

    public void setHealth(float health) {
        this.health = health;
        updateHealth();
    }

    public float getHealth() {return health;}

    public void setFood(int food) {
        this.food = food;
        updateHealth();
    }

    public int getFood() {
        return food;
    }

    public int getXp() {
        return xp;
    }

    public void updateHealth() {
        OutboundPacket packet = new OutboundPacket(PacketType.UPDATE_HEALTH, health, new VarInt(food), foodSaturation);
        sendPacket(packet);
    }

    public void sendMessage(String s) {
        sendPacket(new OutboundPacket(PacketType.CHAT_CLIENTBOUND, TextMessage.text(s), (byte) 0));

    }

    @Override
    public void issueCommand(String command) {
        //TODO: Issue Command
    }

    @Override
    public void chat(String message) {
        PlayerChatEvent chatEvent = new PlayerChatEvent(this, message, TextMessage.chat(getName() + ": " + message).asBuilder());
        if (!EventBus.INSTANCE.post(chatEvent).isCancelled()) {
            Omega.broadcastJSONMessage(chatEvent.getJson().build().getAsJson());
        }
    }

    public void playSound(Sound sound, SoundCategory catagory, Location location, float volume, float pitch) {
        OutboundPacket packet = new OutboundPacket(PacketType.SOUND_EFFECT, new VarInt(sound.getId()), new VarInt(catagory.getId()), location.getBlockX() * 8, location.getBlockY() * 8, location.getBlockZ() * 8, volume, pitch);
        sendPacket(packet);
    }

    public void sendTitle(String title, String subtitle, int fadein, int stay, int fadeout) {
        OutboundPacket settile = new OutboundPacket(PacketType.TITLE, new VarInt(0), TextMessage.text(title));
        OutboundPacket setsubtitle = new OutboundPacket(PacketType.TITLE, new VarInt(1), TextMessage.text(title));
        OutboundPacket settimesanddisplay = new OutboundPacket(PacketType.TITLE, new VarInt(2), fadein, stay, fadeout);
        sendPacket(settile);
        sendPacket(setsubtitle);
        //sendPacket(settimesanddisplay);
    }

    public void teleport(Location location){
        OutboundPacket positionAndLook = new OutboundPacket(PacketType.PLAYER_POSITION_AND_LOOK, location.getX(),location.getY(),location.getZ(),location.getYaw(),location.getPitch(),(byte)0, new VarInt(1));
        sendPacket(positionAndLook);
        playerEntity.teleport(location);
    }

    public World getWorld() {
        return world;
    }

    public int getEntityID() {
        return playerEntity.getEntityID();
    }

    public Entity getEntity() {
        return playerEntity;
    }

    public boolean allowFlight(){
        return allowflight;
    }
    public Location getBedLocation(){
        return bedlocation;
    }
    public int getClientViewDistance(){
        return viewdistance;
    }
}
