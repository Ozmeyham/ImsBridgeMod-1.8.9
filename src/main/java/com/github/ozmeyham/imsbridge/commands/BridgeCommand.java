package com.github.ozmeyham.imsbridge.commands;

import com.github.ozmeyham.imsbridge.IMSBridge;
import com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils;
import com.github.ozmeyham.imsbridge.utils.ConfigUtils;
import com.github.ozmeyham.imsbridge.utils.TextUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.*;

public class BridgeCommand extends CommandBase {
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
    public List<String> getCommandAliases() {
        return Collections.singletonList("");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bridge <help|key|toggle|color>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            printHelp();
            return;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "help":
                printHelp();
                break;

            case "key":
                if (args.length < 2) {
                    TextUtils.printToChat("§cUsage: /bridge key <key>");
                } else {
                    String key = args[1];
                    BridgeKeyUtils.bridgeKey = key;
                    ConfigUtils.saveConfigValue("bridgeKey", key);
                    TextUtils.printToChat("§aBridge key set to: §f" + key);
                }
                break;

            case "toggle":
                IMSBridge.bridgeEnabled = !IMSBridge.bridgeEnabled;
                ConfigUtils.saveConfigValue("bridgeEnabled", String.valueOf(IMSBridge.bridgeEnabled));
                TextUtils.printToChat(
                        IMSBridge.bridgeEnabled
                                ? "§aEnabled guild bridge!"
                                : "§cDisabled guild bridge!"
                );
                break;

            case "color":
                if (args.length < 4) {
                    TextUtils.printToChat("§cUsage: /bridge color <tagColor> <senderColor> <msgColor>");
                } else {
                    String c1 = args[1].toLowerCase(Locale.ROOT);
                    String c2 = args[2].toLowerCase(Locale.ROOT);
                    String c3 = args[3].toLowerCase(Locale.ROOT);
                    if (!COLOR_CODE_MAP.containsKey(c1)
                            || !COLOR_CODE_MAP.containsKey(c2)
                            || !COLOR_CODE_MAP.containsKey(c3)) {
                        TextUtils.printToChat("§cInvalid color! Valid options: " + COLOR_CODE_MAP.keySet());
                    } else {
                        IMSBridge.bridgeC1 = COLOR_CODE_MAP.get(c1);
                        IMSBridge.bridgeC2 = COLOR_CODE_MAP.get(c2);
                        IMSBridge.bridgeC3 = COLOR_CODE_MAP.get(c3);
                        ConfigUtils.saveConfigValue("bridgeC1", IMSBridge.bridgeC1);
                        ConfigUtils.saveConfigValue("bridgeC2", IMSBridge.bridgeC2);
                        ConfigUtils.saveConfigValue("bridgeC3", IMSBridge.bridgeC3);
                        TextUtils.printToChat(
                                "§aBridge colors set to: §f" + c1 + " §f" + c2 + " §f" + c3
                        );
                    }
                }
                break;

            default:
                TextUtils.printToChat("§cUnknown subcommand! Try /bridge help");
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    private void printHelp() {
        String helpMsg = "§6=== Guild Bridge Commands === \n" +
                "§c/bridge help §f- Shows all available bridge commands along with helpful descriptions \n" +
                "§c/bridge key <key> §f- Sets your bridge key (If you don't have this do /key in Discord) \n" +
                "§c/bridge toggle §f- Toggles guild bridge on or off \n" +
                "§c/bridge color <Color1> <Color2> <Color3> §f- Sets colors of Tag, Sender, and Message respectively for guild bridge \n" +
                "§6=== Combined Bridge Commands === \n" +
                "§c/cbridge toggle §f- Toggles combined bridge on or off \n" +
                "§c/cbridge color <Color1> <Color2> <Color3> §f- Sets colors of Tag, Sender, and Message respectively for combined bridge \n" +
                "§c/bc <message> §f- Sends a message to combined bridge \n" +
                "§c/cbridge chat §f- Sets your selected chat to combined bridge";

        FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(helpMsg));
    }
}
