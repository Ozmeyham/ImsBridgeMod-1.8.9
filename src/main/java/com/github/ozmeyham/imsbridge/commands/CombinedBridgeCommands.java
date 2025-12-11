package com.github.ozmeyham.imsbridge.commands;

import com.github.ozmeyham.imsbridge.IMSBridge;
import com.github.ozmeyham.imsbridge.ImsWebSocketClient;
import com.github.ozmeyham.imsbridge.utils.ConfigUtils;
import com.github.ozmeyham.imsbridge.utils.TextUtils;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.*;

import static com.github.ozmeyham.imsbridge.IMSBridge.combinedBridgeEnabled;
import static com.github.ozmeyham.imsbridge.ImsWebSocketClient.wsClient;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class CombinedBridgeCommands extends CommandBase {
    private static final Map<String, String> COLOR_CODE_MAP = BridgeCommands.COLOR_CODE_MAP;

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
        return "/cbridge <help|toggle|colour|cbc|chat|party>";
    }

    public static long lastParty = -1;
    public static int partySpotsLeft = -1;

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            TextUtils.printToChat("§cUsage: " + getCommandUsage(sender));
            return;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "help":
                printHelp();
                break;

            case "toggle":
                IMSBridge.combinedBridgeEnabled = !IMSBridge.combinedBridgeEnabled;
                ConfigUtils.saveConfigValue("combinedBridgeEnabled", String.valueOf(IMSBridge.combinedBridgeEnabled));
                TextUtils.printToChat(
                        IMSBridge.combinedBridgeEnabled
                                ? "§aEnabled combined bridge messages!"
                                : "§cDisabled combined bridge messages!"
                );
                break;

            case "colour":
                if (args.length < 4) {
                    IMSBridge.cbridgeC1 = COLOR_CODE_MAP.get("dark_red");
                    IMSBridge.cbridgeC2 = COLOR_CODE_MAP.get("gold");
                    IMSBridge.cbridgeC3 = COLOR_CODE_MAP.get("white");
                    ConfigUtils.saveConfigValue("combinedBridgeC1", IMSBridge.cbridgeC1);
                    ConfigUtils.saveConfigValue("combinedBridgeC2", IMSBridge.cbridgeC2);
                    ConfigUtils.saveConfigValue("combinedBridgeC3", IMSBridge.cbridgeC3);
                    printToChat("§cYou have set the combined bridge colour format to: \n" + IMSBridge.cbridgeC1 + "CB > " + IMSBridge.cbridgeC2 + "Username" + "§f: " + IMSBridge.cbridgeC3 + "Message");
                } else {
                    String c1 = args[1].toLowerCase(Locale.ROOT);
                    String c2 = args[2].toLowerCase(Locale.ROOT);
                    String c3 = args[3].toLowerCase(Locale.ROOT);
                    if (!COLOR_CODE_MAP.containsKey(c1)
                            || !COLOR_CODE_MAP.containsKey(c2)
                            || !COLOR_CODE_MAP.containsKey(c3)) {
                        TextUtils.printToChat("§cInvalid colour! Valid options: " + COLOR_CODE_MAP.keySet());
                    } else {
                        IMSBridge.cbridgeC1 = COLOR_CODE_MAP.get(c1);
                        IMSBridge.cbridgeC2 = COLOR_CODE_MAP.get(c2);
                        IMSBridge.cbridgeC3 = COLOR_CODE_MAP.get(c3);
                        ConfigUtils.saveConfigValue("combinedBridgeC1", IMSBridge.cbridgeC1);
                        ConfigUtils.saveConfigValue("combinedBridgeC2", IMSBridge.cbridgeC2);
                        ConfigUtils.saveConfigValue("combinedBridgeC3", IMSBridge.cbridgeC3);
                        printToChat("§cYou have set the combined bridge colour format to: \n" + IMSBridge.cbridgeC1 + "CB > " + IMSBridge.cbridgeC2 + "Username" + "§f: " + IMSBridge.cbridgeC3 + "Message");
                    }
                }
                break;

            case "chat":
                if (!IMSBridge.combinedBridgeEnabled) {
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
                break;

            case "party":
                if (args.length < 3) {
                    TextUtils.printToChat("§cUsage: /cbridge party <number of people> <reason>");
                    return;
                }
                int partyCap;
                try {
                    partyCap = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    TextUtils.printToChat("§cUsage: /cbridge party <number of people> <reason>");
                    return;
                }
                partySpotsLeft = partyCap;
                String reason = "";
                for (int i = 2; i < args.length; i++) {
                    reason += args[i] + " ";
                }

                String message = reason.trim() + ". Do !join " + Minecraft.getMinecraft().thePlayer.getName() + " to join! (" + partyCap + " spots)";

                if (!combinedBridgeEnabled) {
                    printToChat("§cYou need to enable combined bridge messages to use this command! §6§o/cbridge toggle");
                } else {
                    if (wsClient != null && wsClient.isOpen()) { // Check if WebSocket client is connected
                        JsonObject payload = new JsonObject();
                        payload.addProperty("from", "mc");
                        payload.addProperty("msg", message);
                        payload.addProperty("combinedbridge", true);
                        ImsWebSocketClient.wsClient.send(payload.toString());
                        lastParty = System.currentTimeMillis();
                    } else {
                        printToChat("§cYou are not connected to the bridge websocket server!");
                    }
                }

                break;

            default:
                TextUtils.printToChat("§cUnknown subcommand! Try /cbridge help|toggle|colour|chat");
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    private void printHelp() {
        String helpMsg = "§b§l-- Combined Bridge Help -- \n" +
                "§b/cbridge toggle: §6(alias /cbt) §7Enables/disables cbridge message rendering. \n" +
                "§b/cbridge colour <colour1> <colour2> <colour3>: §7Sets the colour formatting of rendered cbridge messages. \n" +
                "§b/cbridge colour: §7Sets the colour formatting back to default. \n" +
                "§b/cbridge chat: §6(alias /cbc or /bc) §7Enter/exit cbridge chat (like how \"/chat guild\" works). \n" +
                "§b/cbc <msg> §6or §b/bc <msg>: §7Sends msg to cbridge.";

        FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(helpMsg));
    }
}
