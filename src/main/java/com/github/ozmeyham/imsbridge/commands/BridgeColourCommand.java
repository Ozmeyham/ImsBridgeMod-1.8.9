package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;


import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.loadConfig; // Assuming you'd want to reload config after setting colors

public class BridgeColourCommand extends CommandBase {


    public static String bridgeC1 = "§9";
    public static String bridgeC2 = "§6";
    public static String bridgeC3 = "§f";


    private static final List<String> VALID_COLORS = Arrays.asList(
            "black", "dark_blue", "dark_green", "dark_aqua", "dark_red", "dark_purple",
            "gold", "gray", "dark_gray", "blue", "green", "aqua", "red", "light_purple",
            "yellow", "white"
    );

    @Override
    public String getCommandName() {
        return "bridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bridge colour [colour1] [colour2] [colour3] or /bridge colour (resets to default)";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0 && args[0].equalsIgnoreCase("colour")) {
            if (args.length == 1) {
                bridgeC1 = "§9";
                bridgeC2 = "§6";
                bridgeC3 = "§f";
                bridgeColourFormat(sender);
                sender.addChatMessage(new ChatComponentText("§cReset bridge colour format to default."));
            } else if (args.length == 2) {
                String color1Name = args[1].toLowerCase();
                if (VALID_COLORS.contains(color1Name)) {
                    EnumChatFormatting chatFormatting = EnumChatFormatting.valueOf(color1Name.toUpperCase());
                    bridgeC1 = chatFormatting.toString();
                    bridgeC2 = bridgeC1;
                    bridgeC3 = bridgeC1;
                    bridgeColourFormat(sender);
                } else {
                    sender.addChatMessage(new ChatComponentText("§cInvalid color name: " + args[1]));
                    sender.addChatMessage(new ChatComponentText("§cValid colors: " + String.join(", ", VALID_COLORS)));
                }
            } else if (args.length == 3) {
                String color1Name = args[1].toLowerCase();
                String color2Name = args[2].toLowerCase();

                if (VALID_COLORS.contains(color1Name) && VALID_COLORS.contains(color2Name)) {
                    EnumChatFormatting chatFormatting1 = EnumChatFormatting.valueOf(color1Name.toUpperCase());
                    EnumChatFormatting chatFormatting2 = EnumChatFormatting.valueOf(color2Name.toUpperCase());
                    bridgeC1 = chatFormatting1.toString();
                    bridgeC2 = chatFormatting2.toString();
                    bridgeC3 = bridgeC2;
                    bridgeColourFormat(sender);
                } else {
                    sender.addChatMessage(new ChatComponentText("§cInvalid color name(s)."));
                    sender.addChatMessage(new ChatComponentText("§cValid colors: " + String.join(", ", VALID_COLORS)));
                }
            } else if (args.length == 4) {
                String color1Name = args[1].toLowerCase();
                String color2Name = args[2].toLowerCase();
                String color3Name = args[3].toLowerCase();

                if (VALID_COLORS.contains(color1Name) && VALID_COLORS.contains(color2Name) && VALID_COLORS.contains(color3Name)) {
                    EnumChatFormatting chatFormatting1 = EnumChatFormatting.valueOf(color1Name.toUpperCase());
                    EnumChatFormatting chatFormatting2 = EnumChatFormatting.valueOf(color2Name.toUpperCase());
                    EnumChatFormatting chatFormatting3 = EnumChatFormatting.valueOf(color3Name.toUpperCase());
                    bridgeC1 = chatFormatting1.toString();
                    bridgeC2 = chatFormatting2.toString();
                    bridgeC3 = chatFormatting3.toString();
                    bridgeColourFormat(sender);
                } else {
                    sender.addChatMessage(new ChatComponentText("§cInvalid color name(s)."));
                    sender.addChatMessage(new ChatComponentText("§cValid colors: " + String.join(", ", VALID_COLORS)));
                }
            } else {
                sender.addChatMessage(new ChatComponentText("§cUsage: " + getCommandUsage(sender)));
            }
        } else {
            sender.addChatMessage(new ChatComponentText("§cUsage: " + getCommandUsage(sender)));
        }
    }

    public static void bridgeColourFormat(ICommandSender sender) {
        saveConfigValue("bridge_colour1", bridgeC1);
        saveConfigValue("bridge_colour2", bridgeC2);
        saveConfigValue("bridge_colour3", bridgeC3);
        loadConfig();

        sender.addChatMessage(new ChatComponentText("§cYou have set the bridge colour format to: \n" + bridgeC1 + "Bridge > " + bridgeC2 + "Username: " + bridgeC3 + "Message"));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, net.minecraft.util.BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "colour");
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("colour")) {
            return getListOfStringsMatchingLastWord(args, VALID_COLORS);
        }
        return null;
    }
}