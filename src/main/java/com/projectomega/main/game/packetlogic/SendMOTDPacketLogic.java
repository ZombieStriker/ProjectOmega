package com.projectomega.main.game.packetlogic;

import com.projectomega.main.packets.*;

public class SendMOTDPacketLogic extends PacketListener {

    @Override
    public void onCall(InboundPacket packet) {
        System.out.println(packet.getAddress()+"|"+packet.getData(0));
        OutboundPacket outboundPacket = new OutboundPacket(PacketType.STATUS_PING,new Object[]{"{\n" +
                "    \"version\": {\n" +
                "        \"name\": \"1.8.7\",\n" +
                "        \"protocol\": 47\n" +
                "    },\n" +
                "    \"players\": {\n" +
                "        \"max\": 100,\n" +
                "        \"online\": 5,\n" +
                "        \"sample\": [\n" +
                "            {\n" +
                "                \"name\": \"thinkofdeath\",\n" +
                "                \"id\": \"4566e69f-c907-48ee-8d71-d7ba5aa00d20\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"description\": {\n" +
                "        \"text\": \"Hello world\"\n" +
                "    }\n" +
                "}"});
        PacketUtil.writePacketToOutputStream(packet.getAddress(),outboundPacket);
    }
}