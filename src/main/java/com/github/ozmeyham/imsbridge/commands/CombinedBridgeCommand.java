package com.github.ozmeyham.imsbridge.commands;

import com.github.ozmeyham.imsbridge.IMSBridge;
import com.github.ozmeyham.imsbridge.ImsWebSocketClient;
import com.github.ozmeyham.imsbridge.utils.ConfigUtils;
import com.github.ozmeyham.imsbridge.utils.TextUtils;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.*;

public class CombinedBridgeCommand extends CommandBase {
    private static final Map<String, String> COLOR_CODE_MAP = BridgeCommand.COLOR_CODE_MAP;

    @Override
    public String getCommandName() {
        return "cbridge";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("cbt", "cbm", "cbc");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbridge <toggle|color|message|chat>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            TextUtils.printToChat("§cUsage: /cbridge <toggle|color|message|chat>");
            return;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "toggle":
                IMSBridge.combinedBridgeEnabled = !IMSBridge.combinedBridgeEnabled;
                ConfigUtils.saveConfigValue("combinedBridgeEnabled",
                        String.valueOf(IMSBridge.combinedBridgeEnabled));
                TextUtils.printToChat(
                        IMSBridge.combinedBridgeEnabled
                                ? "§aEnabled combined bridge!"
                                : "§cDisabled combined bridge!"
                );
                break;

            case "color":
                if (args.length < 4) {
                    TextUtils.printToChat("§cUsage: /cbridge color <Color1> <Color2> <Color3>");
                } else {
                    String c1 = args[1].toLowerCase(Locale.ROOT);
                    String c2 = args[2].toLowerCase(Locale.ROOT);
                    String c3 = args[3].toLowerCase(Locale.ROOT);
                    if (!COLOR_CODE_MAP.containsKey(c1)
                            || !COLOR_CODE_MAP.containsKey(c2)
                            || !COLOR_CODE_MAP.containsKey(c3)) {
                        TextUtils.printToChat("§cInvalid color! Valid options: " + COLOR_CODE_MAP.keySet());
                    } else {
                        IMSBridge.cbridgeC1 = COLOR_CODE_MAP.get(c1);
                        IMSBridge.cbridgeC2 = COLOR_CODE_MAP.get(c2);
                        IMSBridge.cbridgeC3 = COLOR_CODE_MAP.get(c3);
                        ConfigUtils.saveConfigValue("combinedBridgeC1", IMSBridge.cbridgeC1);
                        ConfigUtils.saveConfigValue("combinedBridgeC2", IMSBridge.cbridgeC2);
                        ConfigUtils.saveConfigValue("combinedBridgeC3", IMSBridge.cbridgeC3);
                        TextUtils.printToChat(
                                "§aCombined bridge colors set to: §f" + c1 + " §f" + c2 + " §f" + c3
                        );
                    }
                }
                break;

            case "message":
                // ← updated to send JSON payload, check WS connection, print usage/errors
                if (args.length > 1) {
                    String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                    if (!IMSBridge.combinedBridgeEnabled) {
                        TextUtils.printToChat("§cYou have to enable combined bridge to use this command! §6§o/cbridge toggle");
                    } else {
                        if (ImsWebSocketClient.wsClient != null && ImsWebSocketClient.wsClient.isOpen()) {
                            JsonObject payload = new JsonObject();
                            payload.addProperty("from", "mc");
                            payload.addProperty("msg", message);
                            payload.addProperty("combinedbridge", true);
                            ImsWebSocketClient.wsClient.send(payload.toString());
                        } else {
                            TextUtils.printToChat("§cWebSocket client not connected. Please ensure IMSBridge is running and connected.");
                        }
                    }
                } else {
                    TextUtils.printToChat("§cUsage: " + getCommandUsage(sender));
                }
                break;

            case "chat":
                if (!IMSBridge.combinedBridgeEnabled) {
                    TextUtils.printToChat("§cEnable combined bridge first with /cbridge toggle");
                } else {
                    IMSBridge.combinedBridgeChatEnabled = !IMSBridge.combinedBridgeChatEnabled;
                    ConfigUtils.saveConfigValue("combinedBridgeChatEnabled",
                            String.valueOf(IMSBridge.combinedBridgeChatEnabled));
                    TextUtils.printToChat(
                            IMSBridge.combinedBridgeChatEnabled
                                    ? "§aEnabled combined bridge chat!"
                                    : "§cDisabled combined bridge chat!"
                    );
                }
                break;

            default:
                TextUtils.printToChat("§cUnknown subcommand! Try /cbridge toggle|color|message|chat");
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
