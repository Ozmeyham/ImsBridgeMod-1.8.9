package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static com.github.ozmeyham.imsbridge.commands.BridgeColourCommand.COLOR_CODE_MAP;
import java.util.List;

public class CombinedBridgeColourCommand extends CommandBase {
    public static String cbridgeC1 = "§4";
    public static String cbridgeC2 = "§6";
    public static String cbridgeC3 = "§f";

    @Override
    public String getCommandName() {
        return "cbridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbridge colour [color1] [color2] [color3] - Set cbridge colors";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("colour")) {
            if (args.length == 1) {
                // Reset to default
                cbridgeC1 = "§4"; cbridgeC2 = "§6"; cbridgeC3 = "§f";
                sender.addChatMessage(new ChatComponentText("§cReset cbridge colour format to default."));
            } else {
                // Set colors based on arguments
                cbridgeC1 = COLOR_CODE_MAP.getOrDefault(args[1], "§4");
                cbridgeC2 = args.length > 2 ? COLOR_CODE_MAP.getOrDefault(args[2], "§6") : cbridgeC1;
                cbridgeC3 = args.length > 3 ? COLOR_CODE_MAP.getOrDefault(args[3], "§f") : cbridgeC2;
            }

            cbridgeColourFormat(sender);
        }
    }

    private void cbridgeColourFormat(ICommandSender sender) {
        saveConfigValue("cbridge_colour1", cbridgeC1);
        saveConfigValue("cbridge_colour2", cbridgeC2);
        saveConfigValue("cbridge_colour3", cbridgeC3);

        sender.addChatMessage(new ChatComponentText("§cYou have set the cbridge colour format to: \n" +
                cbridgeC1 + "CBridge > " + cbridgeC2 + "Username: " + cbridgeC3 + "Message"));
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length > 1 && args[0].equals("colour")) {
            return BridgeColourCommand.VALID_COLORS;
        }
        return null;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}