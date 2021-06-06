package com.projectomega.main.packets.types;

import com.projectomega.main.events.EventBus;
import com.projectomega.main.events.types.PlayerChatEvent;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.game.chat.TextMessage;
import com.projectomega.main.game.chat.TranslatedComponent;
import com.projectomega.main.packets.*;
import com.projectomega.main.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;

public class PacketInboundChat extends PacketHandler {

    public PacketInboundChat() {
        super(PacketType.CHAT_SERVERBOUND);
    }

    @Override
    public void call(ByteBuf bytebuf, int i, Channel ctx) {
        String message = ByteUtils.buildString(bytebuf);
        Player player = Omega.getPlayerByChannel(ctx);

        InboundPacket packet = new InboundPacket(PacketType.CHAT_SERVERBOUND, new Object[]{message}, ctx);
        List<PacketListener> packetlisteners = PacketManager.getListeners(PacketType.CHAT_SERVERBOUND);
        if (packetlisteners != null) {
            for (PacketListener listener : packetlisteners) {
                listener.onCall(packet);
            }
        }

        if (message.startsWith("/")) {
            player.issueCommand(message.substring(1));
        } else {
            TranslatedComponent.Builder json = TextMessage.chat(player.getName(), ": " + message).asBuilder();
            PlayerChatEvent chatEvent = new PlayerChatEvent(player, message, json);
            if (!EventBus.INSTANCE.post(chatEvent).isCancelled()) {
                Omega.broadcastJSONMessage(json.build().getAsJson());
            }
        }
    }
}
