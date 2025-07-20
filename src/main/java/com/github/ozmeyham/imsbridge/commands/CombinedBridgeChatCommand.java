package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.github.ozmeyham.imsbridge.IMSBridge;

public class CombinedBridgeChatCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "cbridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cbridge chat";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1 || !"chat".equalsIgnoreCase(args[0])) {
            throw new CommandException("Usage: " + getCommandUsage(sender));
        }
        IMSBridge.combinedBridgeChatEnabled = !IMSBridge.combinedBridgeChatEnabled;
        sender.addChatMessage(new ChatComponentText(
                EnumChatFormatting.YELLOW + "Combined bridge chat " +
                        (IMSBridge.combinedBridgeChatEnabled ? "enabled" : "disabled")
        ));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
