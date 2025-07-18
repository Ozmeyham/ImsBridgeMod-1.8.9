package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import static com.github.ozmeyham.imsbridge.IMSBridge.*;
import static com.github.ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;

public class BridgeCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "bridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bridge toggle - Toggle bridge messages";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("toggle")) {
            bridgeEnabled = !bridgeEnabled;
            saveConfigValue("bridgeEnabled", String.valueOf(bridgeEnabled));
            sender.addChatMessage(new ChatComponentText(bridgeEnabled ?
                    "§2Enabled bridge messages!" : "§cDisabled bridge messages!"));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}