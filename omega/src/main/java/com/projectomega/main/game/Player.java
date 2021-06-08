package com.projectomega.main.game;

import com.projectomega.main.command.CommandException;
import com.projectomega.main.command.permission.Permission;
import com.projectomega.main.command.permission.PermissionState;
import com.projectomega.main.command.permission.PermissionStore;
import com.projectomega.main.events.EventBus;
import com.projectomega.main.events.types.PlayerChatEvent;
import com.projectomega.main.events.types.PlayerKickEvent;
import com.projectomega.main.game.chat.TextMessage;
import com.projectomega.main.game.entity.Entity;
import com.projectomega.main.game.entity.EntityType;
import com.projectomega.main.game.inventory.Inventory;
import com.projectomega.main.game.inventory.PlayerInventory;
import com.projectomega.main.game.player.GameProfile;
import com.projectomega.main.game.sound.Sound;
import com.projectomega.main.game.sound.SoundCategory;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.PacketUtil;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.packets.types.*;
import com.projectomega.main.utils.MojangAPI;
import io.netty.channel.Channel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player extends OfflinePlayer implements CommandSender {

    private final PermissionStore permissionStore = new PermissionStore();

    private Channel connection;

    private Entity playerEntity;

    private int protocolVersion;

    private String locale = "en_GB";
    private int renderDistance = 10;
    private int chatmode = 0;
    private boolean chatColors = true;
    private byte displayedSkinParts = 127;
    private int mainHand = 1;
    private String displayname;

    private int xp = 0;
    private float health = 20.0f;
    private int food = 20;
    private float foodSaturation = 0.0f;

    private List<OutboundPacket> outgoingPackets = new ArrayList<>();

    private Inventory viewedInventory = null;
    private Inventory playerInventory = new PlayerInventory(this);
    private int heldSlot = 0;
    private World world;
    private int viewdistance = -1;
    private boolean allowflight = false;
    private Location bedlocation;
    private boolean sneaking = false;
    private boolean sprinting = false;
    private boolean connected = true;

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
        this.displayname = name;
        this.connection = connection;
        this.protocolVersion = protocolversion;
        this.world = location.getWorld();
        playerEntity = new Entity(world.getUnusedEID(), location, EntityType.PLAYER);
    }

    public void addPlayerToPlayerList(OfflinePlayer player) {
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
        this.sendPacket(new PacketOpenWindow(this, viewedInventory));
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
        sendPacket(new PacketSetExperience(this, barLevel, level, totalXP));
    }

    public void setHealth(float health) {
        this.health = health;
        this.sendPacket(new PacketUpdateHealth(this));
    }

    public float getHealth() {
        return health;
    }

    public void setFood(int food) {
        this.food = food;
        this.sendPacket(new PacketUpdateHealth(this));
    }

    public int getFood() {
        return food;
    }

    public int getXp() {
        return xp;
    }

    public void sendMessage(String s) {
        sendPacket(new PacketChatClientBound(this, s, 0, getUuid()));
    }

    @Override
    public void issueCommand(String command) {
        try {
            Omega.getCommandHandler().execute(this, command.substring(1));
        } catch (CommandException e) {
            sendMessage(e.getMessage());
        }
    }

    @Override
    public void chat(String message) {
        PlayerChatEvent chatEvent = new PlayerChatEvent(this, message, TextMessage.chat(getName() + ": " + message).asBuilder());
        if (!EventBus.INSTANCE.post(chatEvent).isCancelled()) {
            Omega.broadcastJSONMessage(chatEvent.getJson().build().getAsJson());
        }
    }

    public void playSound(Sound sound, SoundCategory category, Location location, float volume, float pitch) {
        sendPacket(new PacketSoundEffect(this,sound,category,location,volume,pitch));
    }

    public void sendTitle(String title, String subtitle, int fadein, int stay, int fadeout) {
        if (protocolVersion <= 760) {
            OutboundPacket settile = new OutboundPacket(PacketType.TITLE, protocolVersion, new VarInt(0), TextMessage.text(title));
            OutboundPacket setsubtitle = new OutboundPacket(PacketType.TITLE, protocolVersion, new VarInt(1), TextMessage.text(subtitle));
            OutboundPacket settimesanddisplay = new OutboundPacket(PacketType.TITLE, protocolVersion, new VarInt(2), fadein, stay, fadeout);
            sendPacket(settile);
            sendPacket(setsubtitle);
        } else {
            sendPacket(new PacketSetTitleText(this, title));
            sendPacket(new PacketSetTitleSubtitle(this, subtitle));
        }
    }

    public void teleport(Location location) {
       // OutboundPacket positionAndLook = new OutboundPacket(PacketType.PLAYER_POSITION_AND_LOOK, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), (byte) (0), new VarInt(1));
        playerEntity.teleport(location);
        sendPacket(new PacketPlayerPositionAndLook(this));
        //playerEntity.teleport(location);
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

    public boolean allowFlight() {
        return allowflight;
    }

    public Location getBedLocation() {
        return bedlocation;
    }

    public int getClientViewDistance() {
        return viewdistance;
    }

    private void setSprinting(boolean spriting) {
        this.sprinting = spriting;
    }

    public void setSpectatorTarget(Entity e) {
        //TODO: Spectator Target
    }

    public void kick(String message) {
        PlayerKickEvent kickEvent = new PlayerKickEvent(this, message, this.getName() + " has been kicked");
        EventBus.INSTANCE.post(kickEvent);
        if (!kickEvent.isCancelled()) {
            PacketUtil.writePacketToOutputStream(getConnection(), new PacketDisconnect(this,kickEvent.getReason()));
            Omega.removePlayer(this);
        }
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    public void sendResourcepack(String url) {
        //TODO Send resourcepack
    }

    public void setPlayerTime(long time, boolean relative) {
        //TODO:
    }

    public void setPlayerListName(String name) {

    }

    public void setPlayerListHeaderFooter(String header, String footer) {

    }

    public void setHealthScale(double scale) {

    }

    public void setFlySpeed(float speed) {

    }

    public void setFlying(boolean flying) {

    }

    public void setDisplayName(String name) {

    }

    public void setCompassLocation(Location location) {

    }

    public void setBedSpawnLocation(Location location) {

    }

    public void setBedSpawnlocation(Location location, boolean force) {

    }

    public void setAllowFlight(boolean allowFlight) {
        this.allowflight = allowFlight;
    }

    public void sendSignChange(Location location, String[] lines) {

    }

    public void sendSignChange(Location location, String[] lines, DyeColor dyecolor) {

    }

    public void sendRawMessage(String raw) {

    }

    public void sendExperienceLevelChange(int level) {

    }

    public void sendBlockChange(Location location, Material material) {

    }

    public void sendBlockDamage(Location location, float progress) {

    }

    public boolean isSprinting() {
        return sprinting;
    }

    public boolean isSneaking() {
        return sneaking;
    }

    public Location getLocation() {
        return playerEntity.getLocation();
    }

    @Override
    public boolean isOp() {
        return permissionStore.isOp();
    }

    @Override
    public void setOp(boolean op) {
        permissionStore.setOp(op);
    }

    @Override
    public void allowPermission(@NotNull String permission) {
        permissionStore.allowPermission(permission);
    }

    @Override
    public void allowPermission(@NotNull Permission permission) {
        permissionStore.allowPermission(permission);
    }

    @Override
    public void denyPermission(@NotNull String permission) {
        permissionStore.denyPermission(permission);
    }

    @Override
    public void denyPermission(@NotNull Permission permission) {
        permissionStore.denyPermission(permission);
    }

    @Override
    public PermissionState getState(@NotNull Permission permission) {
        return permissionStore.getState(permission);
    }

    @Override
    public PermissionState getState(@NotNull String permission) {
        return permissionStore.getState(permission);
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission) {
        return permissionStore.hasPermission(permission);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return permissionStore.hasPermission(permission);
    }

    public boolean isKicked() {
        return connected;
    }

    public float getSaturation() {
        return foodSaturation;
    }
}
