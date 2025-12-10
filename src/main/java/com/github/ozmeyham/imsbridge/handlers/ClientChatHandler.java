package com.github.ozmeyham.imsbridge.handlers;

import com.github.ozmeyham.imsbridge.ImsWebSocketClient;
import com.google.gson.JsonObject;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import static com.github.ozmeyham.imsbridge.IMSBridge.*;
import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.bridgeKey;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.*;

public class ClientChatHandler {
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String content = event.message.getUnformattedText();

        // reads chat for guild messages and hypixel channel change events
        if (content.contains("§2Guild >")) {
            // send message to websocket
            if (ImsWebSocketClient.wsClient != null && ImsWebSocketClient.wsClient.isOpen() && bridgeKey != null) {
                JsonObject payload = new JsonObject();
                payload.addProperty("from", "mc");
                payload.addProperty("msg", content);
                ImsWebSocketClient.wsClient.send(payload.toString());
                // printToChat("§aIMS-Bridge Mod > §r§7Sent to bridge: " + content);
            }
        } else if (isSkyblockChannelChange(content) && combinedBridgeChatEnabled) {
            combinedBridgeChatEnabled = false;
            saveConfigValue("combinedBridgeChatEnabled", "false");
            printToChat("§cExited cbridge chat!");
        } else if (content.contains("Disabled guild chat!")) {
            if (combinedBridgeEnabled) {
                combinedBridgeEnabled = false;
                saveConfigValue("combinedBridgeEnabled", "false");
                printToChat("§cDisabled combined bridge messages!");
            }
            if (bridgeEnabled) {
                bridgeEnabled = false;
                saveConfigValue("bridgeEnabled", "false");
                printToChat("§cDisabled bridge messages!");
            }
        } else if (content.contains("Enabled guild chat!")) {
            if (!combinedBridgeEnabled) {
                combinedBridgeEnabled = true;
                saveConfigValue("combinedBridgeEnabled", "true");
                printToChat("§aEnabled combined bridge messages!");
            }
            if (!bridgeEnabled) {
                bridgeEnabled = true;
                saveConfigValue("bridgeEnabled", "true");
                printToChat("§aEnabled bridge messages!");
            }
        }
    }
}
