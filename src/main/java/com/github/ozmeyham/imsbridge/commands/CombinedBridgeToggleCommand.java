package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.github.ozmeyham.imsbridge.IMSBridge;

public class CombinedBridgeToggleCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "cbridgetoggle";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbridgetoggle";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        IMSBridge.combinedBridgeEnabled = !IMSBridge.combinedBridgeEnabled;
        sender.addChatMessage(new ChatComponentText(
                EnumChatFormatting.YELLOW + "Combined bridge messages " +
                        (IMSBridge.combinedBridgeEnabled ? "enabled" : "disabled")
        ));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
