package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import com.github.ozmeyham.imsbridge.IMSBridge;

public class BridgeToggleAliasCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "bt";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bt";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        IMSBridge.bridgeEnabled = !IMSBridge.bridgeEnabled;
        sender.addChatMessage(new ChatComponentText(
                EnumChatFormatting.GREEN +
                        "Guild bridge " + (IMSBridge.bridgeEnabled ? "enabled" : "disabled") + "!"
        ));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
