package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;

public class BridgeColourCommand extends CommandBase {
    public static String bridgeC1 = "§9";
    public static String bridgeC2 = "§6";
    public static String bridgeC3 = "§f";

    public static final List<String> VALID_COLORS = Arrays.asList(
            "black", "dark_blue", "dark_green", "dark_aqua", "dark_red", "dark_purple",
            "gold", "gray", "dark_gray", "blue", "green", "aqua", "red", "light_purple",
            "yellow", "white"
    );

    public static final Map<String, String> COLOR_CODE_MAP = new HashMap<String, String>() {{
        put("black", "§0");
        put("dark_blue", "§1");
        // ... (rest of color mappings)
    }};

    @Override
    public String getCommandName() {
        return "bridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bridge colour [color1] [color2] [color3] - Set bridge message colors";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("colour")) {
            if (args.length == 1) {
                // Reset to default
                bridgeC1 = "§9"; bridgeC2 = "§6"; bridgeC3 = "§f";
                sender.addChatMessage(new ChatComponentText("§cReset bridge colour format to default."));
            } else {
                // Set colors based on arguments
                bridgeC1 = COLOR_CODE_MAP.getOrDefault(args[1], "§9");
                bridgeC2 = args.length > 2 ? COLOR_CODE_MAP.getOrDefault(args[2], "§6") : bridgeC1;
                bridgeC3 = args.length > 3 ? COLOR_CODE_MAP.getOrDefault(args[3], "§f") : bridgeC2;
            }

            bridgeColourFormat(sender);
        }
    }

    private void bridgeColourFormat(ICommandSender sender) {
        saveConfigValue("bridge_colour1", bridgeC1);
        saveConfigValue("bridge_colour2", bridgeC2);
        saveConfigValue("bridge_colour3", bridgeC3);

        sender.addChatMessage(new ChatComponentText("§cYou have set the bridge colour format to: \n" +
                bridgeC1 + "Bridge > " + bridgeC2 + "Username: " + bridgeC3 + "Message"));
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length > 1 && args[0].equals("colour")) {
            return VALID_COLORS;
        }
        return null;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}