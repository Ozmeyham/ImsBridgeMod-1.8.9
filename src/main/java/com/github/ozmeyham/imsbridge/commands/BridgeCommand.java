package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.github.ozmeyham.imsbridge.IMSBridge;

public class BridgeCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "bridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bridge toggle";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1 || !"toggle".equalsIgnoreCase(args[0])) {
            throw new CommandException("Usage: " + getCommandUsage(sender));
        }

        IMSBridge.bridgeEnabled = !IMSBridge.bridgeEnabled;
        sender.addChatMessage(new ChatComponentText(
                EnumChatFormatting.YELLOW + "Guild bridge " +
                        (IMSBridge.bridgeEnabled ? "enabled" : "disabled")
        ));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
