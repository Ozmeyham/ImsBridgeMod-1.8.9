package com.github.ozmeyham.imsbridge;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import static com.github.ozmeyham.imsbridge.IMSBridge.bridgeEnabled;
import static com.github.ozmeyham.imsbridge.IMSBridge.combinedBridgeEnabled;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;
import static com.github.ozmeyham.imsbridge.commands.BridgeColourCommand.*;
import static com.github.ozmeyham.imsbridge.commands.CombinedBridgeColourCommand.*;
import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.bridgeKey;

public class ImsWebSocketClient extends WebSocketClient {
    public static ImsWebSocketClient wsClient;

    public ImsWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public static void connectWebSocket() {
        if (wsClient == null || !wsClient.isOpen()) {
            printToChat("§cConnecting to websocket…");
            try {
                wsClient = new ImsWebSocketClient(new URI("ws://ims-bridge.com:3000"));
                // disable built-in ping timeout so Forge won’t drop us
                wsClient.setConnectionLostTimeout(0);
                wsClient.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                printToChat("Invalid WebSocket URI: " + e.getMessage());
            }
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        printToChat("§2Successfully connected to websocket.");
        if (bridgeKey != null && !bridgeKey.isEmpty()) {
            JsonObject auth = new JsonObject();
            auth.addProperty("from", "mc");
            auth.addProperty("key", bridgeKey);
            wsClient.send(auth.toString());
            printToChat("§aIMS-Bridge Mod > §r§7Sent auth JSON: " + auth.toString());
        }
    }

    @Override
    public void onMessage(String message) {
        JsonElement root;
        try {
            // older GSON on 1.8.9 doesn’t have JsonParser.parseString(...)
            root = new JsonParser().parse(message);
        } catch (Exception e) {
            printToChat("§4Received non-JSON data, ignoring: " + message);
            return;
        }
        if (!root.isJsonObject()) return;

        JsonObject obj = root.getAsJsonObject();
        if (obj.has("type")) {
            String type = obj.get("type").getAsString();
            String info = obj.has("message") ? obj.get("message").getAsString() : null;
            switch (type) {
                case "auth_success":
                    printToChat("§2WebSocket: " + (info != null ? info : "Authenticated"));
                    return;
                case "auth_error":
                case "error":
                    printToChat("§4WebSocket: " + (info != null ? info : "Error"));
                    return;
                default:
            }
        }

        if (!obj.has("msg")) return;
        String full = obj.get("msg").getAsString();
        String[] parts = full.split(": ", 2);
        String user = parts.length > 0 ? parts[0] : "";
        String text = parts.length > 1 ? parts[1] : "";
        String guildKey = obj.has("guild") ? obj.get("guild").getAsString() : null;
        String guild = GUILD_MAP.get(guildKey);

        boolean combined = obj.has("combinedbridge") && obj.get("combinedbridge").getAsBoolean();
        boolean fromDiscord = obj.has("from") && "discord".equals(obj.get("from").getAsString());

        if (combined) {
            cbridgeMessage(text, user, guild);
        } else if (fromDiscord) {
            bridgeMessage(text, user, guild);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        printToChat("§aIMS-Bridge Mod > §rWebsocket closed: " + reason);
        if ("Invalid bridge key".equals(reason)) {
            printToChat("§4Authentication failed. Use /bridgekey {key} to retry.");
            return;
        }
        if (bridgeKey != null && !bridgeKey.isEmpty() && !remote) {
            scheduleReconnect();
        }
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        String err = ex.getClass().getSimpleName() + ": " + ex.getMessage();
        printToChat("§4WebSocket Error – " + err);
        try { closeBlocking(); } catch (InterruptedException ignore) { Thread.currentThread().interrupt(); }
        if (bridgeKey != null && !bridgeKey.isEmpty()) scheduleReconnect();
    }

    private void bridgeMessage(String msg, String user, String guild) {
        String txt = bridgeC1 + (guild == null ? "Bridge" : guild)
                + " > " + bridgeC2 + user + ": " + bridgeC3 + msg;
        if (bridgeEnabled) scheduleChat(txt);
    }

    private void cbridgeMessage(String msg, String user, String guild) {
        String txt = cbridgeC1 + "CBridge > " + cbridgeC2
                + user + ": " + cbridgeC3 + msg;
        if (combinedBridgeEnabled) scheduleChat(txt);
    }

    private void scheduleChat(String text) {
        FMLClientHandler.instance().getClient().addScheduledTask(() -> {
            IChatComponent comp = new ChatComponentText(text);
            FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(comp);
        });
    }

    private void scheduleReconnect() {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                printToChat("§6Reconnecting to WebSocket…");
                wsClient.reconnect();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    public static final Map<String, String> GUILD_MAP;
    static {
        Map<String, String> m = new java.util.HashMap<>();
        m.put("Ironman Sweats", "IMS");
        m.put("Ironman Casuals", "IMC");
        m.put("Ironman Academy", "IMA");
        GUILD_MAP = java.util.Collections.unmodifiableMap(m);
    }
}
