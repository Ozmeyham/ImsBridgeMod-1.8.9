package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;

// Import your custom utility classes for Forge 1.8.9
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.loadConfig; // Assuming you'd want to reload config after setting colors
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class CombinedBridgeColourCommand extends CommandBase {

    // Available colours
    private static final List<String> VALID_COLORS = Arrays.asList(
            "black", "dark_blue", "dark_green", "dark_aqua", "dark_red", "dark_purple",
            "gold", "gray", "dark_gray", "blue", "green", "aqua", "red", "light_purple",
            "yellow", "white"
    );
    // Default bridge colour formatting
    public static String cbridgeC1 = "§4";
    public static String cbridgeC2 = "§6";
    public static String cbridgeC3 = "§f";

    @Override
    public String getCommandName() {
        return "cbridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbridge colour [colour4] [colour5] [colour6] or /cbridge colour (resets to default)";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0 && args[0].equalsIgnoreCase("colour")) {
            if (args.length == 1) { // /cbridge colour
                cbridgeC1 = "§4";
                cbridgeC2 = "§6";
                cbridgeC3 = "§f";
                cbridgeColourFormat(sender);
                sender.addChatMessage(new ChatComponentText("§cReset cbridge colour format to default."));
            } else if (args.length == 2) { // /cbridge colour <colour4>
                String colorName = args[1].toLowerCase();
                if (VALID_COLORS.contains(colorName)) {
                    EnumChatFormatting chatFormatting = EnumChatFormatting.valueOf(colorName.toUpperCase());
                    cbridgeC1 = chatFormatting.toString();
                    cbridgeC2 = cbridgeC1;
                    cbridgeC3 = cbridgeC1;
                    cbridgeColourFormat(sender);
                } else {
                    sender.addChatMessage(new ChatComponentText("§cInvalid color name: " + args[1]));
                    sender.addChatMessage(new ChatComponentText("§cValid colors: " + String.join(", ", VALID_COLORS)));
                }
            } else if (args.length == 3) { // /cbridge colour <colour4> <colour5>
                String color1Name = args[1].toLowerCase();
                String color2Name = args[2].toLowerCase();

                if (VALID_COLORS.contains(color1Name) && VALID_COLORS.contains(color2Name)) {
                    EnumChatFormatting chatFormatting1 = EnumChatFormatting.valueOf(color1Name.toUpperCase());
                    EnumChatFormatting chatFormatting2 = EnumChatFormatting.valueOf(color2Name.toUpperCase());
                    cbridgeC1 = chatFormatting1.toString();
                    cbridgeC2 = chatFormatting2.toString();
                    cbridgeC3 = cbridgeC2;
                    cbridgeColourFormat(sender);
                } else {
                    sender.addChatMessage(new ChatComponentText("§cInvalid color name(s)."));
                    sender.addChatMessage(new ChatComponentText("§cValid colors: " + String.join(", ", VALID_COLORS)));
                }
            } else if (args.length == 4) { // /cbridge colour <colour4> <colour5> <colour6>
                String color1Name = args[1].toLowerCase();
                String color2Name = args[2].toLowerCase();
                String color3Name = args[3].toLowerCase();

                if (VALID_COLORS.contains(color1Name) && VALID_COLORS.contains(color2Name) && VALID_COLORS.contains(color3Name)) {
                    EnumChatFormatting chatFormatting1 = EnumChatFormatting.valueOf(color1Name.toUpperCase());
                    EnumChatFormatting chatFormatting2 = EnumChatFormatting.valueOf(color2Name.toUpperCase());
                    EnumChatFormatting chatFormatting3 = EnumChatFormatting.valueOf(color3Name.toUpperCase());
                    cbridgeC1 = chatFormatting1.toString();
                    cbridgeC2 = chatFormatting2.toString();
                    cbridgeC3 = chatFormatting3.toString();
                    cbridgeColourFormat(sender);
                } else {
                    sender.addChatMessage(new ChatComponentText("§cInvalid color name(s)."));
                    sender.addChatMessage(new ChatComponentText("§cValid colors: " + String.join(", ", VALID_COLORS)));
                }
            } else {
                sender.addChatMessage(new ChatComponentText("§cUsage: " + getCommandUsage(sender)));
            }
        } else {
            // This command only handles "colour" subcommand.
            // If you have other "cbridge" subcommands, you'd add more checks here.
            sender.addChatMessage(new ChatComponentText("§cUsage: " + getCommandUsage(sender)));
        }
    }

    // Helper method for sending messages and saving config
    public static void cbridgeColourFormat(ICommandSender sender) {
        // These methods should be implemented in your Forge 1.8.9 ConfigUtils
        saveConfigValue("cbridge_colour1", cbridgeC1);
        saveConfigValue("cbridge_colour2", cbridgeC2);
        saveConfigValue("cbridge_colour3", cbridgeC3);
        // Reload config to ensure immediate application if needed from config file
        loadConfig();

        printToChat("§cYou have set the cbridge colour format to: \n" + cbridgeC1 + "CBridge > " + cbridgeC2 + "Username: " + cbridgeC3 + "Message");
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true; // Allow all players to use this client-side command
    }

    // For tab completion (optional but recommended)
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