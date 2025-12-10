package com.github.ozmeyham.imsbridge.commands;

import com.github.ozmeyham.imsbridge.IMSBridge;
import com.github.ozmeyham.imsbridge.ImsWebSocketClient;
import com.github.ozmeyham.imsbridge.utils.ConfigUtils;
import com.github.ozmeyham.imsbridge.utils.TextUtils;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.ozmeyham.imsbridge.IMSBridge.combinedBridgeEnabled;
import static com.github.ozmeyham.imsbridge.ImsWebSocketClient.wsClient;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class CombinedBridgeMessageCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "cbc";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("bc");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbc <message>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0) {
            String message = String.join(" ", args);
            if (!combinedBridgeEnabled) {
                printToChat("§cYou need to enable combined bridge messages to use this command! §6§o/cbridge toggle");
            } else {
                if (wsClient != null && wsClient.isOpen()) { // Check if WebSocket client is connected
                    JsonObject payload = new JsonObject();
                    payload.addProperty("from", "mc");
                    payload.addProperty("msg", message);
                    payload.addProperty("combinedbridge", true);
                    ImsWebSocketClient.wsClient.send(payload.toString());
                } else {
                    printToChat("§cYou are not connected to the bridge websocket server!");
                }
            }
        } else {
            if (!combinedBridgeEnabled) {
                printToChat("§cYou need to enable combined bridge messages to use this command! §6§o/cbridge toggle");
            } else {
                IMSBridge.combinedBridgeChatEnabled = !IMSBridge.combinedBridgeChatEnabled;
                ConfigUtils.saveConfigValue("combinedBridgeChatEnabled",
                        String.valueOf(IMSBridge.combinedBridgeChatEnabled));
                TextUtils.printToChat(
                        IMSBridge.combinedBridgeChatEnabled
                                ? "§aEntered combined bridge chat!"
                                : "§cExited combined bridge chat!"
                );
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
