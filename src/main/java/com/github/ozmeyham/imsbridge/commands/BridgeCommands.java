package com.github.ozmeyham.imsbridge.commands;

import com.github.ozmeyham.imsbridge.IMSBridge;
import com.github.ozmeyham.imsbridge.ImsWebSocketClient;
import com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils;
import com.github.ozmeyham.imsbridge.utils.ConfigUtils;
import com.github.ozmeyham.imsbridge.utils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.*;

import static com.github.ozmeyham.imsbridge.ImsWebSocketClient.wsClient;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class BridgeCommands extends CommandBase {
    public static final Map<String, String> COLOR_CODE_MAP;
    static {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("black", "§0");
        m.put("dark_blue", "§1");
        m.put("dark_green", "§2");
        m.put("dark_aqua", "§3");
        m.put("dark_red", "§4");
        m.put("dark_purple", "§5");
        m.put("gold", "§6");
        m.put("gray", "§7");
        m.put("dark_gray", "§8");
        m.put("blue", "§9");
        m.put("green", "§a");
        m.put("aqua", "§b");
        m.put("red", "§c");
        m.put("light_purple", "§d");
        m.put("yellow", "§e");
        m.put("white", "§f");
        COLOR_CODE_MAP = Collections.unmodifiableMap(m);
    }

    @Override
    public String getCommandName() {
        return "bridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bridge <help|key|toggle|colour|online>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            TextUtils.printToChat("§cUsage: /bridge <help|key|toggle|colour|online>");
            return;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "help":
                printHelp();
                break;

            case "key":
                if (args.length < 2) {
                    printToChat("§cUsage: /bridge key <key>");
                } else {
                    String key = args[1];
                    BridgeKeyUtils.bridgeKey = key;
                    if (BridgeKeyUtils.isValidBridgeKey()) {
                        ConfigUtils.saveConfigValue("bridgeKey", key);
                        ConfigUtils.loadConfig();
                        printToChat("§cBridge key saved as: §f" + key);
                        ImsWebSocketClient.connectWebSocket();
                    } else {
                        printToChat("§cInvalid bridge key format! Check you pasted correctly.");
                    }
                }
                break;

            case "toggle":
                IMSBridge.bridgeEnabled = !IMSBridge.bridgeEnabled;
                ConfigUtils.saveConfigValue("bridgeEnabled", String.valueOf(IMSBridge.bridgeEnabled));
                printToChat(
                        IMSBridge.bridgeEnabled
                                ? "§aEnabled guild bridge messages!"
                                : "§cDisabled guild bridge messages!"
                );
                break;

            case "colour":
                if (args.length < 4) {
                    IMSBridge.bridgeC1 = COLOR_CODE_MAP.get("dark_green");
                    IMSBridge.bridgeC2 = COLOR_CODE_MAP.get("gold");
                    IMSBridge.bridgeC3 = COLOR_CODE_MAP.get("white");
                    ConfigUtils.saveConfigValue("bridgeC1", IMSBridge.bridgeC1);
                    ConfigUtils.saveConfigValue("bridgeC2", IMSBridge.bridgeC2);
                    ConfigUtils.saveConfigValue("bridgeC3", IMSBridge.bridgeC3);
                    printToChat("§cYou have set the bridge colour format to: \n" + IMSBridge.bridgeC1 + "Guild > " + IMSBridge.bridgeC2 + "Username" + " §9[DISC]§f: " + IMSBridge.bridgeC3 + "Message");
                } else {
                    String c1 = args[1].toLowerCase(Locale.ROOT);
                    String c2 = args[2].toLowerCase(Locale.ROOT);
                    String c3 = args[3].toLowerCase(Locale.ROOT);
                    if (!COLOR_CODE_MAP.containsKey(c1)
                            || !COLOR_CODE_MAP.containsKey(c2)
                            || !COLOR_CODE_MAP.containsKey(c3)) {
                        printToChat("§cInvalid colour! Valid options: " + COLOR_CODE_MAP.keySet());
                    } else {
                        IMSBridge.bridgeC1 = COLOR_CODE_MAP.get(c1);
                        IMSBridge.bridgeC2 = COLOR_CODE_MAP.get(c2);
                        IMSBridge.bridgeC3 = COLOR_CODE_MAP.get(c3);
                        ConfigUtils.saveConfigValue("bridgeC1", IMSBridge.bridgeC1);
                        ConfigUtils.saveConfigValue("bridgeC2", IMSBridge.bridgeC2);
                        ConfigUtils.saveConfigValue("bridgeC3", IMSBridge.bridgeC3);
                        printToChat("§cYou have set the bridge colour format to: \n" + IMSBridge.bridgeC1 + "Guild > " + IMSBridge.bridgeC2 + "Username" + " §9[DISC]§f: " + IMSBridge.bridgeC3 + "Message");

                    }
                }
                break;

            case "online":
                if (wsClient != null && wsClient.isOpen()) {
                    wsClient.send("{\"from\":\"mc\",\"request\":\"getOnlinePlayers\"}");
                } else {
                    printToChat("§cYou are not connected to the bridge server!");
                }
                break;

            default:
                printToChat("§cUnknown subcommand! Try /bridge help");
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    private void printHelp() {
        String helpMsg = "§9§l-- Bridge Help -- \n" +
                "§9/bridge key <key>: §7Sets your bridge key; obtain key via discord bot. \n" +
                "§9/bridge toggle: §7Enables/disables bridge message rendering. \n" +
                "§9/bridge colour <colour1> <colour2> <colour3>: §7Sets the colour formatting of rendered bridge messages. \n" +
                "§9/bridge colour: §7Sets the colour formatting back to default. \n" +
                "§9/bridge online: §6(alias /bl) §7Shows a list of online guildmates using this mod.";

        FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(helpMsg));
    }
}
