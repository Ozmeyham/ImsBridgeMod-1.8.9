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
// pull your colours from IMSBridge now:
import static com.github.ozmeyham.imsbridge.IMSBridge.bridgeC1;
import static com.github.ozmeyham.imsbridge.IMSBridge.bridgeC2;
import static com.github.ozmeyham.imsbridge.IMSBridge.bridgeC3;
import static com.github.ozmeyham.imsbridge.IMSBridge.cbridgeC1;
import static com.github.ozmeyham.imsbridge.IMSBridge.cbridgeC2;
import static com.github.ozmeyham.imsbridge.IMSBridge.cbridgeC3;

import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;
import com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils;

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
                // disable the built-in ping/timeout so Forge won't drop us
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
        String key = BridgeKeyUtils.bridgeKey;
        if (key != null && !key.isEmpty()) {
            JsonObject auth = new JsonObject();
            auth.addProperty("from", "mc");
            auth.addProperty("key", key);
            wsClient.send(auth.toString());
            printToChat("§aIMS-Bridge Mod > §r§7Sent auth JSON: " + auth.toString());
        }
    }

    @Override
    public void onMessage(String message) {
        JsonObject obj;
        try {
            obj = new JsonParser().parse(message).getAsJsonObject();
        } catch (Exception e) {
            printToChat("§4Received non-JSON data, ignoring: " + message);
            return;
        }

        // 1) Handle any "response" field from the server (e.g. command acks)
        JsonElement resp = obj.get("response");
        if (resp != null && !resp.isJsonNull()) {
            printToChat("§aIMS-Bridge Response > §r§7" + resp.getAsString());
            return;
        }

        // 2) Now we only care about actual chat messages
        JsonElement msgEl = obj.get("msg");
        if (msgEl == null || msgEl.isJsonNull()) return;
        String full = msgEl.getAsString();

        // do we have a combined-bridge flag?
        boolean combined = obj.has("combinedbridge")
                && !obj.get("combinedbridge").isJsonNull()
                && obj.get("combinedbridge").getAsBoolean();

        // was it sent from Discord?
        JsonElement fromEl = obj.get("from");
        String from = fromEl != null && !fromEl.isJsonNull()
                ? fromEl.getAsString() : null;

        // ignore anything not from discord or not a combined msg
        if (!"discord".equals(from) && !combined) return;

        // split out user/message
        String[] parts = full.split(": ", 2);
        String user = parts.length > 0 ? parts[0] : "";
        String text = parts.length > 1 ? parts[1] : "";

        // look up guild code
        JsonElement guildEl = obj.get("guild");
        String guildKey = guildEl != null && !guildEl.isJsonNull()
                ? guildEl.getAsString() : null;
        String guild = GUILD_MAP.get(guildKey);

        if (combined) {
            cbridgeMessage(text, user, guild);
        } else {
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
        if (BridgeKeyUtils.bridgeKey != null
                && !BridgeKeyUtils.bridgeKey.isEmpty()
                && !remote) {
            scheduleReconnect();
        }
    }

    @Override
    public void onError(Exception ex) {
        // bubble up errors from onMessage (like JsonNull) into chat
        String err = ex.getClass().getSimpleName() + ": " + ex.getMessage();
        printToChat("§4WebSocket Error – " + err);
        try { closeBlocking(); } catch (InterruptedException ignore) { Thread.currentThread().interrupt(); }
        if (BridgeKeyUtils.bridgeKey != null && !BridgeKeyUtils.bridgeKey.isEmpty()) {
            scheduleReconnect();
        }
    }

    private void bridgeMessage(String msg, String user, String guild) {
        String txt = bridgeC1
                + (guild == null ? "Bridge" : guild)
                + " > " + bridgeC2
                + user + ": " + bridgeC3 + msg;
        if (bridgeEnabled) scheduleChat(txt);
    }

    private void cbridgeMessage(String msg, String user, String guild) {
        String txt = cbridgeC1 + "CBridge > " + cbridgeC2
                + user + ": " + cbridgeC3 + msg;
        if (combinedBridgeEnabled) scheduleChat(txt);
    }

    private void scheduleChat(String text) {
        FMLClientHandler.instance()
                .getClient()
                .addScheduledTask(() -> {
                    IChatComponent comp = new ChatComponentText(text);
                    FMLClientHandler.instance()
                            .getClient()
                            .ingameGUI.getChatGUI()
                            .printChatMessage(comp);
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

    public static final Map<String,String> GUILD_MAP;
    static {
        Map<String,String> m = new java.util.HashMap<>();
        m.put("Ironman Sweats","IMS");
        m.put("Ironman Casuals","IMC");
        m.put("Ironman Academy","IMA");
        GUILD_MAP = java.util.Collections.unmodifiableMap(m);
    }
}
