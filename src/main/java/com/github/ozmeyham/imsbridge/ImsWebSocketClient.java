package com.github.ozmeyham.imsbridge;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import com.github.ozmeyham.imsbridge.commands.BridgeColourCommand;
import com.github.ozmeyham.imsbridge.commands.CombinedBridgeColourCommand;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImsWebSocketClient extends WebSocketClient {
    public static ImsWebSocketClient wsClient; // Made public static for accessibility

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ImsWebSocketClient(URI serverUri) {
        super(serverUri);
        wsClient = this; // Set the static reference on creation
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§aConnected to IMSBridge WebSocket"));
    }

    @Override
    public void onMessage(String message) {
        try {
            JsonNode rootNode = objectMapper.readTree(message);
            String type = rootNode.path("type").asText();
            String chatMsg = rootNode.path("message").asText();
            String username = rootNode.path("username").asText();
            String guild = rootNode.has("guild") ? rootNode.path("guild").asText() : null;

            if ("bridge".equals(type)) {
                bridgeMessage(chatMsg, username, guild);
            } else if ("cbridge".equals(type)) {
                cbridgeMessage(chatMsg, username, guild);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§cDisconnected from IMSBridge WebSocket: " + reason));
    }

    @Override
    public void onError(Exception ex) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§4IMSBridge WebSocket error: " + ex.getMessage()));
    }

    private void bridgeMessage(String chatMsg, String username, String guild) {
        String colouredMsg = BridgeColourCommand.bridgeC1 + (guild == null ? "Bridge" : guild) + " > " +
                BridgeColourCommand.bridgeC2 + username + ": " +
                BridgeColourCommand.bridgeC3 + chatMsg;

        if (IMSBridge.bridgeEnabled) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(colouredMsg));
        }
    }

    private void cbridgeMessage(String chatMsg, String username, String guild) {
        String colouredMsg = CombinedBridgeColourCommand.cbridgeC1 + "CBridge > " +
                CombinedBridgeColourCommand.cbridgeC2 + username + ": " +
                CombinedBridgeColourCommand.cbridgeC3 + chatMsg;

        if (IMSBridge.combinedBridgeEnabled) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(colouredMsg));
        }
    }

    public static void connectWebSocket(String uri) {
        try {
            if (wsClient != null) {
                wsClient.close();
            }
            wsClient = new ImsWebSocketClient(new URI(uri));
            wsClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}