package com.projectomega.main.packets.types;

import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.game.chat.JsonChatBuilder;
import com.projectomega.main.game.chat.JsonChatElement;
import com.projectomega.main.events.EventManager;
import com.projectomega.main.events.types.PlayerChatEvent;
import com.projectomega.main.events.types.PlayerSendCommandEvent;
import com.projectomega.main.packets.PacketHandler;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketInboundChat extends PacketHandler {

    public PacketInboundChat() {
        super(PacketType.CHAT_SERVERBOUND);
    }

    @Override
    public void call(ByteBuf bytebuf, int i, ChannelHandlerContext ctx) {
        String message = ByteUtils.buildString(bytebuf);
        Player player = Omega.getPlayerByChannel(ctx.channel());
        if (message.startsWith("/")) {
            PlayerSendCommandEvent chatEvent = new PlayerSendCommandEvent(player, message);
            EventManager.call(chatEvent);
            if (!chatEvent.isCanceled()) {
                //TODO: Issue Command
            }

        } else {
            JsonChatBuilder json = new JsonChatBuilder().setTranslate(JsonChatBuilder.CHAT_TYPE_TEXT).add(new JsonChatElement(player.getName())).add(new JsonChatElement(": " + message));
            PlayerChatEvent chatEvent = new PlayerChatEvent(player, message, json);
            EventManager.call(chatEvent);
            if (!chatEvent.isCanceled()) {
                Omega.broadcastMessage(json.build());
            }
        }
    }
}
