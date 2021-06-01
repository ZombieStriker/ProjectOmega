package com.projectomega.main.game;

import com.projectomega.main.game.chat.*;
import com.projectomega.main.game.packetlogic.PacketLogicManager;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.PacketUtil;
import com.projectomega.main.plugins.PluginManager;
import io.netty.channel.Channel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class Omega extends Thread {

    private static final Logger LOGGER = Logger.getLogger(Omega.class.getName());

    private static ConsoleSender consoleSender = new ConsoleSender();

    public static List<Player> players = new ArrayList<>();
    public static List<World> worlds = new ArrayList<>();

    private static Omega instance;
    private static boolean init = false;
    private BufferedImage icon;

    public static ConsoleSender getConsoleSender() {
        return consoleSender;
    }

    protected static Omega getInstance() {
        if (instance == null) {
            instance = new Omega();
        }
        return instance;
    }

    private Omega() {
        instance = this;
        createWorld("world");
        createWorld("world_nether");
        createWorld("world_the_end");
    }

    public static BossBar createBossBar(String title, float health, BossBarColor color, BossBarDivisions divisions) {
        BossBar bossBar = new BossBar(UUID.randomUUID(), title, health, color, divisions, false, false, false);
        return bossBar;
    }


    public static World getWorld(String name) {
        for (World world : worlds) {
            if (world.getName().equals(name))
                return world;
        }
        return null;
    }

    public static World createWorld(String name) {
        World world = new World(name);
        worlds.add(world);
        return world;
    }

    public static List<World> getWorlds() {
        return new ArrayList<World>(worlds);
    }

    @Deprecated
    public static void init() {
        if (!init) {
            getInstance().start();

            File servericon = new File(getServerDirectory(), "server-icon.png");
            if (servericon.exists()) {
                try {
                    setServerIcon(ImageIO.read(servericon));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            init = true;
        } else {
            broadcastMessage("Some plugin tried to call init twice!");
        }
    }

    private static void broadcastMessage(String s) {
        for (Player player : players) {
            if (player.getChatmode() == 0) {
                player.sendPacket(new OutboundPacket(PacketType.CHAT_CLIENTBOUND, new Object[]{new JsonChatBuilder().setTranslate(JsonChatBuilder.TEXT).add(new JsonChatElement(s)).build(), (byte) 0}));
            }
        }
    }

    public static void broadcastJSONMessage(String s) {
        for (Player player : players) {
            if (player.getChatmode() == 0) {
                player.sendPacket(new OutboundPacket(PacketType.CHAT_CLIENTBOUND, new Object[]{s, (byte) 0}));
            }
        }
    }

    public static BufferedImage getServerIcon() {
        return getInstance().icon;
    }

    public static void setServerIcon(BufferedImage bi) {
        getInstance().icon = bi;
    }

    public static File getJarFile() {
        File file = null;
        try {
            URI uri = Omega.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            if (uri != null) ;
            file = new File(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File getServerDirectory() {
        return getJarFile().getParentFile();
    }

    public static List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public void run() {
        System.out.println("Starting Server...");
        PacketLogicManager.init();
        PluginManager.init();
        PluginManager.enableAllPlugins();
        while (true) {
            long start = System.currentTimeMillis();
            for (Player player : new ArrayList<>(players)) {
                if (!player.getConnection().isActive() || !player.getConnection().isOpen()) {
                    players.remove(player);
                    continue;
                }
                player.sendPacket(new OutboundPacket(PacketType.KEEP_ALIVE_CLIENTBOUND_OLD, new Object[]{start}));
                for (OutboundPacket packet : player.getOutgoingPackets()) {
                    try {
                        PacketUtil.writePacketToOutputStream(player.getConnection(), packet);
                    } catch (Error | Exception e43) {
                        e43.printStackTrace();
                    }
                }
                player.clearPackets();
            }
            try {
                if ((1000 / 20) - (System.currentTimeMillis() - start) > 0)
                    Thread.sleep((1000 / 20) - (System.currentTimeMillis() - start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addPlayerConnection(Player player) {
        players.add(player);
    }

    public static Player getPlayerByChannel(Channel channel) {
        for (Player player : players) {
            if (player.getConnection().equals(channel))
                return player;
        }
        return null;
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
