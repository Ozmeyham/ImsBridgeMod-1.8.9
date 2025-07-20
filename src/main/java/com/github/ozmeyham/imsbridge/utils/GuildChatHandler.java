package com.github.ozmeyham.imsbridge.utils;

import com.github.ozmeyham.imsbridge.ImsWebSocketClient;
import com.google.gson.JsonObject;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class GuildChatHandler {
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String raw = event.message.getUnformattedText();
        // only forward guild chat
        if (raw.contains("§2Guild >")) {
            // make sure we're connected
            if (ImsWebSocketClient.wsClient != null && ImsWebSocketClient.wsClient.isOpen()) {
                JsonObject payload = new JsonObject();
                payload.addProperty("from", "mc");
                payload.addProperty("msg", raw);
                ImsWebSocketClient.wsClient.send(payload.toString());
                // printToChat("§aIMS-Bridge Mod > §r§7Sent to bridge: " + raw);
            } else {
                printToChat("§aIMS-Bridge Mod > §r§cNot connected to WebSocket, dropping guild message");
            }
        }
    }
}
