package com.github.ozmeyham.imsbridge;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import com.google.gson.JsonArray;
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

import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.bridgeKey;
import static com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils.isValidBridgeKey;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class ImsWebSocketClient extends WebSocketClient {

    public static ImsWebSocketClient wsClient;
    public static boolean clientOnline = false;

    public ImsWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public static void connectWebSocket() {
        if (wsClient == null || !wsClient.isOpen()) {
            // printToChat("§cConnecting to websocket…");
            try {
                wsClient = new ImsWebSocketClient(new URI("wss://ims-bridge.com"));
                wsClient.setConnectionLostTimeout(0);
                wsClient.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                printToChat("Invalid WebSocket URI: " + e.getMessage());
            }
        }
    }

    public static void disconnectWebSocket() {
        if (wsClient != null && wsClient.isOpen()) {
            try {
                wsClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onOpen(ServerHandshake handshakedata) {
        // printToChat("§2Successfully connected to websocket.");
        if (bridgeKey != null && !bridgeKey.isEmpty()) {
            JsonObject auth = new JsonObject();
            auth.addProperty("from", "mc");
            auth.addProperty("key", bridgeKey);
            wsClient.send(auth.toString());
            // printToChat("§aIMS-Bridge Mod > §r§7Sent auth JSON: " + auth.toString());
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
        // handle server command response
        JsonElement resp = obj.get("response");
        if (resp != null && !resp.isJsonNull()) {
            handleResponse(message);
            return;
        }

        // handle regular messages
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
        // look up guild colour
        String guildColour = GUILD_COLOUR_MAP.get(guildKey);
        if (combined) {
            if ("discord".equals(from)) {
                guild = "DISC";
                guildColour = "§9";
            }
            cbridgeMessage(text, user, guild, guildColour);
        } else {
            bridgeMessage(text, user, guild);
        }
    }

    private void handleResponse(String message) {
        JsonObject root = new JsonParser().parse(message).getAsJsonObject();
        String request = root.has("request") ? root.get("request").getAsString() : "";

        if ("getOnlinePlayers".equals(request)) {
            JsonObject response = root.getAsJsonObject("response");

            int totalPlayers = 0;
            for (java.util.Map.Entry<String, JsonElement> entry : response.entrySet()) {
                totalPlayers += entry.getValue().getAsJsonArray().size();
            }
            StringBuilder messageBuilder = new StringBuilder("§aOnline Players: §e" + totalPlayers + "\n");

            for (java.util.Map.Entry<String, JsonElement> entry : response.entrySet()) {
                String guild = entry.getKey();
                JsonArray players = entry.getValue().getAsJsonArray();
                int count = players.size();

                messageBuilder.append(GUILD_COLOUR_MAP.get(guild)).append(guild).append("§7: §e").append(count).append("\n");

                if (count == 0) {
                    messageBuilder.append("§7None\n");
                } else {
                    for (int i = 0; i < count; i++) {
                        messageBuilder.append("§f").append(players.get(i).getAsString());
                        if (i < count - 1) messageBuilder.append(", ");
                    }
                    messageBuilder.append("\n");
                }
            }
            printToChat(messageBuilder.toString());
        }
    }

    private void bridgeMessage(String chatMsg, String username, String guild) {
        String colouredMsg = bridgeC1 + "Guild > " + bridgeC2 + username + " §9[DISC]§f: " + bridgeC3 + chatMsg;
        if (bridgeEnabled) scheduleChat(colouredMsg);
    }

    private void cbridgeMessage(String chatMsg, String username, String guild, String guildColour) {
        String formattedMsg = cbridgeC1 + "CB > " + cbridgeC2 + username + guildColour + " [" + guild + "]§f: " + cbridgeC3 + chatMsg;
        if (combinedBridgeEnabled) scheduleChat(formattedMsg);
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


    public static final Map<String,String> GUILD_MAP;
    static {
        Map<String,String> m = new java.util.HashMap<>();
        m.put("Ironman Sweats","IMS");
        m.put("Ironman Casuals","IMC");
        m.put("Ironman Academy","IMA");
        GUILD_MAP = java.util.Collections.unmodifiableMap(m);
    }

    public static final Map<String,String> GUILD_COLOUR_MAP;
    static {
        Map<String,String> m = new java.util.HashMap<>();
        m.put("Ironman Sweats","§a");
        m.put("Ironman Casuals","§3");
        m.put("Ironman Academy","§2");
        GUILD_COLOUR_MAP = java.util.Collections.unmodifiableMap(m);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if ("Invalid bridge key".equals(reason)) {
            printToChat("§4Disconnected from websocket: §cfailed to authenticate bridge key. §7Use §6/bridge key <key>§7 to try again.");
            return;
        }
        if (isValidBridgeKey() && clientOnline) {
            tryReconnecting();
        }
    }

    @Override
    public void onError(Exception ex) {
        // bubble up errors from onMessage (like JsonNull) into chat
        String err = ex.getClass().getSimpleName() + ": " + ex.getMessage();
        printToChat("§4WebSocket Error – " + err);
        try { closeBlocking(); } catch (InterruptedException ignore) { Thread.currentThread().interrupt(); }
    }

    private void tryReconnecting() {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                wsClient.reconnect();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
