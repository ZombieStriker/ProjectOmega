package com.projectomega.main.game.packetlogic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.projectomega.main.events.EventBus;
import com.projectomega.main.events.types.PingServerEvent;
import com.projectomega.main.game.OfflinePlayer;
import com.projectomega.main.game.Omega;
import com.projectomega.main.packets.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class SendMOTDPacketLogic implements PacketListener {

    @Override
    public void onCall(InboundPacket packet) {
        System.out.println(packet.getChannel() + "|" + packet.getData(0) + "|" + packet.getData(1) + "|" + packet.getData(2)+" | "+packet.getData(3));
        if (((int) packet.getData(3)) == 1) {
            PingServerEvent event = new PingServerEvent(packet.getChannel(), "DEFAULT MESSAGE", 754, Omega.getServerIcon());
            EventBus.INSTANCE.post(event);
            if (event.isCancelled())
                return;
            JsonObject object = new JsonObject();
            JsonObject version = new JsonObject();
            version.addProperty("name", "ProjectOmega V0.1");
            version.addProperty("protocol", event.getVersionID());
            object.add("version", version);
            JsonObject players = new JsonObject();
            players.addProperty("max", event.getMaxPlayerCount());
            players.addProperty("online", event.getPlayerCount());
            JsonArray array = new JsonArray();
            for (OfflinePlayer player : event.getPlayers()) {
                JsonObject playerJson = new JsonObject();
                playerJson.addProperty("name", player.getName());
                playerJson.addProperty("id", player.getUuid().toString());
                array.add(playerJson);
            }
            object.add("players", players);
            JsonObject description = new JsonObject();
            description.addProperty("text", event.getMOTD());
            object.add("description", description);

            if (event.getIcon() != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    ImageIO.write(event.getIcon(), "png", bos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] data = bos.toByteArray();
                object.addProperty("favicon", "data:image/png;base64," +
                        Base64.getEncoder().encodeToString(data));
            }
            OutboundPacket outboundPacket = new OutboundPacket(PacketType.STATUS_PING, object.toString());
            PacketUtil.writePacketToOutputStream(packet.getChannel(), outboundPacket);
        }
    }
}
