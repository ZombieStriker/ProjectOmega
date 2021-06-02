package com.projectomega.main.game.packetlogic;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
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
        System.out.println(packet.getChannel() + "|" + packet.getData(0) + "|" + packet.getData(1) + "|" + packet.getData(2));
        if (((int) packet.getData(3)) == 1) {


            PingServerEvent event = new PingServerEvent(packet.getChannel(), "DEFAULT MESSAGE", 754, Omega.getServerIcon());
            EventBus.INSTANCE.post(event);
            if (event.isCancelled())
                return;

            JsonObject object = new JsonObject();
            JsonObject version = new JsonObject();
            version.put("name", "ProjectOmega V0.1");
            version.put("protocol", event.getVersionID());
            object.put("version", version);
            JsonObject players = new JsonObject();
            players.put("max", event.getMaxPlayerCount());
            players.put("online", event.getPlayerCount());
            JsonArray array = new JsonArray();
            for (OfflinePlayer player : event.getPlayers()) {
                JsonObject playerJson = new JsonObject();
                playerJson.put("name", player.getName());
                playerJson.put("id", player.getUuid().toString());
                array.add(playerJson);
            }
            object.put("players",players);
            JsonObject description = new JsonObject();
            description.put("text",event.getMOTD());
            object.put("description",description);

            if(event.getIcon()!=null){
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    ImageIO.write(event.getIcon(), "png", bos );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte [] data = bos.toByteArray();
                object.put("favicon","data:image/png;base64,"+
                        Base64.getEncoder().encodeToString(data));
            }
            OutboundPacket outboundPacket = new OutboundPacket(PacketType.STATUS_PING, new Object[]{object.toJson()});
            PacketUtil.writePacketToOutputStream(packet.getChannel(), outboundPacket);
        }
    }
}
