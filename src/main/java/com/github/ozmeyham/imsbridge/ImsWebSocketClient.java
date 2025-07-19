package com.github.ozmeyham.imsbridge;
import java.util.logging.Logger;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import net.minecraftforge.fml.client.FMLClientHandler;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// import static com.mojang.text2speech.Narrator.LOGGER;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.quote;
import static com.github.ozmeyham.imsbridge.IMSBridge.*;
import static com.github.ozmeyham.imsbridge.commands.BridgeColourCommand.*;
import static com.github.ozmeyham.imsbridge.commands.CombinedBridgeColourCommand.*;
import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.bridgeKey;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class ImsWebSocketClient extends WebSocketClient {

    public static ImsWebSocketClient wsClient;

    public ImsWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public static void connectWebSocket() {
        if (wsClient == null || !wsClient.isOpen()) {
            printToChat("§cConnecting to websocket...");
            try {
                wsClient = new ImsWebSocketClient(new URI("wss://ims-bridge.com"));
                wsClient.connect();
            } catch (URISyntaxException e) {
            printToChat("Invalid Websocket URI");    // LOGGER.error("Invalid WebSocket URI", e);
            }
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // LOGGER.info("WebSocket Connected");
        printToChat("§2Successfully connected to websocket.");
        // Send bridgeKey immediately after connecting
        if (bridgeKey != null) {
            wsClient.send("{\"from\":\"mc\",\"key\":" + quote(bridgeKey) + "}");
        }
    }
    private void bridgeMessage(String chatMsg, String username, String guild) {
        String colouredMsg = bridgeC1 + (guild == null ? "Bridge" : guild) + " > " + bridgeC2 + username + ": " + bridgeC3 + chatMsg;
        // Send formatted message in client chat
        if (bridgeEnabled == true) {
            FMLClientHandler.instance().getClient().addScheduledTask(() -> {
                IChatComponent component = new ChatComponentText(colouredMsg);
                FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(component);
            });
        }
    }

    private void cbridgeMessage(String chatMsg, String username, String guild) {
        String colouredMsg = cbridgeC1 + "CBridge > " + cbridgeC2 + username + ": " + cbridgeC3 + chatMsg;
        // Send formatted message in client chat
        if (combinedBridgeEnabled == true) {
            FMLClientHandler.instance().getClient().addScheduledTask(() -> {
                IChatComponent component = new ChatComponentText(colouredMsg);
                FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(component);
            });
        }
    }

    @Override
    public void onMessage(String message) {
        //printToChat(message);
        String msg = getJsonValue(message, "msg");
        String[] split = msg.split(": ", 2);
        String username = split.length > 0 ? split[0] : "";
        String chatMsg = split.length > 1 ? split[1] : "";
        String guild = GUILD_MAP.get(getJsonValue(message, "guild"));

        if (message.contains("\"combinedbridge\":true")){
            cbridgeMessage(chatMsg, username, guild);
        } else if (message.contains("\"from\":\"discord\"")){
            bridgeMessage(chatMsg, username, guild);
        }
    }

    public static String getJsonValue(String jsonString, String key) {
        try {
            JsonParser parser = new JsonParser();
            JsonElement root = parser.parse(jsonString);
            if (!root.isJsonObject()) {
                return null;
            }
            JsonObject obj = root.getAsJsonObject();
            JsonElement el = obj.get(key);
            return (el != null && !el.isJsonNull()) ? el.getAsString() : null;
        } catch (Exception e) {
            printToChat("Error parsing JSON for key"); // IMSBridge.LOGGER.error("Error parsing JSON for key '" + key + "'", e);
            return null;
        }
    }

    public static final Map<String, String> GUILD_MAP;
    static {
        Map<String, String> map = new java.util.HashMap<>();
        map.put("Ironman Sweats", "IMS");
        map.put("Ironman Casuals", "IMC");
        map.put("Ironman Academy", "IMA");
        GUILD_MAP = java.util.Collections.unmodifiableMap(map);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        printToChat("Websocket closed"); // LOGGER.info("WebSocket Closed: {}", reason);

        if ("Invalid bridge key".equals(reason)) {
            printToChat("§4Disconnected from websocket: failed to authenticate bridge key. §6Use /bridgekey {key} to try again.");
            // LOGGER.warn("Not reconnecting due to invalid key.");
            return; // Don't attempt to reconnect
        }
        if (bridgeKey != null) {
            tryReconnecting();
        }
    }

    @Override
    public void onError(Exception ex) {
        printToChat("Websocket Error");// LOGGER.error("WebSocket Error", ex);
    }

    private void tryReconnecting() {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                // LOGGER.info("Attempting to reconnect...");
                printToChat("§4Disconnected from websocket. §6Attempting to reconnect...");
                this.reconnect();
            } catch (InterruptedException e) {
                printToChat("reconnect interrupted");// LOGGER.error("Reconnect interrupted", e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}