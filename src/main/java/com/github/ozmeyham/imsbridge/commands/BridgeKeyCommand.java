package com.github.ozmeyham.imsbridge.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.github.ozmeyham.imsbridge.utils.BridgeKeyUtils;

public class BridgeKeyCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "bridgekey";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bridgekey <key>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) {
            throw new CommandException("Usage: " + getCommandUsage(sender));
        }

        String key = args[0];
        if (!BridgeKeyUtils.uuidValidator(key)) {
            sender.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.RED + "Invalid bridge key format!"
            ));
            return;
        }

        BridgeKeyUtils.bridgeKey = key;
        sender.addChatMessage(new ChatComponentText(
                EnumChatFormatting.GREEN + "Bridge key set to: " + key
        ));
        BridgeKeyUtils.checkBridgeKey();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
