package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.List;

import static com.github.ozmeyham.imsbridge.IMSBridge.combinedBridgeEnabled;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.loadConfig;
import static com.github.ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class CombinedBridgeToggleCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "cbridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbridge toggle: Toggles combined bridge messages on/off.";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
            if (combinedBridgeEnabled) {
                combinedBridgeEnabled = false;
                saveConfigValue("combinedBridgeEnabled", "false");
                printToChat("§cDisabled combined bridge messages!");
            } else {
                combinedBridgeEnabled = true;
                saveConfigValue("combinedBridgeEnabled", "true");
                printToChat("§aEnabled combined bridge messages!");
            }
            loadConfig();
        } else {
            sender.addChatMessage(new ChatComponentText("§cUsage: " + getCommandUsage(sender)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, net.minecraft.util.BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "toggle");
        }
        return null;
    }
}