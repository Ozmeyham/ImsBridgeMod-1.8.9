package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import static com.github.ozmeyham.imsbridge.IMSBridge.combinedBridgeEnabled;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;

public class CombinedBridgeToggleCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "cbridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbridge toggle - Toggle combined bridge messages";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("toggle")) {
            combinedBridgeEnabled = !combinedBridgeEnabled;
            saveConfigValue("combinedBridgeEnabled", String.valueOf(combinedBridgeEnabled));
            sender.addChatMessage(new ChatComponentText(combinedBridgeEnabled ?
                    "§aEnabled combined bridge messages!" : "§cDisabled combined bridge messages!"));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}