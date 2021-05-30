package com.projectomega.main.packets.types;

import com.projectomega.main.Main;
import com.projectomega.main.debugging.DebuggingUtil;
import com.projectomega.main.game.Core;
import com.projectomega.main.game.Player;
import com.projectomega.main.game.chat.JsonAction;
import com.projectomega.main.game.chat.JsonChatBuilder;
import com.projectomega.main.game.chat.JsonChatElement;
import com.projectomega.main.game.events.EventManager;
import com.projectomega.main.game.events.types.PlayerChatEvent;
import com.projectomega.main.game.events.types.PlayerSendCommandEvent;
import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketHandler;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.PacketUtil;
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
        Player player = Core.getPlayerByChannel(ctx.channel());
        if (message.startsWith("/")) {
            PlayerSendCommandEvent chatEvent = new PlayerSendCommandEvent(player, message);
            EventManager.call(chatEvent);
            if (!chatEvent.isCanceled()) {
                //TODO: Issue Command
            }

        } else {
            JsonChatBuilder json = new JsonChatBuilder().add(new JsonChatElement(player.getName())).add(new JsonChatElement(": " + message));
            PlayerChatEvent chatEvent = new PlayerChatEvent(player, message, json);
            EventManager.call(chatEvent);
            if (!chatEvent.isCanceled()) {
                // String jsonTest = "{\"translate\":\"chat.type.text\",\"with\":[{\"text\":\"Herobrine\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg Herobrine \"},\"hoverEvent\":{\"action\":\"show_entity\",\"value\":\"{id:f84c6a79-0a4e-45e0-879b-cd49ebd4c4e2,name:Herobrine}\"},\"insertion\":\"Herobrine\"},{\"text\":\"I don't exist\"}]}";

                //OutboundPacket responseChat = new OutboundPacket(PacketType.CHAT_CLIENTBOUND,new Object[]{json.build(),(byte)0});
                //player.sendPacket(responseChat);
                Core.broadcastMessage(json.build());
            }
        }
    }
}
